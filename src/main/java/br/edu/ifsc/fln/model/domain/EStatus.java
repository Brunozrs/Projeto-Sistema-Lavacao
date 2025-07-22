package br.edu.ifsc.fln.model.domain;

public enum EStatus {
    ABERTA("Aberta"), FECHADA("Fechada"), CANCELADA("Cancelada");

    private final String descricao;

    EStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}