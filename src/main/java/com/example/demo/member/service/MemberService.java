package com.example.demo.member.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.member.dto.MemberRegisterRequest;
import com.example.demo.member.entity.Member;
import com.example.demo.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;

	public Optional<Member> find(String providerName, long accountId) {
		return memberRepository.findByOauthProviderNameAndOauthAccountId(providerName, accountId);
	}

	public Member register(MemberRegisterRequest req) {
		return memberRepository.save(req.toEntity());
	}

}
