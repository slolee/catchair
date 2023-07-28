package com.example.demo.reservation.entity;

// 우리 뭔가 개발하려고할때 7가지 단계 거쳐야한다.
// 5번인가? 거기에 해당 기능에서 필요한 데이터가 뭔지 작성한다.
// 여기서 나온 데이터를 기반으로 Entity(DB Table) 을 만드는 거에요.

// 1. 사용자는 Restaurant 의 상세정보를 확인할 수 있다.
//   1.5. 인원 선택하고.. 예약하기 버튼 누르기..!
// 2. 사용자는 달력의 특정 날짜를 선택해 해당 Restaurant 의 예약가능한 시간을 확인할 수 있다.
// 3. 사용자는 Restaurant 을 예약할 수 있다.
// 4. 사용자는 본인의 Restaurant 예약 목록을 확인할 수 있다.
// 5. 사용자는 Restaurant 예약을 취소할 수 있다.

// -> 이 과정을 꼭 거쳐야합니다.
// -> BE 개발자가 정하는게 아니라 사실은 기획단계입니다.
// -> 우리가 그 기획단계에 참여하는 개념이에요.
// -> 기획단계는 기획자가 하는거아니야? 이렇게 생각할수도 있지만..
// -> 기획단계에서 데이터를 보는거에요. 그리고 어떻게 구현해야할지도 한번 고민해보는거에요. (7단계 거치면서)
// -> 그 과정에서 아 이거 안돼... 이런 것도 나와요. 얼마걸리겠네. 이런것도 나오죠.

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.hibernate.annotations.Where;

import com.example.demo.member.entity.Member;
import com.example.demo.restaurant.entity.Restaurant;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity  // 이거는 해당 클래스가 Entity 다! 라는 표시
// 이 밑에 3개의 어노테이션은 "Lombok"
// "Lombok" 은 쉽게말해서 반복되는 코드 안적어도 알아서 만들어줄게.
@Getter
@ToString
@Where(clause = "is_canceled = false") // Reservation 을 조회하는 모든 쿼리에 Default 값으로 취소된거 제외하는 Where 문 추가
@NoArgsConstructor
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long reservationId; // PK

	@ManyToOne(fetch = FetchType.LAZY) // JPA 지연로딩에 대한 이야기인데..
	@JoinColumn(name = "member_id")
	private Member member; // 누가

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	private Restaurant restaurant; // 어디를

	// 2023-07-23
	private LocalDate date;
	// 15:00
	private LocalTime time;

	private int count; // 몇명
	private boolean isCanceled; // true (Soft Delete 라고 하는데)

	public Reservation(Member member, Restaurant restaurant, LocalDateTime datetime, int count) {
		this.member = member;
		this.restaurant = restaurant;
		this.date = datetime.toLocalDate();
		this.time = datetime.toLocalTime();
		this.count = count;
		this.isCanceled = false;
	}

	public void cancel() {
		this.isCanceled = true;
	}

	public LocalDateTime getDateTime() {
		return LocalDateTime.of(date, time);
	}

}
