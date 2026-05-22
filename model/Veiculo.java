package model;

import java.time.LocalDateTime;
import java.time.Duration;

public class Veiculo {
    private String matricula;
    private String tipo; // Carro ou Mota
    private LocalDateTime horaEntrada;
    private LocalDateTime horaSaida;
    private double valorPago;
    private boolean estaAtivo;
    private int numeroVagaOcupada;

    public Veiculo(String matricula, String tipo, int numeroVagaOcupada) {
        this.matricula = matricula;
        this.tipo = tipo;
        this.numeroVagaOcupada = numeroVagaOcupada;
        this.horaEntrada = LocalDateTime.now(); // Captura a hora exata atual
        this.estaAtivo = true;
    }

    // Simulação de cálculo de preço (Ex: 500 Kzs por hora para Carro, 250 Kzs para Mota)
    public double calcularValor() {
        if (this.horaSaida == null) {
            this.horaSaida = LocalDateTime.now();
        }
        
        long minutos = Duration.between(horaEntrada, horaSaida).toMinutes();
        
        // Simulação pedagógica: se passou menos de 1 minuto, conta como 1 hora
        if (minutos == 0) minutos = 1; 
        
        double horas = Math.ceil(minutos / 60.0);
        double precoPorHora = this.tipo.equalsIgnoreCase("Carro") ? 500.0 : 250.0;
        
        this.valorPago = horas * precoPorHora;
        return this.valorPago;
    }

    // Getters e Setters
    public String getMatricula() { return matricula; }
    public String getTipo() { return tipo; }
    public LocalDateTime getHoraEntrada() { return horaEntrada; }
    public void setHoraSaida(LocalDateTime horaSaida) { this.horaSaida = horaSaida; }
    public boolean isEstaAtivo() { return estaAtivo; }
    public void setEstaAtivo(boolean estaAtivo) { this.estaAtivo = estaAtivo; }
    public int getNumeroVagaOcupada() { return numeroVagaOcupada; }
}