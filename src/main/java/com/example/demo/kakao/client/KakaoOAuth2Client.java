package com.example.demo.kakao.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.demo.kakao.client.response.KakaoTokenResponse;
import com.example.demo.kakao.client.response.KakaoUserInfoResponse;

// --Client 로 끝나네?
// 그럼 여기서 카카오쪽 API 호출할거다..!
@Component
public class KakaoOAuth2Client {

	private final static String KAKAO_AUTH_BASE_URL = "https://kauth.kakao.com";
	private final static String KAKAO_API_BASE_URL = "https://kapi.kakao.com";
	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${oauth.kakao.client_id}")
	private String clientId;

	@Value("${oauth.kakao.redirect_uri}")
	private String redirectUri;

	public String generateRedirectUrl() {
		return KAKAO_AUTH_BASE_URL + "/oauth/authorize"
			+ "?client_id=" + clientId
			+ "&redirect_uri=" + redirectUri
			+ "&response_type=" + "code";
	}

	public String getAccessToken(String code) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "authorization_code");
		body.add("client_id", clientId);
		body.add("code", code);

		ResponseEntity<KakaoTokenResponse> resp = restTemplate.postForEntity(
			KAKAO_AUTH_BASE_URL + "/oauth/token",
			new HttpEntity<>(body, headers),
			KakaoTokenResponse.class
		);
		if (resp.getStatusCode().isError()) {
			throw new RuntimeException(String.format("KAKAO AccessToken 조회에 실패했습니다. (code : %s)", code));
		}
		return resp.getBody().getAccessToken();
		// 카카오쪽의 REST API 요청에 대한 응답은 JSON 으로 온다.
		// ex) {"access_token":"rPQaDJc02-pQEPJzbJrcgwIxJ0PIJnWqR5T5ScUkCiolDQAAAYl8_hia","token_type":"bearer","refresh_token":"XsDZtwDLuy0VIaXkUj9BXPy9XAOki1tmR4-kJKTBCiolDQAAAYl8_hiZ","expires_in":21599,"scope":"profile_image profile_nickname","refresh_token_expires_in":5183999}
		// 그래서 우리는 이 데이터를 객체에 옮겨담을 필요가 있다.
		//   (매핑한다 -> 수동으로 하지 않고 스프링에서 기본적으로 제공해주는 Jackson Library 를 사용하게된다)
	}

	public KakaoUserInfoResponse getUserInfo(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);

		ResponseEntity<KakaoUserInfoResponse> resp = restTemplate.exchange(
			KAKAO_API_BASE_URL + "/v2/user/me",
			HttpMethod.GET,
			new HttpEntity<>(headers),
			KakaoUserInfoResponse.class
		);
		if (resp.getStatusCode().isError()) {
			throw new RuntimeException("KAKAO 사용자 정보 조회 실패");
		}
		return resp.getBody();
	}

}
