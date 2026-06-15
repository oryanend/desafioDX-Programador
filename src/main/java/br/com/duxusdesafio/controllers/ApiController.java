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

@RestController
@RequestMapping("/api/v1")
public class ApiController {
    @Autowired private ApiProcessingService service;

    @GetMapping("/time-da-data")
    public ResponseEntity<TimeDaDataDTO> timeDaData(
            @RequestParam("data")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data)
    {
        return ResponseEntity.ok().body(service.timeDaData(data));
    }

    @GetMapping("/integrante-mais-usado")
    public ResponseEntity<IntegranteDTO> integranteMaisUsado(
            @RequestParam(required = false, name = "data-inicial")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false, name = "data-final")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal)
    {
        return ResponseEntity.ok().body(service.integranteMaisUsado(dataInicial, dataFinal));
    }

    @GetMapping("/integrantes-time-mais-recorrente")
    public ResponseEntity<List<String>> integrantesDoTimeMaisRecorrente(
            @RequestParam(required = false, name = "data-inicial")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false, name = "data-final")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal)
    {
        return ResponseEntity.ok().body(service.integrantesDoTimeMaisRecorrente(dataInicial, dataFinal));
    }

    @GetMapping("/funcao-mais-recorrente")
    public ResponseEntity<FuncaoMaisRecorrenteDTO> funcaoMaisRecorrente(
            @RequestParam(required = false, name = "data-inicial")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false, name = "data-final")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal)
    {
        return ResponseEntity.ok().body(service.funcaoMaisRecorrente(dataInicial, dataFinal));
    }

    @GetMapping("/clube-mais-recorrente")
    public ResponseEntity<ClubeMaisRecorrenteDTO> clubeMaisRecorrente(
            @RequestParam(required = false, name = "data-inicial")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false, name = "data-final")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal)
    {
        return ResponseEntity.ok().body(service.clubeMaisRecorrente(dataInicial, dataFinal));
    }
}
