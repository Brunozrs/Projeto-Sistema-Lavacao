package br.edu.ifsc.fln.model.domain;

public class Motor {
    private int id;
    private int potencia;
    private ETipoCombustivel tipoCombustivel = ETipoCombustivel.GASOLINA;

    private Modelo modelo;

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public ETipoCombustivel getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(ETipoCombustivel tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }
}
