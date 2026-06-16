package br.com.duxusdesafio.DTO;

public class FuncaoMaisRecorrenteDTO {
    private String funcao;

    public FuncaoMaisRecorrenteDTO() {
    }

    public FuncaoMaisRecorrenteDTO(String funcao) {
        this.funcao = funcao;
    }

    public String getFuncao() {
        return funcao;
    }

    @Override
    public String toString() {
        return "Função Mais Recorrente: " + funcao;
    }
}
