/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package br.edu.ifsc.fln.model.domain;

/**
 *
 * @author mpisc
 */
public enum ECategoria {
    //PEQUENO,MEDIO,GRANDE,MOTO,PADRAO;
    PEQUENO(1, "Pequeno", "Small"), MEDIO(2, "Medio", "Medium"), GRANDE(3, "Grande", "Big"),MOTO(4,"Moto","Motorcycle"),PADRAO(5,"Padrao","Standard");
    private int id;
    private String descricao;
    private String description;

    private ECategoria(int id, String descricao, String description) {
        this.id = id;
        this.descricao = descricao;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDescription() {
        return description;
    }


}