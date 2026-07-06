package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa um registo imutável de uma saída de veículo.
 * Usado para construir o histórico de transacções da sessão,
 * que depois alimenta o relatório diário exportado.
 */
public class RegistoSaida {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final String        matricula;
    private final String        tipo;
    private final int           numeroVaga;
    private final LocalDateTime horaEntrada;
    private final LocalDateTime horaSaida;
    private final double        valorPago;

    public RegistoSaida(String matricula, String tipo, int numeroVaga,
                        LocalDateTime horaEntrada, LocalDateTime horaSaida,
                        double valorPago) {
        this.matricula   = matricula;
        this.tipo        = tipo;
        this.numeroVaga  = numeroVaga;
        this.horaEntrada = horaEntrada;
        this.horaSaida   = horaSaida;
        this.valorPago   = valorPago;
    }

    /** Linha formatada para o relatório .txt */
    public String toLinhaRelatorio() {
        long minutos = java.time.Duration.between(horaEntrada, horaSaida).toMinutes();
        return String.format("  %-14s | %-5s | Vaga %2d | Entrada: %s | Saída: %s | %3d min | %6.0f Kzs",
                matricula, tipo, numeroVaga,
                horaEntrada.format(FMT), horaSaida.format(FMT),
                minutos, valorPago);
    }

    // ─── Getters ─────────────────────────────────────────────────────────────
    public String        getMatricula()  { return matricula; }
    public String        getTipo()       { return tipo; }
    public int           getNumeroVaga() { return numeroVaga; }
    public LocalDateTime getHoraEntrada(){ return horaEntrada; }
    public LocalDateTime getHoraSaida() { return horaSaida; }
    public double        getValorPago()  { return valorPago; }
}