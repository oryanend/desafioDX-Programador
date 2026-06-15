package br.com.duxusdesafio.controllers;

import br.com.duxusdesafio.DTO.ClubeMaisRecorrenteDTO;
import br.com.duxusdesafio.DTO.FuncaoMaisRecorrenteDTO;
import br.com.duxusdesafio.DTO.IntegranteDTO;
import br.com.duxusdesafio.DTO.TimeDaDataDTO;
import br.com.duxusdesafio.controllers.handlers.ControllerExceptionHandler;
import br.com.duxusdesafio.service.ApiProcessingService;
import br.com.duxusdesafio.service.exceptions.ResourceNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.duxusdesafio.factory.IntegranteFactory.createIntegrante;
import static br.com.duxusdesafio.factory.TimeFactory.createTimeDaDataDTO;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(ControllerExceptionHandler.class)
public class ApiControllerTest {
    @Autowired private MockMvc mockMvc;

    @MockBean private ApiProcessingService service;

    private TimeDaDataDTO validTimeDaDataDTO;


    private LocalDate existingData;
    private LocalDate nonExistingData;
    private LocalDate validDataInicial, validDataFinal;

    private IntegranteDTO validIntegranteDTO;

    private List<String> validIntegrantesDoTimeMaisRecorrente;
    private Map<String, Long> validContagemDeClubesNoPeriodo;
    private Map<String, Long> validContagemPorFuncao;

    private FuncaoMaisRecorrenteDTO funcaoMaisRecorrenteDTO;
    private ClubeMaisRecorrenteDTO clubeMaisRecorrenteDTO;

    @Before
    public void setUp() throws Exception {
        validTimeDaDataDTO = createTimeDaDataDTO();
        funcaoMaisRecorrenteDTO = new FuncaoMaisRecorrenteDTO("Atacante");
        clubeMaisRecorrenteDTO = new ClubeMaisRecorrenteDTO(validTimeDaDataDTO.getClube());

        existingData = validTimeDaDataDTO.getData();
        nonExistingData = LocalDate.of(2020, 1, 1);
        validDataInicial = LocalDate.of(2021, 1, 1);
        validDataFinal = LocalDate.of(2021, 12, 31);

        validIntegranteDTO = new IntegranteDTO(createIntegrante());
        validIntegrantesDoTimeMaisRecorrente = Arrays.asList("Integrante 1", "Integrante 2", "Integrante 3");

        validContagemDeClubesNoPeriodo = new HashMap<>();
        validContagemDeClubesNoPeriodo.put("Santos", 5L);
        validContagemDeClubesNoPeriodo.put("Palmeiras", 3L);
        validContagemDeClubesNoPeriodo.put("Corinthians", 2L);

        validContagemPorFuncao = new HashMap<>();
        validContagemPorFuncao.put("Atacante", 10L);
        validContagemPorFuncao.put("Defensor", 7L);
        validContagemPorFuncao.put("Goleiro", 5L);
    }

