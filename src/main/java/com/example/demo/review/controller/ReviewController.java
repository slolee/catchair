package com.example.demo.review.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.helper.JwtHelper;
import com.example.demo.common.type.MemberType;
import com.example.demo.review.dto.ReviewResponse;
import com.example.demo.review.dto.WriteReviewRequest;
import com.example.demo.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

/**
 *  사용자가 리뷰에 대해서 어떤 행동들을 할 수 있을까? (사용자 시나리오)
 *   1. 사용자는 맛집 상세페이지에 들어가서 리뷰 목록을 확인할 수 있다.
 *   2. 사용자는 자기가 작성한 리뷰를 삭제할 수 있다.
 *   3. 사용자는 자신이 예약한 목록에서 기간이 "지난 예약"에 대해서 리뷰를 작성할 수 있어야한다.
 *        => 예약한 목록 조회 API
 *        => 리뷰가 이미 작성되어 있는 케이스, 작성 가능한 케이스, 아직 작성 불가능한 케이스
 *   4. 사용자는 지도에서 맛집의 별점 평점을 확인할 수 있다. (리뷰에 의해서 만들어지는 데이터)
 *
 *   -----------------------------------------------------------------------------------------
 *   이런거를 개발하기에 앞서 7단계.
 *    -> API 설계 (버튼 모양 정하기)
 *    -> 이런 요청들을 처리하기 위해서 내가 저장해야할 데이터는 뭐지? -> 데이터 모델링
 *
 *   -----------------------------------------------------------------------------------------
 *   [ Review 데이터 모델링 ]
 *    1) review_id (PK)
 *    2) reservation_id  -> "누가, 언제, 어디를, 몇명" 이 정보가 여기 포함되어 있어서..
 *    3) contents
 *    4) star_count
 */

/**
 *  Swagger
 *   - API 문서를 쉽게 만들어주는 도구다.
 *   - API 문서는 뭔데?
 *   	1) 자판기의 사용설명서. 어떤 버튼을 누르면 어떤일이 일어나. 어떤 버튼이 어떻게 생겼어.
 *   - API 문서를 왜 만들어야하는데?
 *   	1) FE 개발자와 API 스펙(버튼의 모양)에 대해서 이야기할 때 참고할 수 있는 도구가 됩니다.
 *      2) FE 개발자랑 기획자가 실제로 한번 테스트해보고싶다.
 *   - 제가 지금 시점에서 이걸 하는거는
 *      1. 어디 제출하기위한 산출물 목적으로 많이 사용된다.
 */

@RestController
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;
	private final JwtHelper jwtHelper;

	// API 1. 사용자는 맛집 상세페이지에 들어가서 리뷰 목록을 확인할 수 있다.
	//  -> 각 리뷰의 (작성자 nickname, 별점, 리뷰 내용, 방문일자, 예약인원)
	@GetMapping("/reviews/restaurant/{id}")
	public List<ReviewResponse> retrieveAllReviewByRestaurant(@PathVariable(name = "id") long restaurantId) {
		System.out.println("restaurantId = " + restaurantId);
		return reviewService.retrieveAll(restaurantId);
	}

	// API 3. 사용자는 자신이 예약한 목록에서 기간이 "지난 예약"에 대해서 리뷰를 작성할 수 있어야한다.
	// Why? 내가 생각한 사용자 스토리는 ~~~ 이거야. 그래서 이런 기능이 필요해.
	// 일단 기능을 제공하고 보는게 아니라 필요하면 만드는거
	@PostMapping("/review")  // 어떤 예약인지, 내용이 뭔지, 별점은 몇개줄건지..
	public void writeReview(@RequestBody WriteReviewRequest req) {
		System.out.println("req = " + req);
		reviewService.write(req);
	}

	@DeleteMapping("/review/{id}")
	public void deleteReview(
		@PathVariable(name = "id") long reviewId,
		@RequestHeader(name = "Authorization") String authorization
	) {
		// 처음으로 컨트롤러 코드가 조금 길어졌는데, 얘네들은 왜 여기있을까?
		// 해당 기능을 사용할 수 있는 사용자인지 판단.
		// 사용자가 준 정보를 가공하는 작업도 여기.
		//   -> 사실은 얘네는 반복되는 코드다. 그래서 얘네는 AOP 방식으로 변경할 수 있다. (인터셉터, 필터)
		String accessToken = authorization.substring("Bearer ".length());
		MemberType type = MemberType.valueOf(jwtHelper.getClaim(accessToken, "MEMBER_TYPE"));
		if (!type.equals(MemberType.GENERAL)) {
			throw new RuntimeException("일반 사용자만 이용가능한 기능입니다.");
		}
		long memberId = Long.parseLong(jwtHelper.getClaim(accessToken, "MEMBER_ID"));
		reviewService.delete(memberId, reviewId);
	}

}
