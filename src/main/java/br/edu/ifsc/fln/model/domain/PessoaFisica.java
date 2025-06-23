package br.edu.ifsc.fln.model.domain;

import java.sql.Date;

public class PessoaFisica extends Cliente {
    private String cpf;
    private Date dataNascimento;


    /**@Override
    public String getdados(){
        return  super.getdados() + "\nCpf: " + getCpf() + "\nData de Nascimento: " + getDataNascimento();
    }

    @Override
    public String getdados(String descricao){
        return super.getdados() + "\nCpf: " + getCpf() + "\nData de Nascimento: " + getDataNascimento() + descricao;
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * @return the dataNascimento
     */
    public Date getDataNascimento() {
        return dataNascimento;
    }

    /**
     * @param dataNascimento the dataNascimento to set
     */
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

}