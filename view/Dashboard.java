package view;

import model.SessaoStats;
import model.Vaga;
import model.Veiculo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Dashboard {

    private static final String DUPLA   = "╔══════════════════════════════════════════════════════════════╗";
    private static final String SIMPLES = "╠══════════════════════════════════════════════════════════════╣";
    private static final String FECHO   = "╚══════════════════════════════════════════════════════════════╝";
    private static final String LAT     = "║";
    private static final int    W       = 64; // largura interna (entre bordas)

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter FMT_HORA = DateTimeFormatter.ofPattern("HH:mm");
    /**
     * Exibe o dashboard completo.
     *
     * @param vagas            Lista de vagas do parque
     * @param stats            Estatísticas financeiras da sessão
     * @param veiculoMaisAntigo Veículo activo há mais tempo (pode ser null)
     * @param tempoMedioMin    Tempo médio de permanência em minutos
     */
    public void exibirDashboard(List<Vaga> vagas, SessaoStats stats,
                                Veiculo veiculoMaisAntigo, long tempoMedioMin) {

        int total    = vagas.size();
        int ocupadas = (int) vagas.stream().filter(Vaga::isEstaOcupada).count();
        int livres   = total - ocupadas;
        double taxa  = total > 0 ? (ocupadas * 100.0 / total) : 0.0;

        System.out.println();


        System.out.println(DUPLA);
        centrar("JMR  –  SISTEMA DE GESTÃO DE ESTACIONAMENTO");
        centrar("Dashboard  │  " + LocalDateTime.now().format(FMT));
        centrar("Sessão iniciada às " + stats.getInicioSessao().format(FMT_HORA)
                + "  │  Duração: " + formatarDuracao(stats.getDuracaoSessaoMinutos()));

        System.out.println(SIMPLES);
        centrar("▌ OCUPAÇÃO DO PARQUE ▐");
        linha();

        stat("Total de Lugares",  String.valueOf(total));
        stat("Lugares Livres",    livres   + "  " + icones(livres,   total, "🟢"));
        stat("Lugares Ocupados",  ocupadas + "  " + icones(ocupadas, total, "🔴"));
        stat("Taxa de Ocupação",  String.format("%.1f%%", taxa));
        linha();
        barraOcupacao(ocupadas, total);
        linha();


        System.out.println(SIMPLES);
        centrar("▌ FINANCEIRO DA SESSÃO ▐");
        linha();

        stat("Receita Total",          String.format("%,.0f Kzs", stats.getReceitaTotal()));
        stat("  ↳ Carros",             String.format("%,.0f Kzs", stats.getReceitaCarros()));
        stat("  ↳ Motas",              String.format("%,.0f Kzs", stats.getReceitaMotas()));
        stat("Receita Média/Veículo",  String.format("%,.0f Kzs", stats.getReceitaMedia()));
        linha();
        stat("Lucro Estimado (70%)",   String.format("%,.0f Kzs", stats.getLucroEstimado()));
        linha();
        stat("Entradas esta sessão",   String.valueOf(stats.getTotalEntradas()));
        stat("Saídas  esta sessão",    String.valueOf(stats.getTotalSaidas()));
        stat("Tempo médio perm.",      tempoMedioMin > 0 ? formatarDuracao(tempoMedioMin) : "N/D");
        linha();


        System.out.println(SIMPLES);
        centrar("▌ VEÍCULO MAIS ANTIGO ▐");
        linha();
        if (veiculoMaisAntigo != null) {
            long min = Duration.between(veiculoMaisAntigo.getHoraEntrada(), LocalDateTime.now()).toMinutes();
            stat("Matrícula",  veiculoMaisAntigo.getMatricula());
            stat("Tipo",       veiculoMaisAntigo.getTipo());
            stat("Entrada",    veiculoMaisAntigo.getHoraEntrada().format(FMT));
            stat("Tempo no parque", formatarDuracao(min) + (min > 120 ? "  ⚠ LONGA ESTADIA" : ""));
        } else {
            centrar("(Nenhum veículo activo neste momento)");
        }
        linha();


        System.out.println(SIMPLES);
        centrar("▌ MAPA DO PARQUE ▐");
        centrar("[ L ] = Livre     [MAT] = Ocupado");
        System.out.println(SIMPLES);
        System.out.println();

        exibirMapa(vagas);

        System.out.println();
        System.out.println(SIMPLES);
        centrar("ZONA CARROS: Vagas 01–05     │     ZONA MOTAS: Vagas 06–08");
        System.out.println(FECHO);
        System.out.println();
    }


    private void exibirMapa(List<Vaga> vagas) {
        List<Vaga> carros = vagas.stream().filter(v -> v.getTipo().equalsIgnoreCase("Carro")).toList();
        List<Vaga> motas  = vagas.stream().filter(v -> v.getTipo().equalsIgnoreCase("Mota")).toList();

        System.out.println("  ┌──────────────────── ZONA CARROS ─────────────────────┐");
        System.out.println("  │                                                        │");
        System.out.println("  │   ENTRADA ──►                         ◄── SAÍDA       │");
        System.out.println("  │           ╔══════════════════════════════╗             │");

        imprimirFila("  │  FILA A   ║ ", carros, 0, Math.min(3, carros.size()), " ║             │");
        System.out.println("  │           ╠══════════════════════════════╣             │");
        System.out.println("  │  CORREDOR ╬═══════ PASSAGEM ═════════════╬═══          │");
        System.out.println("  │           ╠══════════════════════════════╣             │");

        imprimirFila("  │  FILA B   ║ ", carros, 3, carros.size(), " ║             │");
        System.out.println("  │           ╚══════════════════════════════╝             │");
        System.out.println("  │                                                        │");
        System.out.println("  └────────────────────────────────────────────────────────┘");

        System.out.println();

        System.out.println("  ┌──────────────────── ZONA MOTAS ──────────────────────┐");
        System.out.println("  │                                                        │");
        imprimirFila("  │  MOTAS    ║ ", motas, 0, motas.size(), " ║                      │");
        System.out.println("  │                                                        │");
        System.out.println("  └────────────────────────────────────────────────────────┘");
    }

    private void imprimirFila(String pre, List<Vaga> vagas, int ini, int fim, String suf) {
        StringBuilder sb = new StringBuilder(pre);
        for (int i = ini; i < fim && i < vagas.size(); i++) {
            Vaga v = vagas.get(i);
            if (v.isEstaOcupada()) {
                String mat = v.getMatriculaAtual() != null ? v.getMatriculaAtual() : "OCP";
                String tag = mat.length() >= 3 ? mat.substring(0, 3).toUpperCase() : pad(mat, 3);
                sb.append("[").append(tag).append("]");
            } else {
                sb.append("[ L ]");
            }
            if (i < fim - 1 && i < vagas.size() - 1) sb.append(" ");
        }
        System.out.println(sb + suf);
    }


    private void centrar(String texto) {
        int pad = Math.max(0, (W - texto.length()) / 2);
        System.out.printf("%s%-" + W + "s%s%n", LAT, " ".repeat(pad) + texto, LAT);
    }

    private void linha() {
        System.out.printf("%s%-" + W + "s%s%n", LAT, "", LAT);
    }

    private void stat(String etiqueta, String valor) {
        String conteudo = String.format("  %-28s :  %s", etiqueta, valor);
        System.out.printf("%s%-" + W + "s%s%n", LAT, conteudo, LAT);
    }

    private void barraOcupacao(int ocupadas, int total) {
        int barW   = 32;
        int cheio  = total > 0 ? (int) Math.round((double) ocupadas / total * barW) : 0;
        int vazio  = barW - cheio;
        String barra = "  Ocupação  [" + "█".repeat(cheio) + "░".repeat(vazio) + "]"
                + String.format(" %d/%d", ocupadas, total);
        System.out.printf("%s%-" + W + "s%s%n", LAT, barra, LAT);
    }

    private String icones(int valor, int total, String icone) {
        if (total == 0) return "";
        int num = (int) Math.round((double) valor / total * 8);
        return icone.repeat(Math.max(0, num));
    }

    private String formatarDuracao(long minutos) {
        if (minutos < 60) return minutos + " min";
        return (minutos / 60) + "h " + (minutos % 60) + "min";
    }

    private String pad(String s, int len) {
        return String.format("%-" + len + "s", s);
    }
}