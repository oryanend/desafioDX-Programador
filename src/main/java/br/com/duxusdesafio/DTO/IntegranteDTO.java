package br.com.duxusdesafio.DTO;

import br.com.duxusdesafio.model.Integrante;

public class IntegranteDTO {
    private long id;
    private String nome;
    private String funcao;

    public IntegranteDTO() {
    }

    public IntegranteDTO(String nome, String funcao) {
        this.nome = nome;
        this.funcao = funcao;
    }

    public IntegranteDTO(Integrante entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.funcao = entity.getFuncao();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }
}
