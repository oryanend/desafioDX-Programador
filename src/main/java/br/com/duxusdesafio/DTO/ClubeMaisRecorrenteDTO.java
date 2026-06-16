package br.com.duxusdesafio.DTO;

public class ClubeMaisRecorrenteDTO {
    private String nomeDoClube;

    public ClubeMaisRecorrenteDTO() {
    }

    public ClubeMaisRecorrenteDTO(String nomeDoClube) {
        this.nomeDoClube = nomeDoClube;
    }

    public String getNomeDoClube() {
        return nomeDoClube;
    }

    public void setNomeDoClube(String nomeDoClube) {
        this.nomeDoClube = nomeDoClube;
    }

    @Override
    public String toString() {
        return "Clube Mais Recorrente: " + nomeDoClube;
    }
}
