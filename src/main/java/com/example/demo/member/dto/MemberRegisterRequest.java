package com.example.demo.member.dto;

import com.example.demo.kakao.client.response.KakaoUserInfoResponse;
import com.example.demo.member.entity.Member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class MemberRegisterRequest {

	final private String providerName;
	final private long accountId;
	final private String nickname;
	final private String profileImage;

	public static MemberRegisterRequest from(KakaoUserInfoResponse resp) {
		return new MemberRegisterRequest(
			"KAKAO",
			resp.getId(),
			resp.getProperties().getNickname(),
			resp.getProperties().getProfileImage()
		);
	}

	public Member toEntity() {
		return new Member(this.providerName, this.accountId, this.nickname, this.profileImage);
	}
}
