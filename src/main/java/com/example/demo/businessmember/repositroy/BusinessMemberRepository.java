package com.example.demo.businessmember.repositroy;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.businessmember.entity.BusinessMember;

@Repository
public interface BusinessMemberRepository extends JpaRepository<BusinessMember, Long> {

	Optional<BusinessMember> findByEmail(String email);
	// SELECT * FROM BusinessMember WHERE email = ?;

	// Repository 는 일을 시킬때 어떻게 처리하는지는 안적어줘도 된다.
	//  Q. 어떻게 코드도 안짰는데 데이터베이스에서 데이터를 조회해오지..?
	//  A. JPA(Spring Data JPA) 가 그 몸통을 알아서 만들어준다.
	//  Q. 내가 원하는게 뭔줄알고 걔가 만들어주냐.. 내가 해야지..?
	//  A. 내가 만든 메소드의 이름, 반환값, 파라미터의 정보를 가지고 알아서 만들어준다.
	//     그래서 이 메소드의 이름에는 규칙이 정해져있다.

}
