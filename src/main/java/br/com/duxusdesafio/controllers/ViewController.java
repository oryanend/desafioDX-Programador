package br.com.duxusdesafio.controllers;

import br.com.duxusdesafio.DTO.ViewResultDTO;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.service.ViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/telas")
public class ViewController {

    @Autowired
    private ViewService viewService;

    @GetMapping
    public String home() {
        return "redirect:/telas/integrantes";
    }

    @GetMapping("/integrantes")
    public String paginaIntegrantes(Model model) {
        model.addAttribute("integrantes", viewService.listarIntegrantes());
        model.addAttribute("novoIntegrante", new Integrante());

        return "integrantes";
    }

    @PostMapping("/integrantes")
    public String salvarIntegrante(@ModelAttribute Integrante integrante) {
        viewService.salvarIntegrante(integrante);

        return "redirect:/telas/integrantes";
    }

    @GetMapping("/times")
    public String paginaTimes(Model model) {
        model.addAttribute("times", viewService.listarTimes());
        model.addAttribute("integrantes", viewService.listarIntegrantes());

        return "times";
    }

    @PostMapping("/times")
    public String salvarTime(
            @RequestParam String nomeClube,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam List<Long> integranteIds)
    {
        viewService.salvarTime(nomeClube, data, integranteIds);
        return "redirect:/telas/times";
    }

    @GetMapping("/processamento")
    public String paginaProcessamento() {
        return "processamento";
    }

    @GetMapping("/processamento/buscar")
    public String buscarResultado(
            @RequestParam String funcao,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal,
            Model model
    ){
        ViewResultDTO resultado = viewService.processarResultado(funcao, data, dataInicial, dataFinal);
        model.addAttribute("resultado", resultado);
        model.addAttribute("funcaoSelecionada", funcao);

        return "processamento";
    }
}