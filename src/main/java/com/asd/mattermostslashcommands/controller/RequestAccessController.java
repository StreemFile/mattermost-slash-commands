package com.asd.mattermostslashcommands.controller;

import com.asd.mattermostslashcommands.TestDto;
import com.asd.mattermostslashcommands.service.RequestAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request-access")
@RequiredArgsConstructor
public class RequestAccessController {
	private final RequestAccessService service;

//	@GetMapping
//	private String createRequestAccess(@RequestParam)

	@GetMapping("/test")
	public String test() {
		return new TestDto("TEST");
	}

	@GetMapping("/test2")
	public void test2() {
		System.out.println("DWADAMWFA");
	}
}
