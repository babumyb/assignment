package com.example.DTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeedDataDTO {

	private Long gameId;
	private Date moveStartTime;
	private Boolean isPlayerTurn;
}
