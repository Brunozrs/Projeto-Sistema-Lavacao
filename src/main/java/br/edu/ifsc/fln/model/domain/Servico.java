package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author PC
 */
public class    Servico {
    private int id;
    private String descricao;
    private double valor;
    private int pontos;
    ECategoria categoria = ECategoria.PADRAO;

    public Servico() {
    }

    public Servico(int id, String descricao, double valor, ECategoria categoria) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {this.valor = valor;}

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public ECategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(ECategoria categoria) {
        this.categoria = categoria;
    }


    @Override
    public String toString() {
        return descricao;
    }

}