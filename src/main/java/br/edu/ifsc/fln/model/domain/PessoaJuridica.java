package br.edu.ifsc.fln.model.domain;

import java.sql.Date;

public class PessoaJuridica extends Cliente{
    private String cnpj;
    private String inscricaoEstadual;



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
