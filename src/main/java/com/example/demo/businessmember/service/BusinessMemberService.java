package com.example.demo.businessmember.service;

import java.util.Optional;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import com.example.demo.businessmember.dto.BusinessMemberLoginRequest;
import com.example.demo.businessmember.dto.BusinessMemberLoginResponse;
import com.example.demo.businessmember.dto.BusinessMemberRegisterRequest;
import com.example.demo.businessmember.dto.BusinessMemberResponse;
import com.example.demo.businessmember.entity.BusinessMember;
import com.example.demo.businessmember.repositroy.BusinessMemberRepository;
import com.example.demo.common.helper.JwtHelper;
import com.example.demo.common.type.MemberType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessMemberService {

	private final BusinessMemberRepository businessMemberRepository;
	private final JwtHelper jwtHelper;

	public void register(BusinessMemberRegisterRequest req) {
		// 1. 해당 email 을 조건으로 하는 사업자가 있는지 DB 에서 조회한다. 그리고, 있으면 에러를 발생시킨다.
		Optional<BusinessMember> businessMember = businessMemberRepository.findByEmail(req.getEmail());
		if (businessMember.isPresent()) {
			throw new RuntimeException("이미 가입되어있는 이메일입니다!");
		}

		// 2. 사업자 정보를 DB 에 저장한다.
		//    JPA Repository 는 저장할때 무조건 Entity 여야만한다.
		businessMemberRepository.save(req.toEntity());
	}

	public BusinessMemberLoginResponse login(BusinessMemberLoginRequest req) {
		// 1. 사용자로부터 받은 email 로 데이터베이스에서 사용자정보 조회해
		Optional<BusinessMember> memberOptional = businessMemberRepository.findByEmail(req.getEmail());
		if (memberOptional.isEmpty()) {
			throw new RuntimeException("잘못된 Email 입니다!");
		}

		// 2. 조회된 사용자정보에서 패스워드가 일치하는지 확인해
		// 여기 왔다는거는 이메일로 사용자 정보가 조회됐다는걸 전제로 할 수 있게되는거죠.
		BusinessMember member = memberOptional.get();
		if (!member.validPassword(req.getPassword())) { // 일치하면 true, 일치하지 않으면 false
			throw new RuntimeException("잘못된 Password 입니다!");
		}

		// 여기 왔다는건 EMail 로 사용자도 찾아졌고, 패스워드도 일치한거네..
		// 3. AccessToken 만들어서 응답으로 내려줘야한다.
		//    AccessToken 은 JWT(Json Web Token) 표준을 가지고 토큰을 만들어요.
		String accessToken = jwtHelper.generateAccessToken(MemberType.BUSINESS, member.getBusinessMemberId()); // PK
		return new BusinessMemberLoginResponse(accessToken);
	}

	// Q. 아니 조회된 BusinessMember 그대로 내리면 되는거 아니야?
	//    왜 굳이 BusinessMemberResponse(DTO) 로 바꿔서 내려..?
	// A. 에러가능성 (JPA Proxy Initialize),
	//    필요한 것만 내린다는 그 개념을 위배하게됩니다.
	public BusinessMemberResponse retrieve(long businessMemberId) {
		// 1. BusinessMemberId 로 데이터베이스에서 사업자 정보를 조회한다.
		return businessMemberRepository.findById(businessMemberId)

		// 2. 조회된 사업자 정보를 BusinessMemberResponse 로 모양을 바꿔서 반환한다.
			.map(BusinessMemberResponse::from)
			.orElseThrow();
	}

}
