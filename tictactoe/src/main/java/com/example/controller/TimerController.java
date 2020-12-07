package com.example.controller;

import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.example.domain.Player;
import com.example.r2repository.TimerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

//TBC: Not to be used
@RestController
@RequestMapping("/timer")
public class TimerController {

	Logger logger = LoggerFactory.getLogger(TimerController.class);

	@Autowired
	TimerRepository timerRepo;
	@RequestMapping(value = "/feeder", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Player> timerFeed() {
		Flux<Player> game =timerRepo.findAll();
		return 	Flux.zip(game, Flux.interval(Duration.ofSeconds(1))).map(Tuple2::getT1);
	}


}
