package model;

import java.time.LocalDateTime;
import java.time.Duration;

public class SessaoStats {


    private int totalEntradas;

    private int totalSaidas;

    private double receitaTotal;

    private double receitaCarros;

    private double receitaMotas;

    private final LocalDateTime inicioSessao;


    public SessaoStats() {
        this.inicioSessao  = LocalDateTime.now();
        this.totalEntradas = 0;
        this.totalSaidas   = 0;
        this.receitaTotal  = 0.0;
        this.receitaCarros = 0.0;
        this.receitaMotas  = 0.0;
    }

    public void registarEntrada() {
        totalEntradas++;
    }

    /**
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



    public long getDuracaoSessaoMinutos() {
        return Duration.between(inicioSessao, LocalDateTime.now()).toMinutes();
    }

 
    public double getReceitaMedia() {
        return totalSaidas > 0 ? receitaTotal / totalSaidas : 0.0;
    }

     
    public double getLucroEstimado() {
        return receitaTotal * 0.70;
    }


    public int           getTotalEntradas()    { return totalEntradas; }
    public int           getTotalSaidas()      { return totalSaidas; }
    public double        getReceitaTotal()     { return receitaTotal; }
    public double        getReceitaCarros()    { return receitaCarros; }
    public double        getReceitaMotas()     { return receitaMotas; }
    public LocalDateTime getInicioSessao()     { return inicioSessao; }
}
