package persistence;

import model.Vaga;
import model.Veiculo;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Camada de persistência do JMR.
 *
 * Formato do ficheiro parque_dados.txt:
 *
 *   Secção VAGAS:
 *     VAGA;numero;tipo;ocupado;matricula
 *     VAGA;1;Carro;false;-
 *     VAGA;3;Carro;true;LD-23-23-43
 *
 *   Secção VEÍCULOS ACTIVOS (para reconstruir o historicoVeiculos):
 *     VEICULO;matricula;tipo;numeroVaga;horaEntrada
 *     VEICULO;LD-23-23-43;Carro;3;2026-06-19T10:45:00
 */
public class ParqueDadosManager {

    private static final String CAMINHO_FICHEIRO    = "parque_dados.txt";
    private static final String SEPARADOR           = ";";
    private static final String VAGA_LIVRE          = "-";
    private static final String PREFIXO_VAGA        = "VAGA";
    private static final String PREFIXO_VEICULO     = "VEICULO";

    // ─── GUARDAR ────────────────────────────────────────────────────────────

    /**
     * Guarda vagas E veículos activos no ficheiro.
     * Os veículos activos são necessários para calcular o valor na saída
     * após um reinício do programa.
     * @param vagas
     * @param veiculosAtivos
     * @return 
     */
    public boolean guardarEstado(List<Vaga> vagas, List<Veiculo> veiculosAtivos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_FICHEIRO))) {

            writer.write("# JMR - Dados do Parque");
            writer.newLine();
            writer.write("# Formato vagas:    VAGA;numero;tipo;ocupado;matricula");
            writer.newLine();
            writer.write("# Formato veiculos: VEICULO;matricula;tipo;numeroVaga;horaEntrada");
            writer.newLine();

            for (Vaga vaga : vagas) {
                String mat = (vaga.getMatriculaAtual() != null) ? vaga.getMatriculaAtual() : VAGA_LIVRE;
                writer.write(PREFIXO_VAGA
                        + SEPARADOR + vaga.getNumero()
                        + SEPARADOR + vaga.getTipo()
                        + SEPARADOR + vaga.isEstaOcupada()
                        + SEPARADOR + mat);
                writer.newLine();
            }

            for (Veiculo v : veiculosAtivos) {
                if (v.isEstaAtivo()) {
                    writer.write(PREFIXO_VEICULO
                            + SEPARADOR + v.getMatricula()
                            + SEPARADOR + v.getTipo()
                            + SEPARADOR + v.getNumeroVagaOcupada()
                            + SEPARADOR + v.getHoraEntrada().toString());
                    writer.newLine();
                }
            }

            System.out.println("[SISTEMA] Dados guardados com sucesso em '" + CAMINHO_FICHEIRO + "'.");
            return true;

        } catch (IOException e) {
            System.err.println("[ERRO] Não foi possível guardar os dados: " + e.getMessage());
            return false;
        }
    }


    public static class ResultadoCarregamento {
        public final List<Vaga>    vagas;
        public final List<Veiculo> veiculosAtivos;

        public ResultadoCarregamento(List<Vaga> vagas, List<Veiculo> veiculosAtivos) {
            this.vagas          = vagas;
            this.veiculosAtivos = veiculosAtivos;
        }
    }

    public ResultadoCarregamento carregarEstado() {
        File ficheiro = new File(CAMINHO_FICHEIRO);

        if (!ficheiro.exists()) {
            System.out.println("[SISTEMA] Ficheiro não encontrado. A inicializar parque por defeito...");
            return null;
        }

        List<Vaga>    vagas          = new ArrayList<>();
        List<Veiculo> veiculosAtivos = new ArrayList<>();
        int linhaNum = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(ficheiro))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                linhaNum++;
                linha = linha.trim();

                if (linha.isEmpty() || linha.startsWith("#")) continue;

                String[] p = linha.split(SEPARADOR);

                if (p[0].equals(PREFIXO_VAGA) && p.length == 5) {
                    try {
                        int     numero  = Integer.parseInt(p[1].trim());
                        String  tipo    = p[2].trim();
                        boolean ocupada = Boolean.parseBoolean(p[3].trim());
                        String  mat     = p[4].trim();

                        Vaga vaga = new Vaga(numero, tipo);
                        vaga.setEstaOcupada(ocupada);
                        if (ocupada && !mat.equals(VAGA_LIVRE)) {
                            vaga.setMatriculaAtual(mat);
                        }
                        vagas.add(vaga);

                    } catch (NumberFormatException e) {
                        System.err.println("[AVISO] Linha " + linhaNum + " ignorada: " + linha);
                    }

                } else if (p[0].equals(PREFIXO_VEICULO) && p.length == 5) {
                    try {
                        String        mat        = p[1].trim();
                        String        tipo       = p[2].trim();
                        int           numVaga    = Integer.parseInt(p[3].trim());
                        LocalDateTime horaEntrada = LocalDateTime.parse(p[4].trim());

                        Veiculo v = new Veiculo(mat, tipo, numVaga, horaEntrada);
                        veiculosAtivos.add(v);

                    } catch (NumberFormatException e) {
                        System.err.println("[AVISO] Linha " + linhaNum + " ignorada (veículo): " + linha);
                    }
                }
            }

            System.out.println("[SISTEMA] " + vagas.size() + " vagas e "
                    + veiculosAtivos.size() + " veículo(s) activo(s) carregados.");
            return new ResultadoCarregamento(vagas, veiculosAtivos);

        } catch (IOException e) {
            System.err.println("[ERRO] Falha ao ler o ficheiro: " + e.getMessage());
            return null;
        }
    }
}

