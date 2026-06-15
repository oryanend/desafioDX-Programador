package br.com.duxusdesafio.factory;

import br.com.duxusdesafio.DTO.TimeDaDataDTO;
import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import net.datafaker.Faker;

import java.util.Arrays;

import static br.com.duxusdesafio.factory.IntegranteFactory.createIntegrante;

@SuppressWarnings("removal")
public class TimeFactory {
    private static final Faker faker = new Faker();

    public static Time createTime() {
        Time time = new Time();
        time.setNomeDoClube(faker.team().name());
        time.setData(faker.date().birthday().toLocalDateTime().toLocalDate());
        return time;
    }

    public static TimeDaDataDTO createTimeDaDataDTO() {
        Time time = createTime();

        Integrante i1 = createIntegrante();
        Integrante i2 = createIntegrante();
        Integrante i3 = createIntegrante();

        ComposicaoTime c1 = new ComposicaoTime();
        c1.setIntegrante(i1);

        ComposicaoTime c2 = new ComposicaoTime();
        c2.setIntegrante(i2);

        ComposicaoTime c3 = new ComposicaoTime();
        c3.setIntegrante(i3);

        time.setComposicaoTime(Arrays.asList(c1, c2, c3));

        return new TimeDaDataDTO(time);
    }
}