    /**
     * Testes sobre o endpoint GET `/api/v1/time-da-data`
     */
    @Test
    public void getTimeDaDataShouldReturnTimeWhenDataHasTime() throws Exception {
        when(service.timeDaData(existingData)).thenReturn(validTimeDaDataDTO);

        mockMvc.perform(get("/api/v1/time-da-data").param("data", existingData.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(existingData.toString()))
                .andExpect(jsonPath("$.clube").value(validTimeDaDataDTO.getClube()))
                .andExpect(jsonPath("$.integrantes.length()").value(3))
        ;

        verify(service).timeDaData(existingData);
    }

    @Test
    public void getTimeDaDataShouldReturnResourceNotFoundWhenDataHasNoTime() throws Exception {
        when(service.timeDaData(nonExistingData)).thenThrow(new ResourceNotFoundException("Time não encontrado para a data: " + nonExistingData));

        mockMvc.perform(get("/api/v1/time-da-data").param("data", nonExistingData.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Time não encontrado para a data: " + nonExistingData))
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.path").isNotEmpty())
        ;

        verify(service).timeDaData(nonExistingData);
    }

    /**
     * Testes sobre o endpoint GET `/api/v1/integrante-mais-usado`
     */
    @Test
    public void getIntegranteMaisUsadoShouldReturnIntegranteMaisUsadoWhenExistingData() throws Exception {
        when(service.integranteMaisUsado(existingData, existingData)).thenReturn(validIntegranteDTO);

        mockMvc.perform(get("/api/v1/integrante-mais-usado")
                        .param("data-inicial", existingData.toString())
                        .param("data-final", existingData.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validIntegranteDTO.getId()))
                .andExpect(jsonPath("$.nome").value(validIntegranteDTO.getNome()))
                .andExpect(jsonPath("$.funcao").value(validIntegranteDTO.getFuncao()))
        ;
    }

    @Test
    public void getIntegranteMaisUsadoShouldReturnIntegranteMaisUsadoWhenDataInicialAndDataFinalIsNull() throws Exception {
        when(service.integranteMaisUsado(null, null)).thenReturn(validIntegranteDTO);

        mockMvc.perform(get("/api/v1/integrante-mais-usado"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validIntegranteDTO.getId()))
                .andExpect(jsonPath("$.nome").value(validIntegranteDTO.getNome()))
                .andExpect(jsonPath("$.funcao").value(validIntegranteDTO.getFuncao()))
        ;
    }

    @Test
    public void getIntegranteMaisUsadoShouldReturnResourceNotFoundWhenIntegranteIsNotFound() throws Exception {
        when(service.integranteMaisUsado(existingData, existingData)).thenThrow(new ResourceNotFoundException("Integrante mais usado não encontrado para o período: " + existingData + " a " + existingData));

        mockMvc.perform(get("/api/v1/integrante-mais-usado")
                        .param("data-inicial", existingData.toString())
                        .param("data-final", existingData.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Integrante mais usado não encontrado para o período: " + existingData + " a " + existingData))
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.path").isNotEmpty())
        ;

        verify(service).integranteMaisUsado(existingData, existingData);
    }

    /**
     * Testes sobre o endpoint GET `/api/v1/integrantes-time-mais-recorrente`
     */
    @Test
    public void getIntegrantesDoTimeMaisRecorrenteShouldReturnIntegrantesDoTimeWhenDataInicialAndDataFinalExists() throws Exception {
        when(service.integrantesDoTimeMaisRecorrente(existingData, existingData)).thenReturn(validIntegrantesDoTimeMaisRecorrente);

        mockMvc.perform(get("/api/v1/integrantes-time-mais-recorrente")
                        .param("data-inicial", existingData.toString())
                        .param("data-final", existingData.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(validIntegrantesDoTimeMaisRecorrente.size()))
                .andExpect(jsonPath("$[0]").value(validIntegrantesDoTimeMaisRecorrente.get(0)))
                .andExpect(jsonPath("$[1]").value(validIntegrantesDoTimeMaisRecorrente.get(1)))
                .andExpect(jsonPath("$[2]").value(validIntegrantesDoTimeMaisRecorrente.get(2)))
        ;
    }

    @Test
    public void getIntegrantesDoTimeMaisRecorrenteShouldReturnIntegrantesDoTimeWhenDataInicialAndDataFinalDoesNotExists() throws Exception {
        when(service.integrantesDoTimeMaisRecorrente(null, null)).thenReturn(validIntegrantesDoTimeMaisRecorrente);

        mockMvc.perform(get("/api/v1/integrantes-time-mais-recorrente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(validIntegrantesDoTimeMaisRecorrente.size()))
                .andExpect(jsonPath("$[0]").value(validIntegrantesDoTimeMaisRecorrente.get(0)))
                .andExpect(jsonPath("$[1]").value(validIntegrantesDoTimeMaisRecorrente.get(1)))
                .andExpect(jsonPath("$[2]").value(validIntegrantesDoTimeMaisRecorrente.get(2)))
        ;
    }

    @Test
    public void getIntegrantesDoTimeMaisRecorrenteShouldReturnResourceNotFoundWhenTimeDoesNotExist() throws Exception {
        when(service.integrantesDoTimeMaisRecorrente(nonExistingData, nonExistingData)).thenThrow(new ResourceNotFoundException("Time mais recorrente não encontrado para o período: " + nonExistingData + " a " + nonExistingData));

        mockMvc.perform(get("/api/v1/integrantes-time-mais-recorrente")
                        .param("data-inicial", nonExistingData.toString())
                        .param("data-final", nonExistingData.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Time mais recorrente não encontrado para o período: " + nonExistingData + " a " + nonExistingData))
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.path").isNotEmpty())
        ;

        verify(service).integrantesDoTimeMaisRecorrente(nonExistingData, nonExistingData);
    }

    /**
     * Testes sobre o endpoint GET `/api/v1/funcao-mais-recorrente`
     */
    @Test
    public void getFuncaoMaisRecorrenteShouldReturnFuncao() throws Exception {
        when(service.funcaoMaisRecorrente(existingData, existingData))
                .thenReturn(funcaoMaisRecorrenteDTO);

        mockMvc.perform(get("/api/v1/funcao-mais-recorrente")
                        .param("data-inicial", existingData.toString())
                        .param("data-final", existingData.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.funcao").value(funcaoMaisRecorrenteDTO.getFuncao()));

        verify(service).funcaoMaisRecorrente(existingData, existingData);
    }

    @Test
    public void getFuncaoMaisRecorrenteShouldReturnResourceNotFoundWhenFuncaoIsNotFoundBetweenDataInicialAndDataFinal() throws Exception {
        when(service.funcaoMaisRecorrente(nonExistingData, nonExistingData)).thenThrow(new ResourceNotFoundException("Nenhuma função encontrada no período informado"));

        mockMvc.perform(get("/api/v1/funcao-mais-recorrente")
                        .param("data-inicial", nonExistingData.toString())
                        .param("data-final", nonExistingData.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Nenhuma função encontrada no período informado"))
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.path").isNotEmpty());

        verify(service).funcaoMaisRecorrente(nonExistingData, nonExistingData);
    }

    /**
     * Testes sobre o endpoint GET `/api/v1/clube-mais-recorrente`
     */
    @Test
    public void getClubeMaisRecorrenteShouldReturnNomeDoClubeMaisRecorrente() throws Exception {
        when(service.clubeMaisRecorrente(existingData, existingData))
                .thenReturn(clubeMaisRecorrenteDTO);

        mockMvc.perform(get("/api/v1/clube-mais-recorrente")
                        .param("data-inicial", existingData.toString())
                        .param("data-final", existingData.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomeDoClube").value(clubeMaisRecorrenteDTO.getNomeDoClube()));

        verify(service).clubeMaisRecorrente(existingData, existingData);
    }

    @Test
    public void getClubeMaisRecorrenteShouldReturnResourceNotFoundWhenClubeIsNotFoundBetweenDataInicialAndDataFinal() throws Exception {
        when(service.clubeMaisRecorrente(nonExistingData, nonExistingData)).thenThrow(new ResourceNotFoundException("Nenhum clube encontrado no período informado"));

        mockMvc.perform(get("/api/v1/clube-mais-recorrente")
                        .param("data-inicial", nonExistingData.toString())
                        .param("data-final", nonExistingData.toString()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Nenhum clube encontrado no período informado"))
                .andExpect(jsonPath("$.error").value("Resource not found"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.path").isNotEmpty());

        verify(service).clubeMaisRecorrente(nonExistingData, nonExistingData);
    }

    /**
     * Testes sobre o endpoint GET `/api/v1/contagem-clubes`
     */
    @Test
    public void getContagemDeClubesNoPeriodoShouldReturnContagemDeClubes() throws Exception {
        when(service.contagemDeClubesNoPeriodo(validDataInicial, validDataFinal)).thenReturn(validContagemDeClubesNoPeriodo);

        mockMvc.perform(get("/api/v1/contagem-clubes")
                        .param("data-inicial", validDataInicial.toString())
                        .param("data-final", validDataFinal.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.Santos").value(validContagemDeClubesNoPeriodo.get("Santos").intValue()))
                .andExpect(jsonPath("$.Palmeiras").value(validContagemDeClubesNoPeriodo.get("Palmeiras").intValue()))
                .andExpect(jsonPath("$.Corinthians").value(validContagemDeClubesNoPeriodo.get("Corinthians").intValue()));

        verify(service).contagemDeClubesNoPeriodo(validDataInicial, validDataFinal);
    }

    /**
     * Testes sobre o endpoint GET `/api/v1/contagem-funcao`
     */
    @Test
    public void getContagemPorFuncaoShouldReturnContagemPorFuncao() throws Exception {
        when(service.contagemPorFuncao(validDataInicial, validDataFinal)).thenReturn(validContagemPorFuncao);

        mockMvc.perform(get("/api/v1/contagem-funcao")
                        .param("data-inicial", validDataInicial.toString())
                        .param("data-final", validDataFinal.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.Atacante").value(validContagemPorFuncao.get("Atacante").intValue()))
                .andExpect(jsonPath("$.Defensor").value(validContagemPorFuncao.get("Defensor").intValue()))
                .andExpect(jsonPath("$.Goleiro").value(validContagemPorFuncao.get("Goleiro").intValue()));

        verify(service).contagemPorFuncao(validDataInicial, validDataFinal);
    }
}
