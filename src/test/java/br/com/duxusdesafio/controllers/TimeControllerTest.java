package br.com.duxusdesafio.controllers;

import br.com.duxusdesafio.DTO.TimeDTO;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.repositories.IntegranteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static br.com.duxusdesafio.factory.IntegranteFactory.createIntegrante;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TimeControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private IntegranteRepository integranteRepository;

    private String validNomeDoClube;
    private LocalDate validLocalDate;
    private List<Long> validIntegrantesIds;

    private Integrante validIntegrante1;
    private Integrante validIntegrante2;
    private Integrante validIntegrante3;

    private TimeDTO validTimeDTO;

    @Before
    public void setUp() throws Exception {
        validNomeDoClube = "Santos";
        validLocalDate = LocalDate.of(2020, 1, 1);

        validIntegrante1 = integranteRepository.save(createIntegrante());
        validIntegrante2 = integranteRepository.save(createIntegrante());
        validIntegrante3 = integranteRepository.save(createIntegrante());

        List<Long> validIntegrantesIds = Arrays.asList(validIntegrante1.getId(), validIntegrante2.getId(), validIntegrante3.getId());

        validTimeDTO = new TimeDTO(validNomeDoClube, validLocalDate, validIntegrantesIds);
    }

    @Test
    public void postTimeShouldReturnSuccessfulWhenIsValid() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(validTimeDTO);

        ResultActions createIntegranteResult = mockMvc
                .perform(post("/api/v1/times")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON));

        createIntegranteResult
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nomeDoClube").value(validNomeDoClube))
                .andExpect(jsonPath("$.data").value(validLocalDate.toString()))
                .andExpect(jsonPath("$.integrantesIds[0]").value(validIntegrante1.getId()))
                .andExpect(jsonPath("$.integrantesIds[1]").value(validIntegrante2.getId()))
                .andExpect(jsonPath("$.integrantesIds[2]").value(validIntegrante3.getId()))
        ;
    }
}
