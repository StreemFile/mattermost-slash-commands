package com.asd.mattermostslashcommands.controller;

import com.asd.mattermostslashcommands.dto.TestDto;
import com.asd.mattermostslashcommands.service.RequestAccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request-access")
@RequiredArgsConstructor
@Slf4j
public class RequestAccessController {
	private final RequestAccessService service;

	@GetMapping
	private void createRequestAccess(@RequestParam(name = "user_name") String username,
									   @RequestParam(name = "text") String text) {
		service.createRequestAccess(username, text);
	}

	@GetMapping("/approve")
	private void approveRequestAccess(@RequestParam(name = "text", required = false) String text,
			@RequestParam(name = "context", required = false) String context) {
		log.info("APPROVED");
		log.info(text);
		log.info("CON");
		log.info(context);
	}

	@GetMapping("/reject")
	private void rejectRequestAccess(@RequestParam(name = "text", required = false) String text,
			@RequestParam(name = "context", required = false) String context) {
		log.info("REJECTED");
		log.info(text);
		log.info("CON");
		log.info(context);
	}

	@GetMapping("/test")
	public TestDto test(@RequestParam(required = false, name = "channel_id") String channelId) {
		log.info("TEST CALLED");
		log.info("CHANNEL_ID: " + channelId);
		return new TestDto("TEST");
	}

	@GetMapping("/test2")
	public void test2() {
		System.out.println("DWADAMWFA");
	}
}
