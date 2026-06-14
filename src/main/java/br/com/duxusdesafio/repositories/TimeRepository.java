package br.com.duxusdesafio.repositories;

import br.com.duxusdesafio.model.Time;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeRepository extends JpaRepository<Time, Long> {
}
