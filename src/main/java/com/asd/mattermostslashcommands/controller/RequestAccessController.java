package com.asd.mattermostslashcommands.controller;

import com.asd.mattermostslashcommands.service.RequestAccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	@PostMapping("/approve")
	private void approveRequestAccess(@RequestBody String requestBody) {
		service.answerToRequestAccessByPm(requestBody, true);
	}

	@PostMapping("/reject")
	private void rejectRequestAccess(@RequestBody String requestBody) {
		service.answerToRequestAccessByPm(requestBody, false);
	}

	@PostMapping("/approve-by-devops")
	private void approveRequestAccessByDevOps(@RequestBody String requestBody) {
		service.answerToRequestAccessByDevOps(requestBody);
	}
}
