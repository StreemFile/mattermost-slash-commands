package com.asd.mattermostslashcommands.dao;

import com.asd.mattermostslashcommands.entity.RequestAccessEntity;
import com.asd.mattermostslashcommands.repository.RequestAccessRepository;
import com.sun.tools.corba.se.idl.InvalidArgument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Created by IntelliJ IDEA.
 * mattermost-slash-commands.RequestAccessDao
 *
 * @Autor: vovamv
 * @DateTime: 18.02.2022|16:43
 * @Version RequestAccessDao: 1.0
 */
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
