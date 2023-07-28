package com.example.demo.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	private String oauthProviderName; // KAKAO, NAVER, GOOGLE ...
	private long oauthAccountId; // 카카오쪽에서 준 내 계정에 대한 PK

	private String nickname;
	private String profileImage;

	public Member(String oauthProviderName, long oauthAccountId, String nickname, String profileImage) {
		this.oauthProviderName = oauthProviderName;
		this.oauthAccountId = oauthAccountId;
		this.nickname = nickname;
		this.profileImage = profileImage;
	}
}
