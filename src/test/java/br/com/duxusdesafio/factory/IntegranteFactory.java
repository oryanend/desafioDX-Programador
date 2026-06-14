package br.com.duxusdesafio.factory;

import br.com.duxusdesafio.model.Integrante;
import net.datafaker.Faker;

public class IntegranteFactory {
    private static final Faker faker = new Faker();
    private static int indice = 0;

    private static final String[] funcoesTime = {
            "GOLEIRO",
            "ZAGUEIRO",
            "LATERAL",
            "VOLANTE",
            "MEIA",
            "ATACANTE"
    };

    public static Integrante createIntegrante() {
        Integrante integrante = new Integrante();
        integrante.setNome(faker.name().fullName());
        integrante.setFuncao(funcoesTime[indice++ % funcoesTime.length]);
        return integrante;
    }
}
