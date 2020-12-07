package com.example.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.DTO.CreateMoveDTO;
import com.example.DTO.MoveDTO;
import com.example.domain.Game;
import com.example.domain.Move;
import com.example.domain.Player;
import com.example.domain.Position;
import com.example.enums.GameStatus;
import com.example.service.GameService;
import com.example.service.MoveService;
import com.example.service.PlayerService;

@RestController
@RequestMapping("/game/{gameId}/move")
public class MoveController {

	@Autowired
	private MoveService moveService;

	@Autowired
	private PlayerService playerService;

	@Autowired
	private GameService gameService;

	Logger logger = LoggerFactory.getLogger(MoveController.class);

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Move createMove(@PathVariable Long gameId, @RequestBody CreateMoveDTO createMoveDTO) {
		logger.info("move to insert:" + createMoveDTO.getBoardColumn() + createMoveDTO.getBoardRow());

		Move move = moveService.createMove(gameService.getGame(gameId), playerService.getLoggedUser(), createMoveDTO);
		Game game = gameService.getGame(gameId);
		GameStatus gameStatus = moveService.checkCurrentGameStatus(game);
		gameService.updateGameStatus(gameService.getGame(gameId), gameStatus);
		Player player = playerService.getLoggedUser();
		gameService.updateScore(game, moveService.getPlayerMoves(game, player), player, false);
		return move;
	}

	@RequestMapping(value = "/autocreate", method = RequestMethod.GET)
	public Move autoCreateMove(@PathVariable Long gameId) {

		logger.info("AUTO move to insert:");

		Move move = moveService.autoCreateMove(gameService.getGame(gameId));

		Game game = gameService.getGame(gameId);
		GameStatus gameStatus = moveService.checkCurrentGameStatus(game);
		gameService.updateGameStatus(gameService.getGame(gameId), gameStatus);
		if (gameStatus.equals(GameStatus.SECOND_PLAYER_WON)) {
			gameService.updateScore(game, null, null, true);
		}
		return move;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<MoveDTO> getMovesInGame(@PathVariable Long gameId) {

		return moveService.getMovesInGame(gameService.getGame(gameId));
	}

	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public List<Position> validateMoves(@PathVariable Long gameId) {
		return moveService.getPlayerMovePositionsInGame(gameService.getGame(gameId), playerService.getLoggedUser());
	}

	@RequestMapping(value = "/turn", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public boolean isPlayerTurn(@PathVariable Long gameId) {
		Player player = playerService.getLoggedUser();
		return moveService.isPlayerTurn(gameService.getGame(gameId), gameService.getGame(gameId).getFirstPlayer(),
				gameService.getGame(gameId).getSecondPlayer(), player);
	}

}
