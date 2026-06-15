package br.com.duxusdesafio.controllers;

import br.com.duxusdesafio.DTO.IntegranteDTO;
import br.com.duxusdesafio.service.IntegranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "api/v1/integrantes")
public class IntegranteController {
    @Autowired private IntegranteService integranteService;

    @PostMapping
    public ResponseEntity<IntegranteDTO> insert(@RequestBody IntegranteDTO dto){
        dto = integranteService.insert(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(dto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(dto);
    }
}
