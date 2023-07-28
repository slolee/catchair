package com.example.demo.restaurant.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.demo.businessmember.entity.BusinessMember;
import com.example.demo.common.helper.DateTimeHelper;
import com.example.demo.reservation.entity.Reservation;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@NoArgsConstructor
public class Restaurant {

	// PK
	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long restaurantId;

	@Getter
	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "business_member_id")
	private BusinessMember businessMember; // 연관관계

	@Getter
	private String name;

	@Getter
	private String address;

	@Getter
	private String contents;

	@Getter
	private String phoneNumber;
	// 영업일 같은 경우에는 요일로 표현할건데,
	// MONDAY,FRIDAY,SATURDAY

	private String businessDays;

	// 너가 예약받고 싶은 시간까지 등록해줘..
	// -> 여는시간, 닫는시간만 받아서 무조건 한시간 단위로 예약을 받도록 할거에요.
	@Getter
	private LocalTime openingTime;

	@Getter
	private LocalTime closingTime;

	@Getter
	private int reservationCapacity;

	// 메소드는 내가 제공해주는 기능.. 보다는 사용하는사람이 편해야하는 기능.. (협력적인 객체를 만들자)
	public List<DayOfWeek> getBusinessDays() {
		return Arrays.stream(this.businessDays.split(",")) // "MONDAY"
			.map(DayOfWeek::valueOf)
			.collect(Collectors.toList());
	}

	public List<LocalTime> findAllAvailableReservationTime(List<Reservation> alreadyReservations, int reservationCount) {
		// 0. 데이터 가공을 할건데.. => 언제, 몇명
		Map<LocalTime, Integer> alreadyReservationCount = new HashMap<>();
		// for (Reservation reservation : alreadyReservations) {
		// 	var count = alreadyReservationCount.getOrDefault(reservation.getReservationAt(), 0);
		// 	alreadyReservationCount.put(reservation.getReservationAt(), count + reservation.getCount());
		// }

		alreadyReservations.stream()
			// 언제를 기준으로 쭉 모은거죠
			.collect(Collectors.groupingBy(Reservation::getTime))
			.forEach((at, reservations) ->
				// 그 모은걸 기준으로 하나씩 더해서 모으는거다
				// 13:00 에 2명, 3명, 4명 예약했네..? 그럼 이걸 더하면 9명이구나
				// 그러면 13:00 시에 9명 예약했어라고 PUT 하는거에요
				// 14:00 에는 1명 예약했네..? 이걸 더하면 1명이구나
				// 그러면 14:00 시에 1명 예약했어라고 PUT 하는거에요
				// 17:00 3명, 2명 예약했네..?
				// 그러면 17:00 시에 5명 예약했어라고 PUT 하는겁니다.
				alreadyReservationCount.put(at, reservations.stream().mapToInt(Reservation::getCount).sum())
			);
		// Map<LocalDateTime, Integer> alreadyReservationCount = new HashMap<>();
		// 최종적으로 여기에는
		// {"13:00": 9, "14:00": 1, "17:00": 3}
		// 이렇게 생긴 데이터가 들어가있는거다.

		// 1. 예약을 받을수 있는 전체 시간 목록을 구할겁니다.
		//   -> opening_at, closing_at => [12:00, 13:00, 14:00, 15:00] (유틸성 기능)
		return DateTimeHelper.generateLocalTimeInterval(this.openingTime, this.closingTime).stream()
		// 2. 이미 예약이 완료된 시간을 제외할거에요.
			// 총 예약가능한 사람수 : 5명
			// 이미 예약한 사람 : 3명
			// 내가 예약하려고 하는사람 : 1명
			.filter(time -> this.reservationCapacity - alreadyReservationCount.getOrDefault(time, 0) >= reservationCount)
			// 3. 나머지 시간을 반환할겁니다.
			.collect(Collectors.toList());
	}

	public Restaurant(String name, String address, String contents, String phoneNumber,
		String businessDays, LocalTime openingTime, LocalTime closingTime, int reservationCapacity) {
		this.name = name;
		this.address = address;
		this.contents = contents;
		this.phoneNumber = phoneNumber;
		this.businessDays = businessDays;
		this.openingTime = openingTime;
		this.closingTime = closingTime;
		this.reservationCapacity = reservationCapacity;
	}

}
