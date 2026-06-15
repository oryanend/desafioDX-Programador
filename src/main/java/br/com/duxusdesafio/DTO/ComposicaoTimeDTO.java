package br.com.duxusdesafio.DTO;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;

public class ComposicaoTimeDTO {
    private long id;
    private Time time;
    private Integrante integrante;

    public ComposicaoTimeDTO() {
    }

    public ComposicaoTimeDTO(Time time, Integrante integrante) {
        this.time = time;
        this.integrante = integrante;
    }

    public ComposicaoTimeDTO(ComposicaoTime entity) {
        this.id = entity.getId();
        this.time = entity.getTime();
        this.integrante = entity.getIntegrante();
    }

    public long getId() {
        return id;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Integrante getIntegrante() {
        return integrante;
    }

    public void setIntegrante(Integrante integrante) {
        this.integrante = integrante;
    }
}
