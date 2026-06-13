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
    public Integrante integranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        return maisFrequente(integrantesNoPeriodo(todosOsTimes, dataInicial, dataFinal), "Nenhum integrante encontrado no período informado");
    }

    /**
     * Vai retornar uma lista com os nomes dos integrantes do time mais recorrente dentro do período.
     * OBS: Time é o clube + composição em determinada data
     */
    public List<String> integrantesDoTimeMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        List<Time> timesNoPeriodo = filtrarPorPeriodo(todosOsTimes, dataInicial, dataFinal).collect(Collectors.toList());

        String clube = maisFrequente(timesNoPeriodo.stream(), Time::getNomeDoClube, "Nenhum time encontrado no período informado");

        return timesNoPeriodo.stream()
                .filter(t -> t.getNomeDoClube().equals(clube))
                .flatMap(t -> t.getComposicaoTime().stream())
                .map(c -> c.getIntegrante().getNome())
                .distinct().collect(Collectors.toList());
    }

    /**
     * Vai retornar a função mais recorrente nos times dentro do período
     */
    public String funcaoMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        return maisFrequente(integrantesNoPeriodo(todosOsTimes, dataInicial, dataFinal), Integrante::getFuncao, "Nenhuma função encontrada no período informado");
    }

    /**
     * Vai retornar o nome do Clube mais comum dentro do período
     */
    public String clubeMaisRecorrente(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        return maisFrequente(filtrarPorPeriodo(todosOsTimes, dataInicial, dataFinal), Time::getNomeDoClube, "Nenhum clube encontrado no período informado");
    }

    /**
     * Vai retornar o número (quantidade) de aparições de cada Clube participante no período
     */
    public Map<String, Long> contagemDeClubesNoPeriodo(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        return filtrarPorPeriodo(todosOsTimes, dataInicial, dataFinal)
                .collect(Collectors.groupingBy(Time::getNomeDoClube, Collectors.counting()));
    }

    /**
     * Vai retornar o número (quantidade) de Funções dentro do período.
     * Dica - pense sobre repetições!
     */
    public Map<String, Long> contagemPorFuncao(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        return integrantesNoPeriodo(todosOsTimes, dataInicial, dataFinal)
                .distinct().collect(Collectors.groupingBy(Integrante::getFuncao, Collectors.counting()));
    }


    // Métodos Reutilizáveis
    /** Times cuja data está dentro de [dataInicial, dataFinal]; nulos = sem limite. */
    private Stream<Time> filtrarPorPeriodo(List<Time> listaTodosTimes, LocalDate dataInicial, LocalDate dataFinal) {
        return listaTodosTimes.stream()
                .filter(time -> (dataInicial == null || !time.getData().isBefore(dataInicial)) && (dataFinal == null || !time.getData().isAfter(dataFinal)));
    }

    /**
     * Todos os integrantes dos times no período, com repetições.
     */
    private Stream<Integrante> integrantesNoPeriodo(List<Time> listaTodosTimes, LocalDate dataInicial, LocalDate dataFinal) {
        return filtrarPorPeriodo(listaTodosTimes, dataInicial, dataFinal)
                .flatMap(time -> time.getComposicaoTime().stream())
                .map(ComposicaoTime::getIntegrante);
    }

    /** Elemento mais frequente num stream de valores; lança exceção se vazio. */
    private <K> K maisFrequente(Stream<K> stream, String exceptionMessage) {
        return stream
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new ResourceNotFoundException(exceptionMessage));
    }

    /** Variante: extrai uma chave antes de calcular a frequência. */
    private <T, K> K maisFrequente(Stream<T> stream, Function<T, K> key, String exceptionMessage) {
        return maisFrequente(stream.map(key), exceptionMessage);
    }

}
