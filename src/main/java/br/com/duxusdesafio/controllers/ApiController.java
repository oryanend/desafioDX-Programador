package br.com.duxusdesafio.controllers;

import br.com.duxusdesafio.DTO.ClubeMaisRecorrenteDTO;
import br.com.duxusdesafio.DTO.FuncaoMaisRecorrenteDTO;
import br.com.duxusdesafio.DTO.IntegranteDTO;
import br.com.duxusdesafio.DTO.TimeDaDataDTO;
import br.com.duxusdesafio.service.ApiProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ApiController {
    @Autowired private ApiProcessingService service;

    /** Consulta o time de uma determinada data. */
    @GetMapping("/time-da-data")
    public ResponseEntity<TimeDaDataDTO> timeDaData(
            @RequestParam("data")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data)
    {
        return ResponseEntity.ok().body(service.timeDaData(data));
    }

    /** Consulta o integrante mais utilizado no período. */
    @GetMapping("/integrante-mais-usado")
    public ResponseEntity<IntegranteDTO> integranteMaisUsado(
            @RequestParam(required = false, name = "data-inicial")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false, name = "data-final")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal)
    {
        return ResponseEntity.ok().body(service.integranteMaisUsado(dataInicial, dataFinal));
    }

    /** Consulta os integrantes do time mais recorrente. */
    @GetMapping("/integrantes-time-mais-recorrente")
    public ResponseEntity<List<String>> integrantesDoTimeMaisRecorrente(
            @RequestParam(required = false, name = "data-inicial")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false, name = "data-final")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal)
    {
        return ResponseEntity.ok().body(service.integrantesDoTimeMaisRecorrente(dataInicial, dataFinal));
    }

    /** Consulta a função mais recorrente no período. */
    @GetMapping("/funcao-mais-recorrente")
    public ResponseEntity<FuncaoMaisRecorrenteDTO> funcaoMaisRecorrente(
            @RequestParam(required = false, name = "data-inicial")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false, name = "data-final")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal)
    {
        return ResponseEntity.ok().body(service.funcaoMaisRecorrente(dataInicial, dataFinal));
    }

    /** Consulta o clube mais recorrente no período. */
    @GetMapping("/clube-mais-recorrente")
    public ResponseEntity<ClubeMaisRecorrenteDTO> clubeMaisRecorrente(
            @RequestParam(required = false, name = "data-inicial")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false, name = "data-final")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal)
    {
        return ResponseEntity.ok().body(service.clubeMaisRecorrente(dataInicial, dataFinal));
    }

    /** Consulta a contagem de clubes no período. */
    @GetMapping("/contagem-clubes")
    public ResponseEntity<Map<String, Long>> contagemDeClubesNoPeriodo(
            @RequestParam(required = false, name = "data-inicial")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false, name = "data-final")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal)
    {
        return ResponseEntity.ok().body(service.contagemDeClubesNoPeriodo(dataInicial, dataFinal));
    }

    /** Consulta a contagem de funções no período. */
    @GetMapping("/contagem-funcao")
    public ResponseEntity<Map<String, Long>> contagemPorFuncao(
            @RequestParam(required = false, name = "data-inicial")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false, name = "data-final")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal)
    {
        return ResponseEntity.ok().body(service.contagemPorFuncao(dataInicial, dataFinal));
    }
}
