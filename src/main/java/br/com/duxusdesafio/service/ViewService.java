package br.com.duxusdesafio.service;

import br.com.duxusdesafio.DTO.IntegranteDTO;
import br.com.duxusdesafio.DTO.TimeDTO;
import br.com.duxusdesafio.DTO.ViewResultDTO;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repositories.IntegranteRepository;
import br.com.duxusdesafio.repositories.TimeRepository;
import br.com.duxusdesafio.service.exceptions.InvalidRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ViewService {

    @Autowired
    private IntegranteRepository integranteRepository;

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private IntegranteService integranteService;

    @Autowired
    private TimeService timeService;

    @Autowired
    private ApiProcessingService apiProcessingService;

    public List<Integrante> listarIntegrantes() {
        return integranteRepository.findAll();
    }

    public List<Time> listarTimes() {
        return timeRepository.findAll();
    }

    public void salvarIntegrante(Integrante integrante) {
        integranteService.insert(new IntegranteDTO(integrante.getNome(), integrante.getFuncao()));
    }

    public void salvarTime(String nomeClube, LocalDate data, List<Long> integranteIds) {
        TimeDTO dto = new TimeDTO();
        dto.setNomeDoClube(nomeClube);
        dto.setData(data);

        for (Long integranteId : integranteIds) {
            dto.addIntegrantesIds(integranteId);
        }

        timeService.insert(dto);
    }

    public ViewResultDTO processarResultado(String funcao, LocalDate data, LocalDate dataInicial, LocalDate dataFinal) {
        switch (funcao) {
            case "timeDaData":
                return new ViewResultDTO("Time da Data", apiProcessingService.timeDaData(data));

            case "integranteMaisUsado":
                return new ViewResultDTO("Integrante Mais Usado", apiProcessingService.integranteMaisUsado(dataInicial, dataFinal));

            case "integrantesTimeMaisRecorrente":
                return new ViewResultDTO("Integrantes do Time Mais Recorrente", apiProcessingService.integrantesDoTimeMaisRecorrente(dataInicial, dataFinal));

            case "funcaoMaisRecorrente":
                return new ViewResultDTO("Função Mais Recorrente", apiProcessingService.funcaoMaisRecorrente(dataInicial, dataFinal));

            case "clubeMaisRecorrente":
                return new ViewResultDTO("Clube Mais Recorrente", apiProcessingService.clubeMaisRecorrente(dataInicial, dataFinal));

            case "contagemClubes":
                return new ViewResultDTO("Contagem de Clubes", apiProcessingService.contagemDeClubesNoPeriodo(dataInicial, dataFinal));

            case "contagemFuncoes":
                return new ViewResultDTO("Contagem por Função", apiProcessingService.contagemPorFuncao(dataInicial, dataFinal));

            default:
                throw new InvalidRequest("Consulta inválida, tente utilizar outras formas de consulta ou verifique os parâmetros enviados.");
        }
    }
}
