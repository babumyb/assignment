package com.example.controller;

import com.example.DTO.GameDTO;
import com.example.domain.Game;
import com.example.service.GameService;
import com.example.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;



@RestController
@RequestMapping("/game")
public class GameController {

	@Autowired
	GameService gameService;

	@Autowired
	PlayerService playerService;

	Logger logger = LoggerFactory.getLogger(GameController.class);

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Game createNewGame(@RequestBody GameDTO gameDTO) {

		Game game = gameService.createNewGame(playerService.getLoggedUser(), gameDTO);
		return game;
	}

	@RequestMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Game> getGamesToJoin() {
		return gameService.getGamesToJoin(playerService.getLoggedUser());
	}

	@RequestMapping(value = "/join", method = RequestMethod.POST)
	public Game joinGame(@RequestBody GameDTO gameDTO) {
		Game game = gameService.joinGame(playerService.getLoggedUser(), gameDTO);
		return game;
	}


	@RequestMapping(value = "/player/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Game> getPlayerGames() {
		return gameService.getPlayerGames(playerService.getLoggedUser());
	}

	@RequestMapping(value = "/{id}")
	public Game getGameProperties(@PathVariable Long id) {
		return gameService.getGame(id);
	}



}
