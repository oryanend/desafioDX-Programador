package br.com.duxusdesafio.service;

import br.com.duxusdesafio.DTO.ClubeMaisRecorrenteDTO;
import br.com.duxusdesafio.DTO.FuncaoMaisRecorrenteDTO;
import br.com.duxusdesafio.DTO.IntegranteDTO;
import br.com.duxusdesafio.DTO.TimeDaDataDTO;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repositories.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    @Transactional(readOnly = true)
    public List<String> integrantesDoTimeMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal){
        return apiService.integrantesDoTimeMaisRecorrente(dataInicial, dataFinal, timeRepository.findAll());
    }

    @Transactional(readOnly = true)
    public FuncaoMaisRecorrenteDTO funcaoMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal){
        return new FuncaoMaisRecorrenteDTO(apiService.funcaoMaisRecorrente(dataInicial, dataFinal, timeRepository.findAll()));
    }

    @Transactional(readOnly = true)
    public ClubeMaisRecorrenteDTO clubeMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal){
        return new ClubeMaisRecorrenteDTO(apiService.clubeMaisRecorrente(dataInicial, dataFinal, timeRepository.findAll()));
    }

    @Transactional(readOnly = true)
    public Map<String, Long> contagemDeClubesNoPeriodo(LocalDate dataInicial, LocalDate dataFinal){
        return apiService.contagemDeClubesNoPeriodo(dataInicial, dataFinal, timeRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Map<String, Long> contagemPorFuncao(LocalDate dataInicial, LocalDate dataFinal){
        return apiService.contagemPorFuncao(dataInicial, dataFinal, timeRepository.findAll());
    }
}
