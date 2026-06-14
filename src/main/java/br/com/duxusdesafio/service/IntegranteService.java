package br.com.duxusdesafio.service;

import br.com.duxusdesafio.DTO.IntegranteDTO;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.repositories.IntegranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IntegranteService {
    @Autowired private IntegranteRepository integranteRepository;

    @Transactional
    public IntegranteDTO insert(IntegranteDTO dto){
        Integrante entity = new Integrante();

        copyDtoToEntity(dto, entity);
        entity = integranteRepository.save(entity);

        return new IntegranteDTO(entity);
    }

    private void copyDtoToEntity(IntegranteDTO dto, Integrante entity) {
        entity.setNome(dto.getNome());
        entity.setFuncao(dto.getFuncao());
    }
}
