package com.example.demo.businessmember.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.businessmember.dto.BusinessMemberLoginRequest;
import com.example.demo.businessmember.dto.BusinessMemberLoginResponse;
import com.example.demo.businessmember.dto.BusinessMemberRegisterRequest;
import com.example.demo.businessmember.dto.BusinessMemberResponse;
import com.example.demo.businessmember.service.BusinessMemberService;

import lombok.RequiredArgsConstructor;

@RestController   // @Controller
public class BusinessMemberController {

	// DI (의존성 주입) -> 3가지 방법 (필드주입, 생성자주입, Setter주입)
	// @Autowired 를 붙인건 아마도 필드주입
	// 안붙인건 생성자주입
	private final BusinessMemberService businessMemberService;

	public BusinessMemberController(BusinessMemberService businessMemberService) {
		this.businessMemberService = businessMemberService;
	}

	// ** REST API
	// ~~Mapping
	// ~~ : Get(조회), Post(추가), Put(수정), Delete(삭제)
	@PostMapping("/business-member/signup")
	public void register(@RequestBody BusinessMemberRegisterRequest req) {
		System.out.println(req); // 사용자로부터 정보가 제대로 전달됐는지 확인하는 과정
		businessMemberService.register(req);
	}

	// 원래 로그인은 GET 이 맞다.
	// 근데 POST 로 하는 이유는, 로그인할 때 전달해야하는 정보가 민감하기 때문에,
	//  -> GET Request Body 를 사용하지않고..
	//  -> POST 를 사용해서 민감한 정보를 Request Body 에 넣으려 한다.
	@PostMapping("/business-member/login")
	public BusinessMemberLoginResponse login(@RequestBody BusinessMemberLoginRequest req) {
		System.out.println(req);
		return businessMemberService.login(req);
	}

	// 이 API 가 호출되는 곳이.. 사업자 마이페이지에 들어갈때.
	@GetMapping("/business-member/{id}")
	public BusinessMemberResponse retrieveBusinessMemberInfo(@PathVariable(name = "id") long businessMemberId) {
		System.out.println(businessMemberId);
		return businessMemberService.retrieve(businessMemberId);
	}

}

/**
 *  로그아웃 :
 *  	사용자한테 발급한 팔찌(AccessToken) 만 FE 에서 지워주면.. 사실 이게 로그아웃입니다.
 *  	그 AccessToken 을 다시 사용하지 못하게 만들 순 있다. (안정성)
 */