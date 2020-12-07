package com.example.domain;

import com.example.enums.GameStatus;
import com.example.enums.GameType;
import com.example.enums.Piece;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "second_player_id", nullable = true)
	private Player secondPlayer;

	@ManyToOne
	@JoinColumn(name = "first_player_id", nullable = false)
	private Player firstPlayer;

	@Enumerated(EnumType.STRING)
	private Piece firstPlayerPieceCode;

	@Enumerated(EnumType.STRING)
	private GameType gameType;

	@Enumerated(EnumType.STRING)
	private GameStatus gameStatus;

	@Column(name = "created", nullable = false)
	private Date created;

	@Column(name = "winnerScore", nullable = true)
	private Long winnerScore;

	@Column(name = "started", nullable = true)
	private Date started;
}
