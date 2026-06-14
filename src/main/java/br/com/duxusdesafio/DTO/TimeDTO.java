package br.com.duxusdesafio.DTO;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Time;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TimeDTO {
    private long id;
    private String nomeDoClube;
    private LocalDate data;
    private List<Long> integrantesIds;

    public TimeDTO() {
    }

    public TimeDTO(String nomeDoClube, LocalDate data, List<Long> integrantesIds) {
        this.nomeDoClube = nomeDoClube;
        this.data = data;
        this.integrantesIds = new ArrayList<>();

        for (Long id : integrantesIds) addIntegrantesIds(id);
    }

    public TimeDTO(Time entity) {
        this.id = entity.getId();
        this.nomeDoClube = entity.getNomeDoClube();
        this.data = entity.getData();
        this.integrantesIds = new ArrayList<>();

        if (entity.getComposicaoTime() != null) {
            for (ComposicaoTime composicao : entity.getComposicaoTime()) {
                this.integrantesIds.add(
                        composicao.getIntegrante().getId()
                );
            }
        }
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNomeDoClube() {
        return nomeDoClube;
    }

    public void setNomeDoClube(String nomeDoClube) {
        this.nomeDoClube = nomeDoClube;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<Long> getIntegrantesIds() {
        return integrantesIds;
    }

    public void addIntegrantesIds(Long id) {
        integrantesIds.add(id);
    }
}
