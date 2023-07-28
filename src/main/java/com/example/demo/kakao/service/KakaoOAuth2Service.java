package com.example.demo.kakao.service;

import org.springframework.stereotype.Service;

import com.example.demo.common.helper.JwtHelper;
import com.example.demo.common.type.MemberType;
import com.example.demo.kakao.client.KakaoOAuth2Client;
import com.example.demo.kakao.client.response.KakaoUserInfoResponse;
import com.example.demo.member.dto.MemberLoginResponse;
import com.example.demo.member.dto.MemberRegisterRequest;
import com.example.demo.member.entity.Member;
import com.example.demo.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoOAuth2Service {

	private final KakaoOAuth2Client kakaoOAuth2Client;
	private final MemberService memberService;
	private final JwtHelper jwtHelper;

	// 객체의 책임
	// 객체지향 프로그래밍에서 되게 중요한건
	// 책임을 적절한 객체한테 부여하는 일.
	public MemberLoginResponse login(String code) {
		// 0. KAKAO 에서 사용자정보 조회
		String kakaoAccessToken = kakaoOAuth2Client.getAccessToken(code);
		KakaoUserInfoResponse resp = kakaoOAuth2Client.getUserInfo(kakaoAccessToken);

		// 1. 처음이라면 우리쪽에 회원가입 시켜줘야한다. & 이미 가입되어있으면 패스.
		Member member = memberService.find("KAKAO", resp.getId())
			.orElseGet(() -> memberService.register(MemberRegisterRequest.from(resp)));

		// 2. AccessToken 만들어서 반환
		String accessToken = jwtHelper.generateAccessToken(MemberType.GENERAL, member.getMemberId());
		return new MemberLoginResponse(accessToken);
	}

}
