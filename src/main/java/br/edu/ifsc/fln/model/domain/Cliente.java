/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

//import DAOExceptions.ExceptionLavacao;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;

/**
 *
 * @author Bolsistas
 */
public abstract class Cliente {
    protected int id;
    protected String nome;
    protected String celular;
    protected String email;
    protected LocalDate dataCadastro;
    //private Pontuacao pontuacao = new Pontuacao();


    public ArrayList<Veiculo> veiculos = new ArrayList();

    public Cliente() {

    }


    public Cliente(int id, String nome, String celular, String email, LocalDate dataCadastro) {
        this.id = id;
        this.nome = nome;
        this.celular = celular;
        this.email = email;
        this.dataCadastro = dataCadastro;
        // this.pontuacao.setQuantidade(0);
    }

    public Cliente(String nome, String celular, String email, LocalDate dataCadastro) {
        this.nome = nome;
        this.celular = celular;
        this.email = email;
        this.dataCadastro = dataCadastro;

    }


    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the celular
     */
    public String getCelular() {
        return celular;
    }

    /**
     * @param celular the celular to set
     */
    public void setCelular(String celular) {
        this.celular = celular;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the dataCadastro
     */
    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    /**
     * @param dataCadastro the dataCadastro to set
     */
    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }


    /**
     * @return the pontuacao
     */
    /**
     * public Pontuacao getPontuacao() {
     * return pontuacao;
     * }
     */
    public void add(Veiculo v) {
        veiculos.add(v);
        v.setCliente(this);
    }

    public void remove(Veiculo v) {
        for (Veiculo i : veiculos) {
            if (v.getId() == i.getId()) {
                veiculos.remove(v);
                System.out.print("Veículo " + v.getObservacoes() + " removido da lista!");
            }

        }
    }

    @Override
    public String toString() {return nome;}
    }
