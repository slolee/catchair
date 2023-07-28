package com.example.demo.review.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class WriteReviewRequest {

	private long reservationId;
	private String contents;
	private int starCount;

}
