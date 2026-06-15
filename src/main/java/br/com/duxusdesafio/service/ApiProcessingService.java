package br.com.duxusdesafio.service;

import br.com.duxusdesafio.DTO.IntegranteDTO;
import br.com.duxusdesafio.DTO.TimeDaDataDTO;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repositories.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ApiProcessingService {
    @Autowired
    private ApiService apiService;

    @Autowired
    private TimeRepository timeRepository;

    @Transactional(readOnly = true)
    public TimeDaDataDTO timeDaData(LocalDate data){
        Time time = apiService.timeDaData(data, timeRepository.findAll());
        return new TimeDaDataDTO(time);
    }

    @Transactional(readOnly = true)
    public IntegranteDTO integranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal){
        return new IntegranteDTO(apiService.integranteMaisUsado(dataInicial, dataFinal, timeRepository.findAll()));
    }
}
