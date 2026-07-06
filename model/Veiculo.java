package model;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Representa um veículo que entra ou saiu do parque.
 * Suporta dois construtores: um para entrada nova, outro para
 * reconstrução a partir do ficheiro (com horaEntrada já conhecida).
 */
public class Veiculo {

    private String        matricula;
    private String        tipo;
    private int           numeroVagaOcupada;
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSaida;
    private boolean       estaAtivo;

    private static final double TARIFA_CARRO = 500.0;
    private static final double TARIFA_MOTA  = 300.0;

    /**
     * Construtor para nova entrada — horaEntrada = agora.
     * @param matricula
     * @param tipo
     * @param numeroVagaOcupada
     */
    public Veiculo(String matricula, String tipo, int numeroVagaOcupada) {
        this.matricula         = matricula;
        this.tipo              = tipo;
        this.numeroVagaOcupada = numeroVagaOcupada;
        this.horaEntrada       = LocalDateTime.now();
        this.estaAtivo         = true;
    }

    /**
     * Construtor para reconstrução a partir do ficheiro.
     * Preserva a horaEntrada original para o cálculo correcto do valor.
     * @param matricula
     * @param tipo
     * @param numeroVagaOcupada
     * @param horaEntrada
     */
    public Veiculo(String matricula, String tipo, int numeroVagaOcupada, LocalDateTime horaEntrada) {
        this.matricula         = matricula;
        this.tipo              = tipo;
        this.numeroVagaOcupada = numeroVagaOcupada;
        this.horaEntrada       = horaEntrada;   // ← hora original preservada
        this.estaAtivo         = true;
    }

    public double calcularValor() {
        long   minutos      = Duration.between(horaEntrada, horaSaida).toMinutes();
        double horas        = Math.max(1.0, Math.ceil(minutos / 60.0));
        double precoPorHora = tipo.equalsIgnoreCase("Carro") ? TARIFA_CARRO : TARIFA_MOTA;
        return horas * precoPorHora;
    }

    public String        getMatricula()                          { return matricula; }
    public String        getTipo()                               { return tipo; }
    public LocalDateTime getHoraEntrada()                        { return horaEntrada; }
    public boolean       isEstaAtivo()                           { return estaAtivo; }
    public int           getNumeroVagaOcupada()                  { return numeroVagaOcupada; }
    public void          setHoraSaida(LocalDateTime horaSaida)   { this.horaSaida = horaSaida; }
    public void          setEstaAtivo(boolean estaAtivo)         { this.estaAtivo = estaAtivo; }
}