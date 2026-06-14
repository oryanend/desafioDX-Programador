package br.com.duxusdesafio.service;

import br.com.duxusdesafio.DTO.TimeDTO;
import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repositories.ComposicaoTimeRepository;
import br.com.duxusdesafio.repositories.IntegranteRepository;
import br.com.duxusdesafio.repositories.TimeRepository;
import br.com.duxusdesafio.service.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TimeService {
    @Autowired private TimeRepository timeRepository;
    @Autowired private IntegranteRepository integranteRepository;
    @Autowired private ComposicaoTimeRepository composicaoTimeRepository;

    @Transactional
    public TimeDTO insert(TimeDTO dto) {
        Time entity = new Time();
        entity.setNomeDoClube(dto.getNomeDoClube());
        entity.setData(dto.getData());

        entity = timeRepository.save(entity);

        for (Long integranteId : dto.getIntegrantesIds()) {
            Integrante integrante = integranteRepository.findById(integranteId)
                    .orElseThrow(() -> new ResourceNotFoundException("Integrante não encontrado. Id: " + integranteId));

            ComposicaoTime composicao = new ComposicaoTime();
            composicao.setTime(entity);
            composicao.setIntegrante(integrante);

            composicaoTimeRepository.save(composicao);
        }

        dto.setId(entity.getId());

        return dto;
    }
}