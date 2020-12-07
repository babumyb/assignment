package com.example.service;

import com.example.domain.Player;
import com.example.r2repository.TimerRepository;
import reactor.core.publisher.Flux;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class TimerService {

    private final TimerRepository timerRepository;


    @Autowired
    public TimerService(TimerRepository timerRepository) {
        this.timerRepository = timerRepository;
    }
    
    Flux<Player> getAllGames(){
    	return timerRepository.findAll();
    }
}
