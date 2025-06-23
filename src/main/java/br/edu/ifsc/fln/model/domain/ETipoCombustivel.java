package br.edu.ifsc.fln.model.domain;

public enum ETipoCombustivel {
    //GASOLINA,ETANOL,FLEX,DIESEL,GNV,OUTRO;
    GASOLINA(1, "Gasolina", "Gas"), ETANOL(2, "Etanol", "Ethanol"), FLEX(3, "Flex", "Flex"),DIESEL(4,"Diesel","Diesel"),GNV(5,"Gnv","Gnv"),OUTRO(6,"Outro","Other");
    private int id;
    private String descricao;
    private String description;

    private ETipoCombustivel(int id, String descricao, String description) {
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

