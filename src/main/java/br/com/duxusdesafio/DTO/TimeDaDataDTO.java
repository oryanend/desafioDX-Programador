package br.com.duxusdesafio.DTO;

import br.com.duxusdesafio.model.Time;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TimeDaDataDTO {
    private LocalDate data;
    private String clube;
    private List<String> integrantes;

    public TimeDaDataDTO() {
    }

    public TimeDaDataDTO(LocalDate data, String clube, List<String> integrantes) {
        this.data = data;
        this.clube = clube;
        this.integrantes = integrantes;
    }

    public TimeDaDataDTO(Time entity) {
        this.data = entity.getData();
        this.clube = entity.getNomeDoClube();
        this.integrantes = entity.getComposicaoTime().stream()
                .map(integrante -> integrante.getIntegrante().getNome())
                .collect(Collectors.toList());
    }

    public LocalDate getData() {
        return data;
    }

    public String getClube() {
        return clube;
    }

    public List<String> getIntegrantes() {
        return integrantes;
    }

    @Override
    public String toString() {
        return String.format(
                "Data: %s%nClube: %s%nIntegrantes: %s",
                data,
                clube,
                integrantes
        );
    }
}
