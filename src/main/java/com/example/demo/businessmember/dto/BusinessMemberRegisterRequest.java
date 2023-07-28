package com.example.demo.businessmember.dto;

import com.example.demo.businessmember.entity.BusinessMember;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class BusinessMemberRegisterRequest {

	final private String email;
	final private String password;
	final private String businessNumber;
	final private String name;
	final private String phoneNumber;

	public BusinessMember toEntity() {
		return new BusinessMember(email, password, businessNumber, name, phoneNumber);
	}

}
