package com.example.demo.businessmember.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class BusinessMemberLoginRequest {

	final private String email;
	final private String password;

}
