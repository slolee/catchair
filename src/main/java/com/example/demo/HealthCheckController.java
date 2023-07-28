package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // 넌 지금부터 컨트롤러야! 라고 역할을 주는거
public class HealthCheckController {

	// ping - pong API 를 만들거다.

	// 얘도 마찬가지로 아직 아무것도아닌 순수한 메소드입니다.
	// 얘를 API(버튼) 로 만들려면 어떻게해야 하냐면..?
	@GetMapping("/ping") // 넌 지금부터 API(Handler) 야! 라고 붙여주는거
	public String method() {
		System.out.println("ping 버튼눌렸다!!!");
		return "pong2";
	}

	/**
	 *  질문?
	 *    기본적으로 method() 라는 메소드를 호출하려면.. 뭐가 있어야하냐면 HealthCheckController 객체가 있어야합니다.
	 *     (붕어빵틀(클래스) - 붕어빵(객체))
	 *    객체를 어떻게 만들죠? -> new HealthCheckController();
	 *    그럼 나는 객체를 만들지도 않았는데 HealthCheckController 클래스 안에 있는 method() 가 어떻게 호출된거지..?
	 *    -> IoC & DI, Bean 에 대한 이야기.. ***
	 */
}

/**
 *  Database 설치를 해야합니다.
 *  연우님의 컴퓨터에는 MySQL 이 설치되어있나요?
 */