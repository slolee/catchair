package com.example.demo.jwtauth.filter;

import java.util.Arrays;

import org.springframework.util.AntPathMatcher;

import jakarta.servlet.http.HttpServletRequest;

public class JwtAuthenticationFilterSkipMatcher {

	private final AntPathMatcher pathMatcher = new AntPathMatcher();
	private final String[] excludedPaths = {
		"/favicon.ico", "/oauth2/**", "/business-member/signup", "/business-member/login",
		"/swagger-ui/**", "/v3/api-docs/**",
		"/ping"
	};

	public boolean shouldSkipFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return Arrays.stream(excludedPaths)
			.anyMatch(pattern -> pathMatcher.match(pattern, path));
	}

}
