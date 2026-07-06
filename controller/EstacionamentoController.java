package controller;

import model.RegistoSaida;
import model.SessaoStats;
import model.Vaga;
import model.Veiculo;
import persistence.ParqueDadosManager;
import persistence.ParqueDadosManager.ResultadoCarregamento;
import relatorio.RelatorioManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class EstacionamentoController {
    private List<Vaga>        vagas;
    private List<Veiculo>     historicoVeiculos;

    private final SessaoStats      sessaoStats;
    private final List<RegistoSaida> registosSaida;  // histórico de transacções

    private final ParqueDadosManager dadosManager;
    private final RelatorioManager   relatorioManager;

    private static final int NUM_VAGAS_CARRO = 5;
    private static final int NUM_VAGAS_MOTA  = 3;


    public EstacionamentoController() {
        this.dadosManager     = new ParqueDadosManager();
        this.relatorioManager = new RelatorioManager();
        this.sessaoStats      = new SessaoStats();
        this.registosSaida    = new ArrayList<>();
        this.vagas            = new ArrayList<>();
        this.historicoVeiculos = new ArrayList<>();
        inicializarParque();
    }


    private void inicializarParque() {
        ResultadoCarregamento resultado = dadosManager.carregarEstado();
        if (resultado != null && !resultado.vagas.isEmpty()) {
            this.vagas             = resultado.vagas;
            this.historicoVeiculos = resultado.veiculosAtivos;
        } else {
            inicializarPorDefeito();
        }
    }

    private void inicializarPorDefeito() {
        vagas.clear();
        for (int i = 1; i <= NUM_VAGAS_CARRO; i++)
            vagas.add(new Vaga(i, "Carro"));
        for (int i = NUM_VAGAS_CARRO + 1; i <= NUM_VAGAS_CARRO + NUM_VAGAS_MOTA; i++)
            vagas.add(new Vaga(i, "Mota"));
        System.out.println("[SISTEMA] Parque inicializado por defeito ("
                + NUM_VAGAS_CARRO + " Carros + " + NUM_VAGAS_MOTA + " Motas).");
    }
    public String registarEntrada(String matricula, String tipo) {
        for (Veiculo v : historicoVeiculos)
            if (v.getMatricula().equalsIgnoreCase(matricula) && v.isEstaAtivo())
                return "Erro: Este veículo já se encontra dentro do parque!";

        Vaga vagaLivre = null;
        for (Vaga vaga : vagas)
            if (!vaga.isEstaOcupada() && vaga.getTipo().equalsIgnoreCase(tipo)) {
                vagaLivre = vaga; break;
            }

        if (vagaLivre == null)
            return "Erro: Não há vagas disponíveis para " + tipo + "!";

        vagaLivre.setEstaOcupada(true);
        vagaLivre.setMatriculaAtual(matricula);
        historicoVeiculos.add(new Veiculo(matricula, tipo, vagaLivre.getNumero()));

        sessaoStats.registarEntrada();

        return "Sucesso: Veículo [" + matricula + "] registado na Vaga Nº " + vagaLivre.getNumero() + ".";
    }
    public String registarSaida(String matricula) {
        Veiculo veiculo = null;
        for (Veiculo v : historicoVeiculos)
            if (v.getMatricula().equalsIgnoreCase(matricula) && v.isEstaAtivo()) {
                veiculo = v; break;
            }

        if (veiculo == null)
            return "Erro: Veículo com a matrícula [" + matricula + "] não foi encontrado no parque.";

        LocalDateTime horaSaida = LocalDateTime.now();
        veiculo.setHoraSaida(horaSaida);
        double valor = veiculo.calcularValor();
        veiculo.setEstaAtivo(false);

        for (Vaga vaga : vagas)
            if (vaga.getNumero() == veiculo.getNumeroVagaOcupada()) {
                vaga.setEstaOcupada(false);
                vaga.setMatriculaAtual(null);
                break;
            }

        sessaoStats.registarSaida(valor, veiculo.getTipo());
        registosSaida.add(new RegistoSaida(
                veiculo.getMatricula(), veiculo.getTipo(), veiculo.getNumeroVagaOcupada(),
                veiculo.getHoraEntrada(), horaSaida, valor
        ));

        return String.format(
                "╔══ RECIBO ══════════════════════╗%n" +
                "  Matrícula   : %s%n" +
                "  Tipo        : %s%n" +
                "  Vaga Nº     : %d%n" +
                "  Valor Pago  : %.0f Kzs%n" +
                "  Lugar libertado com sucesso!%n" +
                "╚════════════════════════════════╝",
                veiculo.getMatricula(), veiculo.getTipo(),
                veiculo.getNumeroVagaOcupada(), valor
        );
    }

    public void exibirEstadoParque() {
        System.out.println("\n=== ESTADO DAS VAGAS ===");
        for (Vaga vaga : vagas) {
            String estado = vaga.isEstaOcupada()
                    ? "[OCUPADA] ← " + vaga.getMatriculaAtual()
                    : "[LIVRE]";
            System.out.println("Vaga Nº " + vaga.getNumero() + " (" + vaga.getTipo() + "): " + estado);
        }
    }


    public boolean guardarDados() {
        return dadosManager.guardarEstado(vagas, historicoVeiculos);
    }

    public String exportarRelatorio() {
        return relatorioManager.exportarRelatorio(registosSaida, sessaoStats);
    }

    public List<Vaga>    getVagas()        { return Collections.unmodifiableList(vagas); }
    public SessaoStats   getSessaoStats()  { return sessaoStats; }

    public Veiculo getVeiculoMaisAntigo() {
        return historicoVeiculos.stream()
                .filter(Veiculo::isEstaAtivo)
                .min((a, b) -> a.getHoraEntrada().compareTo(b.getHoraEntrada()))
                .orElse(null);
    }

    public long getTempoMedioPermanencia() {
        if (registosSaida.isEmpty()) return 0;
        long totalMin = registosSaida.stream()
                .mapToLong(r -> java.time.Duration.between(r.getHoraEntrada(), r.getHoraSaida()).toMinutes())
                .sum();
        return totalMin / registosSaida.size();
    }
}