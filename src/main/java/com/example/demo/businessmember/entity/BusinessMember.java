package com.example.demo.businessmember.entity;

// Entity : 데이터베이스 테이블과 1:1 로 매칭되는 클래스(객체)
// 데이터베이스에 저장된 데이터는 테이블에 들어있기 때문에 객체지향 프로그래밍에서 쓸수가 없다.
// 그래서 결국 조회해서 객체로 만들어줘야 한다.
// 근데 이 변환과정은 사실 지금 몰라도된다. -> 왜냐하면 JPA 알아서 해준다.

import java.util.Objects;

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
public class BusinessMember {

	// PK
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long businessMemberId;

	private String email;
	private String password;
	private String businessNumber;
	private String name;
	private String phoneNumber;

	public BusinessMember(String email, String password, String businessNumber, String name, String phoneNumber) {
		this.email = email;
		this.password = password;
		this.businessNumber = businessNumber;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}

	public boolean validPassword(String password) {
		return Objects.equals(this.password, password);
	}

}
