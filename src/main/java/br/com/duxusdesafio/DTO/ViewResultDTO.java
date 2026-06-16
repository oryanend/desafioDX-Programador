package br.com.duxusdesafio.DTO;

public class ViewResultDTO {
    private String titulo;
    private Object dados;

    public ViewResultDTO() {
    }

    public ViewResultDTO(String titulo, Object dados) {
        this.titulo = titulo;
        this.dados = dados;
    }

    public String getTitulo() {
        return titulo;
    }

    public Object getDados() {
        return dados;
    }
}
