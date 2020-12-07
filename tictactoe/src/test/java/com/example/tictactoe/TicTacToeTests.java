package com.example.tictactoe;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.json.JSONException;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import com.example.TicTacToeGameApplication;
import com.example.DTO.CreateMoveDTO;
import com.example.DTO.GameDTO;
import com.example.DTO.PlayerDTO;
import com.example.domain.Game;
import com.example.domain.Move;
import com.example.domain.Player;
import com.example.enums.GameStatus;
import com.example.enums.GameType;
import com.example.enums.Piece;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TicTacToeGameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@TestPropertySource(locations = "/application-test.properties")
public class TicTacToeTests {

	@LocalServerPort
	private int port;

	@Autowired
	TestRestTemplate testRestTemplate;

	@Value("${username1}")
	String userName1;
	@Value("${username2}")
	String userName2;
	@Value("${player_create_uri}")
	String createPlayerURI;
	@Value("${game_create_uri}")
	String createGameURI;
	@Value("${game_join_uri}")
	String joinGameURI;
	@Value("${move_1_turn}")
	String checkMoveTurn;
	@Value("${move_1_create}")
	String createMove;

	@Test
	public void testaCreatePlayer() throws JSONException {
		ResponseEntity<Player> response = null;

		PlayerDTO player = new PlayerDTO(this.userName1, this.userName1, "abc@xyz.com");
		PlayerDTO player2 = new PlayerDTO(this.userName2, this.userName2, "xyz@xyz.com");
		HttpEntity<PlayerDTO> entity = null;

		entity = new HttpEntity<PlayerDTO>(player, null);
		response = testRestTemplate.exchange(createURLWithPort(this.createPlayerURI), HttpMethod.POST, entity,
				Player.class);

		entity = new HttpEntity<PlayerDTO>(player2, null);
		response = testRestTemplate.exchange(createURLWithPort(this.createPlayerURI), HttpMethod.POST, entity,
				Player.class);
		assertEquals(this.userName2, response.getBody().getUserName());
		assertNotEquals(this.userName2, response.getBody().getPassword());
	}

	@Test
	public void testbCreateGameAndJoin() {
		GameDTO gameDTO = null;
		HttpEntity<GameDTO> entity = null;
		ResponseEntity<Game> response = null;

		gameDTO = new GameDTO(0, GameType.COMPETITION, Piece.X);
		entity = new HttpEntity<GameDTO>(gameDTO, null);
		response = getRestTemplateWithFirstPlayerAuth().exchange(createURLWithPort(this.createGameURI), HttpMethod.POST,
				entity, Game.class);
		assertNotEquals(response.getBody().getId(), new Long(0));
		assertEquals(GameStatus.WAITS_FOR_PLAYER, response.getBody().getGameStatus());

		gameDTO.setId(response.getBody().getId().intValue());
		entity = new HttpEntity<GameDTO>(gameDTO, null);
		response = getRestTemplateWithSecondPlayerAuth().exchange(createURLWithPort(this.joinGameURI), HttpMethod.POST,
				entity, Game.class);
		assertEquals(GameStatus.IN_PROGRESS, response.getBody().getGameStatus());

	}

	@Test
	public void testcCreateMoves() {

		HttpEntity<Object> entity = null;
		ResponseEntity<Boolean> response = null;

		// Check first player turn
		entity = new HttpEntity<Object>(null, null);
		response = getRestTemplateWithFirstPlayerAuth().exchange(createURLWithPort(this.checkMoveTurn), HttpMethod.GET,
				entity, Boolean.class);
		assertTrue(response.getBody().booleanValue());

		HttpEntity<CreateMoveDTO> createMoveDTOEntity = null;
		CreateMoveDTO createMoveDTO = null;
		// First player makes move
		ResponseEntity<Move> moveResponse = null;
		createMoveDTO = new CreateMoveDTO(1, 1);
		createMoveDTOEntity = new HttpEntity<CreateMoveDTO>(createMoveDTO, null);
		moveResponse = getRestTemplateWithFirstPlayerAuth().exchange(createURLWithPort(this.createMove),
				HttpMethod.POST, createMoveDTOEntity, Move.class);
		assertEquals(this.userName1, moveResponse.getBody().getPlayer().getUserName());

		// Second player turn
		response = getRestTemplateWithSecondPlayerAuth().exchange(createURLWithPort(this.checkMoveTurn), HttpMethod.GET,
				entity, Boolean.class);
		assertTrue(response.getBody().booleanValue());

		// Second player makes move
		createMoveDTO = new CreateMoveDTO(2, 1);
		createMoveDTOEntity = new HttpEntity<CreateMoveDTO>(createMoveDTO, null);
		moveResponse = getRestTemplateWithSecondPlayerAuth().exchange(createURLWithPort(this.createMove),
				HttpMethod.POST, createMoveDTOEntity, Move.class);
		assertNotEquals(Piece.O, moveResponse.getBody().getGame().getFirstPlayerPieceCode());

		// First player makes move
		createMoveDTO = new CreateMoveDTO(1, 2);
		createMoveDTOEntity = new HttpEntity<CreateMoveDTO>(createMoveDTO, null);
		moveResponse = getRestTemplateWithFirstPlayerAuth().exchange(createURLWithPort(this.createMove),
				HttpMethod.POST, createMoveDTOEntity, Move.class);

		// Second player makes move
		createMoveDTO = new CreateMoveDTO(2, 2);
		createMoveDTOEntity = new HttpEntity<CreateMoveDTO>(createMoveDTO, null);
		moveResponse = getRestTemplateWithSecondPlayerAuth().exchange(createURLWithPort(this.createMove),
				HttpMethod.POST, createMoveDTOEntity, Move.class);
		assertNotEquals(Piece.O, moveResponse.getBody().getGame().getFirstPlayerPieceCode());

		// First player makes move
		createMoveDTO = new CreateMoveDTO(1, 3);
		createMoveDTOEntity = new HttpEntity<CreateMoveDTO>(createMoveDTO, null);
		moveResponse = getRestTemplateWithFirstPlayerAuth().exchange(createURLWithPort(this.createMove),
				HttpMethod.POST, createMoveDTOEntity, Move.class);

		assertEquals(GameStatus.FIRST_PLAYER_WON, moveResponse.getBody().getGame().getGameStatus());
		assertTrue(moveResponse.getBody().getGame().getWinnerScore() > 0);

	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}

	private TestRestTemplate getRestTemplateWithFirstPlayerAuth() {
		return testRestTemplate.withBasicAuth(this.userName1, this.userName1);
	}

	private TestRestTemplate getRestTemplateWithSecondPlayerAuth() {
		return testRestTemplate.withBasicAuth(this.userName2, this.userName2);
	}
}