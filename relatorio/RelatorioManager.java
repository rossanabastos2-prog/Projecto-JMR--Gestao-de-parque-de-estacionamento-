package relatorio;

import model.RegistoSaida;
import model.SessaoStats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class RelatorioManager {

    private static final DateTimeFormatter FMT_DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter FMT_DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public String exportarRelatorio(List<RegistoSaida> registos, SessaoStats stats) {

        String nomeFicheiro = "relatorio_" + LocalDate.now() + ".txt";

        try (BufferedWriter w = new BufferedWriter(new FileWriter(nomeFicheiro))) {

            escreverCabecalho(w, stats);
            escreverTransaccoes(w, registos);
            escreverResumoFinanceiro(w, stats);
            escreverRodape(w);

            System.out.println("[SISTEMA] Relatório exportado: '" + nomeFicheiro + "'");
            return nomeFicheiro;

        } catch (IOException e) {
            System.err.println("[ERRO] Não foi possível exportar o relatório: " + e.getMessage());
            return null;
        }
    }

    private void escreverCabecalho(BufferedWriter w, SessaoStats stats) throws IOException {
        w.write("=".repeat(80)); w.newLine();
        w.write("       RELATÓRIO DIÁRIO DE ESTACIONAMENTO – SISTEMA JMR"); w.newLine();
        w.write("=".repeat(80)); w.newLine();
        w.newLine();
        w.write("  Data do Relatório : " + LocalDate.now().format(FMT_DATA)); w.newLine();
        w.write("  Gerado em         : " + LocalDateTime.now().format(FMT_DATA_HORA)); w.newLine();
        w.write("  Início de Sessão  : " + stats.getInicioSessao().format(FMT_DATA_HORA)); w.newLine();
        w.write("  Duração de Sessão : " + formatarDuracao(stats.getDuracaoSessaoMinutos())); w.newLine();
        w.newLine();
        w.write("-".repeat(80)); w.newLine();
    }

    private void escreverTransaccoes(BufferedWriter w, List<RegistoSaida> registos) throws IOException {
        w.newLine();
        w.write("  REGISTO DE TRANSACÇÕES (" + registos.size() + " saída(s))"); w.newLine();
        w.newLine();

        if (registos.isEmpty()) {
            w.write("  (Nenhuma saída registada nesta sessão.)"); w.newLine();
        } else {
            w.write("  " + "-".repeat(76)); w.newLine();
            w.write(String.format("  %-14s | %-5s | %-6s | %-16s | %-16s | %5s | %10s",
                    "MATRÍCULA", "TIPO", "VAGA", "ENTRADA", "SAÍDA", "TEMPO", "VALOR"));
            w.newLine();
            w.write("  " + "-".repeat(76)); w.newLine();

            for (RegistoSaida r : registos) {
                w.write(r.toLinhaRelatorio()); w.newLine();
            }

            w.write("  " + "-".repeat(76)); w.newLine();
        }
        w.newLine();
    }

    private void escreverResumoFinanceiro(BufferedWriter w, SessaoStats stats) throws IOException {
        w.write("-".repeat(80)); w.newLine();
        w.newLine();
        w.write("  RESUMO FINANCEIRO DA SESSÃO"); w.newLine();
        w.newLine();
        w.write(String.format("  %-30s : %10.0f Kzs%n", "Receita Total",      stats.getReceitaTotal()));
        w.write(String.format("  %-30s : %10.0f Kzs%n", "  ↳ de Carros",      stats.getReceitaCarros()));
        w.write(String.format("  %-30s : %10.0f Kzs%n", "  ↳ de Motas",       stats.getReceitaMotas()));
        w.write(String.format("  %-30s : %10.0f Kzs%n", "Receita Média/Veíc.",stats.getReceitaMedia()));
        w.write(String.format("  %-30s : %10.0f Kzs%n", "Lucro Estimado (70%):",stats.getLucroEstimado()));
        w.newLine();
        w.write(String.format("  %-30s : %10d%n",       "Total de Entradas",   stats.getTotalEntradas()));
        w.write(String.format("  %-30s : %10d%n",       "Total de Saídas",     stats.getTotalSaidas()));
        w.newLine();
    }

    private void escreverRodape(BufferedWriter w) throws IOException {
        w.write("=".repeat(80)); w.newLine();
        w.write("  Documento gerado automaticamente pelo Sistema JMR."); w.newLine();
        w.write("  Para uso interno. Conserve este relatório para auditoria."); w.newLine();
        w.write("=".repeat(80)); w.newLine();
    }


    private String formatarDuracao(long minutos) {
        if (minutos < 60) return minutos + " min";
        return (minutos / 60) + "h " + (minutos % 60) + "min";
    }
}