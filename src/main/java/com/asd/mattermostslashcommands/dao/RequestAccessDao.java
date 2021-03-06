package com.asd.mattermostslashcommands.dao;

import com.asd.mattermostslashcommands.entity.RequestAccessEntity;
import com.asd.mattermostslashcommands.repository.RequestAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RequestAccessDao {
	private final RequestAccessRepository requestAccessRepository;

	public void createRequestAccess(RequestAccessEntity requestAccessEntity) {
		requestAccessRepository.save(requestAccessEntity);
	}

	public void updateRequestAccess(RequestAccessEntity requestAccessEntity) {
		requestAccessRepository.save(requestAccessEntity);
	}

	public RequestAccessEntity getRequestAccess(Long id) {
		return requestAccessRepository.findById(id).orElseThrow(null);
	}
}
