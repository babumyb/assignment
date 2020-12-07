package com.example.repository;

import com.example.domain.Player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
	Player findOneByUserName(String userName);
}
