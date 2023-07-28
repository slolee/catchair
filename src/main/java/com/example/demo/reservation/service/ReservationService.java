package com.example.demo.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.reservation.dto.ReservationCommand;
import com.example.demo.reservation.dto.ReservationResponse;
import com.example.demo.reservation.entity.Reservation;
import com.example.demo.reservation.repository.ReservationRepository;
import com.example.demo.restaurant.entity.Restaurant;
import com.example.demo.restaurant.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final MemberRepository memberRepository;
	private final RestaurantRepository restaurantRepository;

	@Transactional
 	public List<ReservationResponse> retrieveAll(long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow();
		return reservationRepository.findAllByMember(member).stream()
			.map(ReservationResponse::from)
			.collect(Collectors.toList());
	}

 	public List<Reservation> retrieveAll(Restaurant restaurant, LocalDate date) {
		return reservationRepository.findAllByRestaurantAndDate(restaurant, date);
	}

	// ReservationService 를 재사용하기 편하라고,,
	@Transactional
	public void reservation(ReservationCommand command) {
		// 1. command 객체를 Entity 로 변환한다.
		Member member = memberRepository.findById(command.getMemberId()).orElseThrow();
		Restaurant restaurant = restaurantRepository.findById(command.getRestaurantId()).orElseThrow();
		Reservation reservation = new Reservation(member, restaurant, command.getReservationAt(), command.getCount());

		// 2. 저장한다.
		reservationRepository.save(reservation);
	}

	@Transactional
	public void cancel(long reservationId) {
		// reservationRepository.deleteById(reservationId); // Hard Delete
		reservationRepository.findById(reservationId)
			.ifPresent(Reservation::cancel);
	}
}
