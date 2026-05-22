package model;

public class Vaga {
    private int numero;
    private String tipo; // Carro ou Mota
    private boolean estaOcupada;

    public Vaga(int numero, String tipo) {
        this.numero = numero;
        this.tipo = tipo;
        this.estaOcupada = false;
    }

    // Getters e Setters
    public int getNumero() { return numero; }
    public String getTipo() { return tipo; }
    public boolean isEstaOcupada() { return estaOcupada; }
    public void setEstaOcupada(boolean estaOcupada) { this.estaOcupada = estaOcupada; }
}