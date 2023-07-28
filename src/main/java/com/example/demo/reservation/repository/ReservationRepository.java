package com.example.demo.reservation.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.member.entity.Member;
import com.example.demo.reservation.entity.Reservation;
import com.example.demo.restaurant.entity.Restaurant;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findAllByRestaurantAndDate(Restaurant restaurant, LocalDate date);

	List<Reservation> findAllByMember(Member member);
}

// 기획상 사업자 로그인과 일반 사용자 로그인이 나눠져있어야 합니다.

/**
 *        [ 일반 사용자 로그인 ]
 *          - 카카오 로그인 -
 *        [ 사업자로 로그인하기 ] -> 이걸 누르면 사업자 로그인 페이지로 이동
 *
 *
 *        [ 사업자 로그인 ]
 *        Email : [          ]
 *        PW : [          ]
 *        [ 회원가입 ] [ 로그인 ]
 */
