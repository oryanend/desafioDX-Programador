package br.com.duxusdesafio.service;

import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.service.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service que possuirá as regras de negócio para o processamento dos dados
 * solicitados no desafio!
 *
 * OBS ao candidato: PREFERENCIALMENTE, NÃO ALTERE AS ASSINATURAS DOS MÉTODOS!
 * Trabalhe com a proposta pura.
 *
 * @author carlosau
 */
@Service
public class ApiService {

    /**
     * Vai retornar um Time, com a composição do time daquela data
     */
    public Time timeDaData(LocalDate data, List<Time> todosOsTimes){
        return todosOsTimes.stream()
                .filter(time -> time.getData().equals(data)).findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Time não encontrado para a data: " + data));
    }

    /**
     * Vai retornar o integrante que estiver presente na maior quantidade de times
     * dentro do período
     */
    public Integrante integranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        List<Integrante> integrantes = todosOsTimes.stream()
                .filter(time -> dentroDoPeriodo(time, dataInicial, dataFinal))
                .flatMap(time -> time.getComposicaoTime().stream())
                .map(ComposicaoTime::getIntegrante).collect(Collectors.toList());

        long idMaisUsado = integrantes.stream()
                .collect(Collectors.groupingBy(Integrante::getId, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Nenhum integrante encontrado no período informado"));

        return integrantes.stream()
                .filter(integrante -> integrante.getId() == idMaisUsado)
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Integrante não encontrado"));
    }

    /**
     * Vai retornar uma lista com os nomes dos integrantes do time mais recorrente dentro do período.
     * OBS: Time é o clube + composição em determinada data
     */
    public List<String> integrantesDoTimeMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        List<Time> timesNoPeriodo = todosOsTimes.stream()
                .filter(time -> dentroDoPeriodo(time, dataInicial, dataFinal))
                .collect(Collectors.toList());

        String clubeMaisRecorrente = timesNoPeriodo.stream()
                .collect(Collectors.groupingBy(Time::getNomeDoClube, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum time encontrado no período informado"));

        return timesNoPeriodo.stream()
                .filter(time -> time.getNomeDoClube().equals(clubeMaisRecorrente))
                .flatMap(time -> time.getComposicaoTime().stream())
                .map(composicao -> composicao.getIntegrante().getNome())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Vai retornar a função mais recorrente nos times dentro do período
     */
    public String funcaoMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        return null;
    }

    /**
     * Vai retornar o nome do Clube mais comum dentro do período
     */
    public String clubeMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        // TODO Implementar método seguindo as instruções!
        return null;
    }


    /**
     * Vai retornar o número (quantidade) de aparições de cada Clube participante no período
     */
    public Map<String, Long> contagemDeClubesNoPeriodo(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        return null;
    }

    /**
     * Vai retornar o número (quantidade) de Funções dentro do período.
     * Dica - pense sobre repetições!
     */
    public Map<String, Long> contagemPorFuncao(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        return null;
    }


    private boolean dentroDoPeriodo(Time t, LocalDate ini, LocalDate fim) {
        return (ini == null || !t.getData().isBefore(ini)) && (fim == null || !t.getData().isAfter(fim));
    }
}
