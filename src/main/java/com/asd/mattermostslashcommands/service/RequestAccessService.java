package com.asd.mattermostslashcommands.service;

import com.asd.mattermostslashcommands.dao.RequestAccessDao;
import com.asd.mattermostslashcommands.entity.RequestAccessEntity;
import com.asd.mattermostslashcommands.enums.RequestAccessState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestAccessService {
	private final RequestAccessDao requestAccessDao;

	public void createRequestAccess(String username, String text) {
		RequestAccessEntity requestAccessEntity = getRequestAccessEntity(username, text);
		requestAccessDao.createRequestAccess(requestAccessEntity);
	}

	private RequestAccessEntity getRequestAccessEntity(String username, String text) {
		List<String> params = Arrays.asList(text.split(","));
		return RequestAccessEntity.builder()
				.project(params.get(0))
				.request(params.get(1))
				.manager(params.get(2))
				.requester("@" + username)
				.state(RequestAccessState.PENDING)
				.build();
	}
}
