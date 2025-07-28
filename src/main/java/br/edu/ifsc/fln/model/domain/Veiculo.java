package br.edu.ifsc.fln.model.domain;

public class Veiculo {
    private int id;
    private String placa;
    private String observacoes;
    private Modelo modelo;
    private Cor cor;
    private Cliente cliente;
    private OrdemServico ordemServico;



    public Veiculo(){

    }

    public Veiculo(int id, String placa, String observacoes, Modelo modelo, Cor cor) {
        this.id = id;
        this.placa = placa;
        this.observacoes = observacoes;
        this.modelo = modelo;
        this.cor = cor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

   public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public boolean donoTemDireitoDesconto() {
        return this.cliente.temDireitoDesconto();
    }

    @Override
    public String toString() {
        return placa;
    }
}
