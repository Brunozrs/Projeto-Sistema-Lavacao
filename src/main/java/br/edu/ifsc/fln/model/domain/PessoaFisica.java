package br.edu.ifsc.fln.model.domain;

import java.sql.Date;
import java.time.LocalDate;

public class PessoaFisica extends Cliente {
    private String cpf;
    private String dataNascimento;

    public PessoaFisica() {}

    public PessoaFisica(String cpf, String dataNascimento) {
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

    public PessoaFisica(String cpf, String dataNascimento, int id, String nome, String celular, String emial, LocalDate dataCadastro) {
        super(id, nome, celular, emial, dataCadastro);
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
    }

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
    public String getDataNascimento() {
        return dataNascimento;
    }

    /**
     * @param dataNascimento the dataNascimento to set
     */
    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

}