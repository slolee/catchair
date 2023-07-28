package com.example.demo.kakao.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.kakao.client.KakaoOAuth2Client;
import com.example.demo.kakao.service.KakaoOAuth2Service;
import com.example.demo.member.dto.MemberLoginResponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class KakaoOAuth2Controller {

	private final KakaoOAuth2Service kakaoOAuth2Service;
	private final KakaoOAuth2Client kakaoOAuth2Client;

	// 1. "카카오 로그인" 버튼을 눌렀을 때 카카오 로그인 페이지로 Redirection 시켜주는 API
	@GetMapping("/oauth2/kakao/login")
	public void login(HttpServletResponse response) throws IOException {
		String redirectUrl = kakaoOAuth2Client.generateRedirectUrl();
		response.sendRedirect(redirectUrl);
	}

	// 2. 카카오 로그인에 성공했을 때 "인증코드" 를 받아주는 API
	@GetMapping("/oauth2/kakao/callback")
	public MemberLoginResponse callback(@RequestParam String code) {
		System.out.println(code);
		return kakaoOAuth2Service.login(code);  // 결과가 뭐가올까요? AccessToken 을 줘야한다.
	}

}
