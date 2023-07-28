package com.example.demo.restaurant.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.restaurant.dto.RegisterRestaurantRequest;
import com.example.demo.restaurant.dto.RestaurantDetailResponse;
import com.example.demo.restaurant.dto.RestaurantResponse;
import com.example.demo.restaurant.service.RestaurantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestaurantController {

	private final RestaurantService restaurantService;

	/**
	 *  제가 무슨 API 만들거다. 갑자기 이렇게 들고온게..
	 *  원래는 어떤식으로 시작하냐면..
	 *   1. 내가 개발해야할 백로그(사용자스토리, 유스케이스, 요구사항)를 본다.
	 *   2. 그 화면 디자인도 본다.
	 *   3. 기획도 같이 본다.
	 *   4. 이 화면을 표현하기 위한 데이터는 뭐가 필요하겠네..! 라는 생각을 하게 된다.
	 *   5. 그 데이터를 내려주기 위한 API 를 만드는거에요.
	 *   6. 아니면, 그 화면에서 뭔가 제공해줘야할 사용자 기능이 있네..? 라는 생각을 하게되면,
	 *   7. 그 기능에 대한 API 를 만드는거에요.
	 */
	// API : 전체 맛집 목록을 조회하는 API 를 만들거다.
	//   -> 지도에 맛집들을 표현해줘야겠네..!
	//  Controller 에 들어오는 API(버튼) 은 혼자 결정하는게 아니에요. => "API 스펙을 논의한다"
	//  결국 사용자와 맞닿아있는 부분 -> 그러면 이 버튼에 대한 결정은 사용자와 함께하는거에요.
	@GetMapping("/restaurants")
	public List<RestaurantResponse> retrieveAllRestaurants() {
		return restaurantService.retrieveAll();
	}

	// API : 사용자는 Restaurant 의 상세정보를 확인할 수 있다.
	@GetMapping("/restaurant/{id}") // 이런 방식의 데이터 전달 방식 => Path Variable
	public RestaurantDetailResponse retrieveRestaurantDetail(@PathVariable(name = "id") long restaurantId) {
		System.out.println("restaurantId = " + restaurantId);
		return restaurantService.retrieveDetail(restaurantId);
	}

	@GetMapping("/restaurants/business-member/{id}")
	public List<RestaurantResponse> retrieveRestaurantByBusinessMember(
		@PathVariable(name = "id") long businessMemberId
	) {
		System.out.println("businessMemberId = " + businessMemberId);
		return restaurantService.retrieveAll(businessMemberId);
	}

	// 이런 류의 API 들을 CRUD API 하거든요.
	// 맛집 등록을 수정해야해요. (정보 추가)
	//  -> 영업일(business_days), 영업시간(opening_at, closing_at), 시간당 예약가능 인원(reservation_capacity)
	@PostMapping("/restaurant")
	public void registerRestaurant(@RequestBody RegisterRestaurantRequest req) {
		System.out.println(req);
		restaurantService.register(req);
	}

	// DELETE /restaurant/1
	// DELETE /restaurant/2
	// DELETE /restaurant/3
	//  => REST API 다 하면서 많이 쓰기 시작한 형태다
	@DeleteMapping("/restaurant/{id}")
	public void deleteRestaurant(@PathVariable(name = "id") long restaurantId) {
		System.out.println("restaurantId = " + restaurantId);
		restaurantService.delete(restaurantId);
	}

}
