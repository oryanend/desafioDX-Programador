package br.com.duxusdesafio.controllers;

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

@RestController
@RequestMapping("/api/v1")
public class ApiController {
    @Autowired private ApiProcessingService service;

    @GetMapping("/time-da-data")
    public ResponseEntity<TimeDaDataDTO> timeDaData(
            @RequestParam("data")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data)
    {
        return ResponseEntity.ok(service.timeDaData(data));
    }

    @GetMapping("/integrante-mais-usado")
    public ResponseEntity<IntegranteDTO> integranteMaisUsado(
            @RequestParam(required = false, name = "data-inicial")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false, name = "data-final")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal)
    {
        return ResponseEntity.ok(service.integranteMaisUsado(dataInicial, dataFinal));
    }


}
