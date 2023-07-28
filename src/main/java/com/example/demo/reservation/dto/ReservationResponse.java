package com.example.demo.reservation.dto;

import java.time.LocalDateTime;

import com.example.demo.reservation.entity.Reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationResponse {

	// 1번 레스토랑
	final private long reservationId;
	final private String restaurantName;
	final private String restaurantAddress;
	final private String restaurantPhoneNumber;
	final private LocalDateTime reservationAt;
	final private int count;

	public static ReservationResponse from(Reservation reservation) {
		return new ReservationResponse(
			reservation.getReservationId(),
			reservation.getRestaurant().getName(),
			reservation.getRestaurant().getAddress(),
			reservation.getRestaurant().getPhoneNumber(),
			LocalDateTime.of(reservation.getDate(), reservation.getTime()),
			reservation.getCount()
		);
	}
}
