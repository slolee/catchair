package com.example.demo.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.restaurant.dto.RestaurantResponse;
import com.example.demo.restaurant.entity.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	Optional<Restaurant> findByAddress(String address);

	// JPQL
	// FETCH JOIN -> N + 1 문제를 해결하기 위해 대응법
	@Query("SELECT res FROM Restaurant res JOIN FETCH res.businessMember "
		+ "WHERE res.businessMember.businessMemberId = :businessMemberId")
	List<Restaurant> findAllByBusinessMemberId(@Param("businessMemberId") long businessMemberId);

}
