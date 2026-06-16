package br.com.duxusdesafio.controllers;

import br.com.duxusdesafio.DTO.IntegranteDTO;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class IntegranteControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    private String validIntegranteNome;
    private String validIntegranteFuncao;
    private IntegranteDTO validIntegranteDTO;

    @Before
    public void setUp() throws Exception {
        validIntegranteNome = "Neymar Jr.";
        validIntegranteFuncao = "Atacante";

        validIntegranteDTO = new IntegranteDTO(validIntegranteNome, validIntegranteFuncao);
    }

    /**
     * Deve cadastrar um integrante e retornar HTTP 201 quando os dados forem válidos.
     */
    @Test
    public void postIntegranteShouldReturnSuccessfulWhenIsValid() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(validIntegranteDTO);

        ResultActions createIntegranteResult = mockMvc
                .perform(post("/api/v1/integrantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON));

        createIntegranteResult
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value(validIntegranteNome))
                .andExpect(jsonPath("$.funcao").value(validIntegranteFuncao))
        ;
    }
}
