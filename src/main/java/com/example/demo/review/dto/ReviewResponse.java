package com.example.demo.review.dto;

import java.time.LocalDateTime;

import com.example.demo.review.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewResponse {

	final private long reviewId;
	final private long writerId;
	final private String writerNickname;
	final private LocalDateTime reservationDateTime;
	final private int reservationCount;
	final private int starCount;
	final private String reviewContents;

	public static ReviewResponse from(Review review) {
		return new ReviewResponse(
			review.getReviewId(),
			review.getReservation().getMember().getMemberId(),
			review.getReservation().getMember().getNickname(),
			review.getReservation().getDateTime(),
			review.getReservation().getCount(),
			review.getStarCount(),
			review.getContents()
		);
	}
}
