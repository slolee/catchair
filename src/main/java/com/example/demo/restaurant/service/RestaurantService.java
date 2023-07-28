package com.example.demo.restaurant.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.businessmember.entity.BusinessMember;
import com.example.demo.businessmember.repositroy.BusinessMemberRepository;
import com.example.demo.reservation.entity.Reservation;
import com.example.demo.reservation.service.ReservationService;
import com.example.demo.restaurant.dto.RegisterRestaurantRequest;
import com.example.demo.restaurant.dto.RestaurantDetailResponse;
import com.example.demo.restaurant.dto.RestaurantResponse;
import com.example.demo.restaurant.entity.Restaurant;
import com.example.demo.restaurant.repository.RestaurantRepository;
import com.example.demo.review.dto.ReviewResponse;
import com.example.demo.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantService {

	private final ReviewService reviewService;
	private final RestaurantRepository restaurantRepository;
	private final ReservationService reservationService;
	private final BusinessMemberRepository businessMemberRepository;

	// 밑에 메소드랑 이름, 반환 자료형이 똑같네?
	// Q. 근데 왜 에러가 안나지..? 이름이 똑같은 메소드가 두개 있어도 괜찮을까?
	// A. 메소드 오버라이딩
	public List<RestaurantResponse> retrieveAll() {
		return restaurantRepository.findAll().stream()
			.map(restaurant -> {
				double starCount = reviewService.calculateStarCountAverage(restaurant);
				return RestaurantResponse.from(restaurant, starCount);
			})
			.collect(Collectors.toList());
	}

	public List<RestaurantResponse> retrieveAll(long businessMemberId) {
		return restaurantRepository.findAllByBusinessMemberId(businessMemberId).stream()
			.map(restaurant -> {
				double starCount = reviewService.calculateStarCountAverage(restaurant);
				return RestaurantResponse.from(restaurant, starCount);
			})
			.collect(Collectors.toList());
	}

	public RestaurantDetailResponse retrieveDetail(long restaurantId) {
		// 1. 사용자로부터 받은 RestaurantId 로 데이터베이스에서 레스토랑 정보를 조회한다.
		return restaurantRepository.findById(restaurantId) // Optional

		// 2. 조회한 레스토랑 정보를 RestaurantDetailResponse 모양으로 바꿔서 반환한다.
			.map(restaurant -> {
				double starCount = reviewService.calculateStarCountAverage(restaurant);
				List<ReviewResponse> reviewList = reviewService.retrieveAll(restaurant.getRestaurantId());
				return RestaurantDetailResponse.from(restaurant, starCount, reviewList);
			})
			.orElseThrow();
	}

	// 지금까지는 계속 Controller -> Service -> Repository 이 흐름만 봤죠.
	// Controller -> [RestaurantService -> ReservationService] -> Repository
	// 고난이도..!
	@Transactional
	public List<LocalTime> retrieveAllAvailableReservationTime(long restaurantId, LocalDate date, int count) {
		// 1. restaurantId 로 데이터베이스에서 레스토랑 정보 조회한다.
		Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();

		// 2. 해당 레스토랑의 특정날짜 예약정보를 조회해줘.
		List<Reservation> reservationList = reservationService.retrieveAll(restaurant, date);

		// 3. 예약 가능한 시간 리스트를 계산해서 반환한다.
		//  만약 이 복잡한 코드가 여기로 나왔었더라면 더 힘들었겠죠..?
		//  복잡한 코드는 왠만하면 시키(위임)는게 요즘 개발 방식중 하나다. (Domain 기반 설계..?)
		//  Service (Application Service) -> Facade(위임) Service -> Thin(마른) Service
		return restaurant.findAllAvailableReservationTime(reservationList, count);
	}

	@Transactional
	public void register(RegisterRestaurantRequest req) {
		// 똑같은 주소로 2개 등록되면 안될거같은데..?
		Optional<Restaurant> restaurant = restaurantRepository.findByAddress(req.getAddress());
		if (restaurant.isPresent()) {
			throw new RuntimeException("동일 주소에 등록된 맛집이 있습니다.");
		}

		BusinessMember businessMember = businessMemberRepository.findById(req.getBusinessMemberId())
				.orElseThrow(() -> new RuntimeException("가입되어있지 않은 사업자정보입니다."));
		Restaurant createdRestaurant = restaurantRepository.save(req.toEntity());
		createdRestaurant.setBusinessMember(businessMember);
	}

	public void delete(long id) {
		restaurantRepository.deleteById(id);
	}

}
