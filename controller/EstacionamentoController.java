package controller;

import model.Vaga;
import model.Veiculo;
import java.util.ArrayList;
import java.util.List;

public class EstacionamentoController {
    private List<Vaga> vagas = new ArrayList<>();
    private List<Veiculo> historicoVeiculos = new ArrayList<>();

    public EstacionamentoController() {
        // Inicializa o parque com 5 vagas de Carro e 3 de Mota para testes
        for (int i = 1; i <= 5; i++) vagas.add(new Vaga(i, "Carro"));
        for (int i = 6; i <= 8; i++) vagas.add(new Vaga(i, "Mota"));
    }

    // Regista a entrada do carro e ocupa uma vaga livre
    public String registarEntrada(String matricula, String tipo) {
        // Verifica se o veículo já está no parque
        for (Veiculo v : historicoVeiculos) {
            if (v.getMatricula().equalsIgnoreCase(matricula) && v.isEstaAtivo()) {
                return "Erro: Este veículo já se encontra dentro do parque!";
            }
        }

        // Procura vaga disponível para o tipo de veículo
        Vaga vagaLivre = null;
        for (Vaga v : vagas) {
            if (!v.isEstaOcupada() && v.getTipo().equalsIgnoreCase(tipo)) {
                vagaLivre = v;
                break;
            }
        }

        if (vagaLivre == null) {
            return "Erro: Não há vagas disponíveis para " + tipo + "s!";
        }

        // Instancia o veículo e ocupa a vaga
        vagaLivre.setEstaOcupada(true);
        Veiculo novoVeiculo = new Veiculo(matricula, tipo, vagaLivre.getNumero());
        historicoVeiculos.add(novoVeiculo);

        return "Sucesso: Veículo registado na Vaga Nº " + vagaLivre.getNumero();
    }

    // Regista a saída, calcula o valor e liberta a vaga
    public String registarSaida(String matricula) {
        for (Veiculo v : historicoVeiculos) {
            if (v.getMatricula().equalsIgnoreCase(matricula) && v.isEstaAtivo()) {
                
                double valor = v.calcularValor();
                v.setEstaAtivo(false);
                
                // Liberta a vaga correspondente
                for (Vaga vaga : vagas) {
                    if (vaga.getNumero() == v.getNumeroVagaOcupada()) {
                        vaga.setEstaOcupada(false);
                        break;
                    }
                }
                
                return "--- RECIBO ---\nMatrícula: " + matricula + "\nValor a pagar: " + valor + " Kzs\nLugar libertado!";
            }
        }
        return "Erro: Veículo com a matrícula inserida não foi encontrado.";
    }

    // Mostra o estado atual das vagas do parque
    public void exibirEstadoParque() {
        System.out.println("\n=== ESTADO DAS VAGAS ===");
        for (Vaga v : vagas) {
            String estado = v.isEstaOcupada() ? "[OCUPADA]" : "[LIVRE]";
            System.out.println("Vaga Nº " + v.getNumero() + " (" + v.getTipo() + "): " + estado);
        }
    }
}