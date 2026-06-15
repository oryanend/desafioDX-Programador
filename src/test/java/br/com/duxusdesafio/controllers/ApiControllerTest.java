package br.com.duxusdesafio.controllers;

import br.com.duxusdesafio.DTO.TimeDaDataDTO;
import br.com.duxusdesafio.controllers.handlers.ControllerExceptionHandler;
import br.com.duxusdesafio.service.ApiProcessingService;
import br.com.duxusdesafio.service.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.Instant;
import java.time.LocalDate;

import static br.com.duxusdesafio.factory.TimeFactory.createTimeDaDataDTO;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(ControllerExceptionHandler.class)
public class ApiControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private ApiProcessingService service;

    private TimeDaDataDTO validTimeDaDataDTO;

    private LocalDate existingData;
    private LocalDate nonExistingData;

    @Before
    public void setUp() throws Exception {
        validTimeDaDataDTO = createTimeDaDataDTO();

        existingData = validTimeDaDataDTO.getData();
        nonExistingData = LocalDate.of(2020, 1, 1);
    }

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

}
