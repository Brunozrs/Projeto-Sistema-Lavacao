package br.edu.ifsc.fln.model.domain;

import java.sql.Date;
import java.time.LocalDate;

public class PessoaJuridica extends Cliente{
    private String cnpj;
    private String inscricaoEstadual;

    public PessoaJuridica() {}


    public PessoaJuridica(int id, String nome, String celular, String emial, LocalDate dataCadastro) {
        super(id, nome, celular, emial, dataCadastro);
    }

    public PessoaJuridica(String cnpj, String inscricaoEstadual, int id, String nome, String celular, String emial, LocalDate dataCadastro) {
        super(id, nome, celular, emial, dataCadastro);
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
    }


    /**@Override
    public String getdados(){
        super.toString();
        return "\nCnpj: " + getCnpj() + "\nInscriÃ§Ã£o Estadual: " + getInscricaoEstadual();
    }

    @Override
    public String getdados(String descricao){
        return super.getdados() + "\nCnpj: " + getCnpj() + "\nInscriÃ§Ã£o Estadual: " + getInscricaoEstadual() + descricao;
    }


    /**
     * @return the cnpj
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * @param cnpj the cnpj to set
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * @return the inscricaoEstadual
     */
    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    /**
     * @param inscricaoEstadual the inscricaoEstadual to set
     */
    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

}
