package com.example.repository;

import com.example.domain.Game;
import com.example.domain.Move;
import com.example.domain.Player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {

	List<Move> findByGame(Game game);

	List<Move> findByGameAndPlayer(Game game, Player player);

	int countByGameAndPlayer(Game game, Player player);
}
