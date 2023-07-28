package com.example.demo.reservation.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReservationCommand {

	private final long memberId;
	private final long restaurantId;
	private final LocalDateTime reservationAt;
	private final int count;

	public ReservationCommand(long restaurantId, ReservationRequest req) {
		this.memberId = req.getMemberId();
		this.restaurantId = restaurantId;
		this.reservationAt = req.getReservationAt();
		this.count = req.getCount();
	}

}
