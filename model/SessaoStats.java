package model;

import java.time.LocalDateTime;
import java.time.Duration;

/**
 * Regista as estatísticas financeiras e operacionais da sessão actual.
 * Vive apenas em memória — é recriado a cada arranque do programa.
 *
 * Responsabilidade (MVC): é um Model puro. Não imprime, não lê ficheiros,
 * só guarda e calcula dados da sessão corrente.
 */
public class SessaoStats {

    // ─── Contadores da sessão ────────────────────────────────────────────────

    /** Total de veículos que entraram nesta sessão. */
    private int totalEntradas;

    /** Total de veículos que saíram (pagaram) nesta sessão. */
    private int totalSaidas;

    /** Soma de todos os pagamentos recebidos nesta sessão (Kzs). */
    private double receitaTotal;

    /** Receita apenas de Carros nesta sessão. */
    private double receitaCarros;

    /** Receita apenas de Motas nesta sessão. */
    private double receitaMotas;

    /** Momento em que o programa foi iniciado. */
    private final LocalDateTime inicioSessao;

    // ─── Construtor ──────────────────────────────────────────────────────────

    public SessaoStats() {
        this.inicioSessao  = LocalDateTime.now();
        this.totalEntradas = 0;
        this.totalSaidas   = 0;
        this.receitaTotal  = 0.0;
        this.receitaCarros = 0.0;
        this.receitaMotas  = 0.0;
    }

    // ─── Métodos de registo ──────────────────────────────────────────────────

    /** Chamado pelo Controller sempre que um veículo entra. */
    public void registarEntrada() {
        totalEntradas++;
    }

    /**
     * Chamado pelo Controller sempre que um veículo sai e paga.
     *
     * @param valor Valor pago em Kzs
     * @param tipo  Tipo do veículo ("Carro" ou "Mota")
     */
    public void registarSaida(double valor, String tipo) {
        totalSaidas++;
        receitaTotal += valor;
        if (tipo.equalsIgnoreCase("Carro")) {
            receitaCarros += valor;
        } else {
            receitaMotas += valor;
        }
    }

    // ─── Cálculos derivados ──────────────────────────────────────────────────

    /**
     * Duração da sessão actual em minutos.
     */
    public long getDuracaoSessaoMinutos() {
        return Duration.between(inicioSessao, LocalDateTime.now()).toMinutes();
    }

    /**
     * Receita média por veículo saído. Devolve 0 se nenhum saiu ainda.
     */
    public double getReceitaMedia() {
        return totalSaidas > 0 ? receitaTotal / totalSaidas : 0.0;
    }

    /**
     * Estimativa de lucro: 70% da receita (custo operacional estimado = 30%).
     * Útil como indicador rápido no dashboard.
     */
    public double getLucroEstimado() {
        return receitaTotal * 0.70;
    }

    // ─── Getters ─────────────────────────────────────────────────────────────

    public int           getTotalEntradas()    { return totalEntradas; }
    public int           getTotalSaidas()      { return totalSaidas; }
    public double        getReceitaTotal()     { return receitaTotal; }
    public double        getReceitaCarros()    { return receitaCarros; }
    public double        getReceitaMotas()     { return receitaMotas; }
    public LocalDateTime getInicioSessao()     { return inicioSessao; }
}
