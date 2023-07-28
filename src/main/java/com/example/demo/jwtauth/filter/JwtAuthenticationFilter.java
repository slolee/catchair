package com.example.demo.jwtauth.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.common.helper.JwtHelper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtHelper jwtHelper;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// 1. AccessToken 가져온거 꺼내
		// 		Request Header 에 Authorization 이라는 이름으로 담아서 보낸다.
		// 		request.getHeader 했을 때 있으면 나오고, 없으면 null 이 나와요.
		//      Bearer ~~~~~~~~~~~~~~~~~~
		try {
			System.out.println(request.getRequestURL());
			String accessToken = Optional.ofNullable(request.getHeader("Authorization"))
				.map(header -> header.substring("Bearer ".length()))
				  // map 은 기본적으로 들어갈때 모양이랑 나올 때 모양이랑 다르게 할 수 있다.
				  // 코드를 넣기위한 목적으로 쓴거다.
				  // 이 코드는 Optional 에 값이 있을때만 동작한다.
				.orElseThrow(() -> new RuntimeException("AccessToken 을 찾을 수 없습니다."));

			// 2. 그리고 그걸 검증(사인 확인, 유효기간 체크,,,)해
			if (jwtHelper.validation(accessToken)) {
				filterChain.doFilter(request, response); // 통과시켜
			} else {
				// 검증 실패했으면 에러(401 UNAUTHORIZED)
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		} catch (RuntimeException ex) {
			// 토큰을 꺼내거나, 검증하는 과정에서 에러가 나면.. 로그찍고 돌려보내
			log.error("AccessToken 인증 실패", ex);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		JwtAuthenticationFilterSkipMatcher skipMatcher = new JwtAuthenticationFilterSkipMatcher();
		return skipMatcher.shouldSkipFilter(request);
	}

}
