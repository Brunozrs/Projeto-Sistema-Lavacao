package br.edu.ifsc.fln.model.domain;

public class ItemOS {
    private  int id;
    private double valorServico;
    private String observacoes;

    private Servico servico;
    private OrdemServico ordemServico;

    public ItemOS(){}

    public ItemOS(String observacoes, Servico servico) {
        this.observacoes = observacoes;
        this.servico = servico;
    }

    public ItemOS(double valorServico, String observacoes, Servico servico, OrdemServico ordemServico) {
        this.valorServico = valorServico;
        this.observacoes = observacoes;
        this.servico = servico;
        this.ordemServico = ordemServico;
    }


    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public double getValorServico() {
        if (this.valorServico == 0) {
            return servico.getValor();
        }else{
            return valorServico;
        }
    }

    public void setValorServico(double valorServico) {
        this.valorServico = valorServico;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    @Override
    public String toString() {
        return this.observacoes;
    }

}