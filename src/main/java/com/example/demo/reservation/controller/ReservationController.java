package com.example.demo.reservation.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.reservation.dto.ReservationCommand;
import com.example.demo.reservation.dto.ReservationRequest;
import com.example.demo.reservation.dto.ReservationResponse;
import com.example.demo.reservation.service.ReservationService;
import com.example.demo.restaurant.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

// 1. 사용자는 Restaurant 의 상세정보를 확인할 수 있다.
//   1.5. 인원 선택하고.. 예약하기 버튼 누르기..!
//   1.6. 1번이 구현됨으로써 사용자는 달력을 보고 날짜를 선택할 수 있게 됐어요.
//   1.7. 이 달력은 어떤 모양일지 상상해보면은, 해당 가게가 영업하는 날만 활성화되어있으면 좋겠다.
// 2. 사용자는 달력의 특정 날짜를 선택해 해당 Restaurant 의 예약가능한 시간을 확인할 수 있다.
// 3. 사용자는 Restaurant 을 예약할 수 있다.
// 4. 사용자는 본인의 Restaurant 예약 목록을 확인할 수 있다.
// 5. 사용자는 Restaurant 예약을 취소할 수 있다.

@RestController
@RequiredArgsConstructor // Lombok 기능
public class ReservationController {

	// 레스토랑 서비스가 가지고 있는 책임이 너무많아져서 여러 곳으로 분산시킨거다.
	private final RestaurantService restaurantService;
	private final ReservationService reservationService;

	// 전제 : 사용자가 몇명 예약할지를 선택해야할거같아.
	// API : 사용자는 달력의 특정 날짜, 예약 인원을 선택해 해당 Restaurant 의 예약가능한 시간을 확인할 수 있다.
	@Operation(summary = "레스토랑의 현재 이용가능한 예약시간 조회하는 API")
	@GetMapping("/restaurant/{id}/available-reservation") // 사용자로부터 받아야 할 데이터 : 언제(날짜)
	public List<LocalTime> retrieveAvailableForReservation(
		@PathVariable(name = "id") long restaurantId,
		@RequestParam(name = "reservation_date") LocalDate reservationDate,
		@RequestParam(name = "reservation_count") int count
	) {
		System.out.println("restaurantId = " + restaurantId + ", reservationDate = " + reservationDate + ", count = " + count);
		return restaurantService.retrieveAllAvailableReservationTime(restaurantId, reservationDate, count);
		// [12:00, 13:00, 14:00, 15:00, 19:00]
	}

	@Operation(summary = "해당 멤버가 예약한 예약정보 전체 조회 API")
	@GetMapping("/reservations/member/{id}")
	public List<ReservationResponse> retrieveAllReservationByMember(@PathVariable(name = "id") long memberId) {
		System.out.println("memberId = " + memberId);
		return reservationService.retrieveAll(memberId);
	}

	// API : 사용자는 Restaurant 을 예약할 수 있다.
	@PostMapping("/restaurant/{id}/reservation")
	public void reservation(
		@PathVariable(name = "id") long restaurantId,
		@RequestBody ReservationRequest req
	) {
		// restaurantId(어디), req(누가, 언제, 몇명) -> 하나의 객체로 묶어보겠다.
		// 그러면 당연히 이 묶인 객체도 DTO
		ReservationCommand command = new ReservationCommand(restaurantId, req);
		reservationService.reservation(command);
	}

	// 제가 지금 가장 추천하는 공부법
	// 같이 작성한 코드를 옮기거나 읽을때,
	// 분명히 모르는게 생기죠. 그러면 그 부분을 채우기위한 공부를 하는거에요.
	@DeleteMapping("/reservation/{id}")
	public void cancelReservation(@PathVariable(name = "id") long reservationId) {
		reservationService.cancel(reservationId);
	}

	/**
	 *   배포한다는게 무슨의미인지?
	 *    - 내가 개발한 Spring Application 코드를 컴퓨터에서 실행시키겠다는거에요.
	 *    - 다른사람이 이 자판기를 쓸 수 있게 거리에 내놓는 행위
	 *    - 다른 사람이 내가 만든 Spring Application 에 인터넷을 통해서 접근할 수 있도록 만들어주는 것까지.
	 *    - 길거리 : AWS, nCloud 같은데서 길거리에 이미 나와있는 컴퓨터를 빌리는 것.
	 */
}
