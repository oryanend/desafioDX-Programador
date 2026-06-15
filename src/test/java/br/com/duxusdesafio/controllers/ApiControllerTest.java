package br.com.duxusdesafio.controllers;

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
import java.util.List;

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

    private IntegranteDTO validIntegranteDTO;

    private List<String> validIntegrantesDoTimeMaisRecorrente;

    private FuncaoMaisRecorrenteDTO funcaoMaisRecorrenteDTO;

    @Before
    public void setUp() throws Exception {
        validTimeDaDataDTO = createTimeDaDataDTO();
        funcaoMaisRecorrenteDTO = new FuncaoMaisRecorrenteDTO("Atacante");

        existingData = validTimeDaDataDTO.getData();
        nonExistingData = LocalDate.of(2020, 1, 1);

        validIntegranteDTO = new IntegranteDTO(createIntegrante());
        validIntegrantesDoTimeMaisRecorrente = Arrays.asList("Integrante 1", "Integrante 2", "Integrante 3");
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
}
