package com.example.demo.businessmember.dto;

import com.example.demo.businessmember.entity.BusinessMember;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BusinessMemberResponse {

	// API 의 응답으로 어떤 정보를 내려줄지는 "BE 개발자" 혼자 정하는게 아닙니다.
	// 이거는 기획자, FE 개발자와 의견을 조율할 필요가 있어요.
	final private long businessMemberId;
	final private String businessNumber;
	final private String name;
	final private String phoneNumber;

	public static BusinessMemberResponse from(BusinessMember businessMember) {
		return new BusinessMemberResponse(
			businessMember.getBusinessMemberId(),
			businessMember.getBusinessNumber(),
			businessMember.getName(),
			businessMember.getPhoneNumber()
		);
	}
}
