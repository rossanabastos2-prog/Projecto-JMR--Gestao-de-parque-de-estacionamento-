package model;

/**
 * Representa uma vaga de estacionamento no parque.
 * Contém o número, o tipo de veículo aceite e o estado de ocupação.
 * A matrícula atual é guardada para suporte à persistência de dados.
 */
public class Vaga {

    private final int numero;
    private final String tipo;          // "Carro" ou "Mota"
    private boolean estaOcupada;
    private String matriculaAtual; // null se livre; matrícula do veículo se ocupada

    /**
     * Cria uma vaga com o número e tipo indicados, inicialmente livre.
     *
     * @param numero Número identificador da vaga (ex: 1, 2, 3...)
     * @param tipo   Tipo de veículo aceite: "Carro" ou "Mota"
     */
    public Vaga(int numero, String tipo) {
        this.numero = numero;
        this.tipo = tipo;
        this.estaOcupada = false;
        this.matriculaAtual = null;
    }

    // ─── Getters e Setters ───────────────────────────────────────────────────

    public int getNumero()                          { return numero; }
    public String getTipo()                         { return tipo; }
    public boolean isEstaOcupada()                  { return estaOcupada; }
    public String getMatriculaAtual()               { return matriculaAtual; }

    public void setEstaOcupada(boolean estaOcupada) { this.estaOcupada = estaOcupada; }
    public void setMatriculaAtual(String matricula) { this.matriculaAtual = matricula; }
}
