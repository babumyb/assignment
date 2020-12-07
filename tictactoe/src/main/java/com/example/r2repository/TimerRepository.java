package com.example.r2repository;

import com.example.domain.Player;

import reactor.core.publisher.Flux;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimerRepository extends R2dbcRepository<Player, Long> {
	@Query("select * from Player")
	Flux<Player> findAll();
}
