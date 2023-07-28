package com.example.demo.review.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Where;

import com.example.demo.reservation.entity.Reservation;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *   [ Review 데이터 모델링 ]
 *    1) review_id (PK)
 *    2) reservation_id  -> "누가, 언제, 어디를, 몇명" 이 정보가 여기 포함되어 있어서..
 *    3) contents
 *    4) star_count
 */

// 데이터를 전부 만들고 거기서 기능을 뽑아내는게 아니라
// 어떤 기능을 만들어야 하는지 사용자 관점에서 스토리(시나리오)를 뽑아내고
// 그 시나리오를 구현하기 위해 필요한 데이터를 모델링 하는것이 중요하다 -> DDD

@Entity  // 이거는 해당 클래스가 Entity 다! 라는 표시
@Getter
@ToString
@NoArgsConstructor
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reservation_id")
	private Reservation reservation;

	private String contents;
	private int starCount;
	// private LocalDateTime dateTime;

	public Review(Reservation reservation, String contents, int starCount) {
		this.reservation = reservation;
		this.contents = contents;
		this.starCount = starCount;
	}

	public long getMemberId() {
		return this.reservation.getMember().getMemberId();
	}
}
