package com.example.demo.common.helper;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.demo.common.type.MemberType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtHelper {

	private static final long ACCESS_TOKEN_DUE = 60 * 60 * 24 * 1000; // 1 Day
	// 토큰에 서명을 한다는 것은.
	// 누가 토큰을 몰래 위조해오는 것을 방지하기 위해서,
	// 내가 알아볼 수 있는 수단을 만들어두는것.

	private String secret = "ch4njunch4njunch4njunch4njunch4njunch4njunch4njunch4njunch4njunch4njunch4njunch4njunch4njunch4njun";

	// 일반 사용자(member), 사업자(business_member) (테이블 구분)
	// Jwts 라이브러리 사용법은 그냥 구글링하면 나오는거니까
	public String generateAccessToken(MemberType type, long id) {
		Key key = Keys.hmacShaKeyFor(secret.getBytes());
		Map<String, Object> claims = new HashMap<>();
		claims.put("MEMBER_TYPE", type); // GENERAL
		claims.put("MEMBER_ID", id); // 3

		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_DUE))
			.signWith(key, SignatureAlgorithm.HS512)
			.compact();
	}

	public boolean validation(String accessToken) throws RuntimeException {
		Key key = Keys.hmacShaKeyFor(secret.getBytes());
		JwtParser parser = Jwts.parserBuilder()
			.setSigningKey(key)
			.build();
		parser.parse(accessToken);
		// TODO: 토큰 만료 체크안되는거 확인 필요함.
		return Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.isSigned(accessToken);
	}

	public String getClaim(String token, String claimKey) {
		Key key = Keys.hmacShaKeyFor(secret.getBytes());
		Jws<Claims> headerClaimsJwt = Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);
		return String.valueOf(headerClaimsJwt.getBody().get(claimKey));
	}
}
