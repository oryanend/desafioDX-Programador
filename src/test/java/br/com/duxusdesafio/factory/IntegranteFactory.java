package br.com.duxusdesafio.factory;

import br.com.duxusdesafio.model.Integrante;
import net.datafaker.Faker;

/**
 * Factory responsável por criar instâncias de Integrante para testes.
 */
public class IntegranteFactory {
    private static final Faker faker = new Faker();
    // Alterna entre as funções disponíveis para gerar dados variados
    private static int indice = 0;

    private static final String[] funcoesTime = {
            "GOLEIRO",
            "ZAGUEIRO",
            "LATERAL",
            "VOLANTE",
            "MEIA",
            "ATACANTE"
    };

    /**
     * Cria uma instância de Integrante para testes.
     *
     * @return instância de Integrante
     */
    public static Integrante createIntegrante() {
        Integrante integrante = new Integrante();
        integrante.setNome(faker.name().fullName());
        integrante.setFuncao(funcoesTime[indice++ % funcoesTime.length]);
        return integrante;
    }
}
