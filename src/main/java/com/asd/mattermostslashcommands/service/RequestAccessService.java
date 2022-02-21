package com.asd.mattermostslashcommands.service;

import com.asd.mattermostslashcommands.dao.RequestAccessDao;
import com.asd.mattermostslashcommands.dto.AccessRequestDto;
import com.asd.mattermostslashcommands.dto.ActionsDto;
import com.asd.mattermostslashcommands.dto.AttachmentDto;
import com.asd.mattermostslashcommands.dto.ContextDto;
import com.asd.mattermostslashcommands.dto.IntegrationDto;
import com.asd.mattermostslashcommands.entity.RequestAccessEntity;
import com.asd.mattermostslashcommands.enums.RequestAccessState;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.HttpClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestAccessService {
	private final RequestAccessDao requestAccessDao;

	public void createRequestAccess(String username, String text) {
		RequestAccessEntity requestAccessEntity = getRequestAccessEntity(username, text);
		requestAccessDao.createRequestAccess(requestAccessEntity);
		AccessRequestDto toPm = getPmAccessRequestDto(requestAccessEntity);
		Gson gson = new Gson();
		log.info(gson.toJson(toPm));
		try {
			sendRequestAccess(toPm);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void sendRequestAccess(AccessRequestDto accessRequestDto) throws IOException {
		log.info("sendRequestAccess");
		String webhookUrl = "https://chat.asd.team/hooks/bynonba5aj8sjc83khm83toadh";
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(webhookUrl);
		postMethod.addRequestHeader("Content-Type", "application/json");
		Gson gson = new Gson();
		RequestEntity requestEntity = new StringRequestEntity(gson.toJson(accessRequestDto),"application/json", "UTF-8");
		log.info(gson.toJson(accessRequestDto));
		postMethod.setRequestEntity(requestEntity);
		int code = httpClient.executeMethod(postMethod);
		log.info(code + "");
	}

	private AccessRequestDto getPmAccessRequestDto(RequestAccessEntity requestAccessEntity) {
		AccessRequestDto accessRequestDto = AccessRequestDto.builder().build();
		accessRequestDto.setChannel(requestAccessEntity.getManager());
		AttachmentDto attachmentDto = AttachmentDto.builder().build();
		StringBuilder attachmentText = new StringBuilder();
		attachmentText.append("Access request");
		attachmentText.append("\nProject: " + requestAccessEntity.getProject());
		attachmentText.append("\nRequest: " + requestAccessEntity.getRequest());
		attachmentText.append("\nRequester: " + requestAccessEntity.getRequester());
		attachmentText.append("\nRequest ID: " + requestAccessEntity.getId());
		attachmentDto.setText(attachmentText.toString());
		ActionsDto approve = getActionsDto(requestAccessEntity, "Approve", "https://mattermost-slash-commands.herokuapp.com/request-access/approve");
		ActionsDto reject = getActionsDto(requestAccessEntity, "Reject", "https://mattermost-slash-commands.herokuapp.com/request-access/reject");
		List<ActionsDto> actionsDtos = Arrays.asList(approve, reject);
		attachmentDto.setActions(actionsDtos);
		List<AttachmentDto> attachmentDtoList = new ArrayList<>();
		attachmentDtoList.add(attachmentDto);
		accessRequestDto.setAttachments(attachmentDtoList);
		return accessRequestDto;
	}

	private ActionsDto getActionsDto(RequestAccessEntity requestAccessEntity, String name, String url) {
		ActionsDto actionsDto = ActionsDto.builder().build();
		actionsDto.setId(name);
		actionsDto.setName(name);
		IntegrationDto integrationDto = IntegrationDto.builder().build();
		integrationDto.setUrl(url);
		ContextDto contextDto = ContextDto.builder().build();
		contextDto.setAction(requestAccessEntity.getId().toString());
		return actionsDto;
	}

	private RequestAccessEntity getRequestAccessEntity(String username, String text) {
		List<String> params = Arrays.asList(text.split(","));
		return RequestAccessEntity.builder()
				.project(params.get(0))
				.request(params.get(1))
				.manager(params.get(2).contains("@") ? params.get(2) : "@" + params.get(2))
				.requester("@" + username)
				.state(RequestAccessState.PENDING)
				.build();
	}
}
