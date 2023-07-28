package com.example.demo.reservation.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class ReservationRequest {

	private long memberId;
	private LocalDateTime reservationAt;
	private int count;

}
