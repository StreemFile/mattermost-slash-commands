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
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestAccessService {
	private final RequestAccessDao requestAccessDao;

	private static final Pattern idPattern = Pattern.compile("\"action\":\"(\\d+)\"");
	private static final Pattern devOpsPattern = Pattern.compile("\"user_name\":\"(.*)\",\"channel_id\"");

	public void createRequestAccess(String username, String text) {
		RequestAccessEntity requestAccessEntity = getRequestAccessEntity(username, text);
		requestAccessDao.createRequestAccess(requestAccessEntity);
		AccessRequestDto toPm = getPmAccessRequestDto(requestAccessEntity);
		AccessRequestDto toUser = getUserRequestCreationConfirmation(requestAccessEntity);
		try {
			sendRequestAccess(toPm);
			sendRequestAccess(toUser);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void sendRequestAccess(AccessRequestDto accessRequestDto) throws IOException {
		log.info("sendRequestAccess");
		String webhookUrl = "https://chat.asd.team/hooks/87wrppcejjfztqshkw7qr5ux1y";
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(webhookUrl);
		postMethod.addRequestHeader("Content-Type", "application/json");
		Gson gson = new Gson();
		RequestEntity requestEntity = new StringRequestEntity(gson.toJson(accessRequestDto), "application/json", "UTF-8");
		log.info(gson.toJson(accessRequestDto));
		postMethod.setRequestEntity(requestEntity);
		int code = httpClient.executeMethod(postMethod);
		log.info(code + "");
	}

	private AccessRequestDto getPmAccessRequestDto(RequestAccessEntity requestAccessEntity) {
		Map<String, String> attachmentText = new LinkedHashMap<>();
		attachmentText.put("Access request", "");
		attachmentText.put("Project", requestAccessEntity.getProject());
		attachmentText.put("Request", requestAccessEntity.getRequest());
		attachmentText.put("Requester", requestAccessEntity.getRequester());
		attachmentText.put("Request ID", requestAccessEntity.getId().toString());
		Map<String, String> attachmentActions = new LinkedHashMap<>();
		attachmentActions.put("Approve", "https://mattermost-slash-commands.herokuapp.com/request-access/approve/pm");
		attachmentActions.put("Reject", "https://mattermost-slash-commands.herokuapp.com/request-access/reject/pm");
		return getAccessRequestDto(requestAccessEntity.getId(), requestAccessEntity.getManager(), attachmentText, attachmentActions);
	}

	private AccessRequestDto getAccessRequestDto(Long requestId, String channel,
			Map<String, String> textMap, Map<String, String> actionsMap) {
		AccessRequestDto accessRequestDto = AccessRequestDto.builder().build();
		accessRequestDto.setChannel(channel);
		AttachmentDto attachmentDto = AttachmentDto.builder().build();
		if (!CollectionUtils.isEmpty(textMap)) {
			StringBuilder attachmentText = new StringBuilder();
			textMap.forEach((k, v) -> attachmentText.append("\n" + k + ": " + v));
			attachmentDto.setText(attachmentText.toString());
		}
		if (!CollectionUtils.isEmpty(actionsMap)) {
			List<ActionsDto> actionsDtoList = new ArrayList<>();
			actionsMap.forEach((k, v) -> actionsDtoList.add(getActionsDto(requestId, k, v)));
			attachmentDto.setActions(actionsDtoList);
		}
		List<AttachmentDto> attachmentDtoList = new ArrayList<>();
		attachmentDtoList.add(attachmentDto);
		accessRequestDto.setAttachments(attachmentDtoList);
		return accessRequestDto;
	}

	private AccessRequestDto getAccessRequestDto(String channel, String text) {
		return AccessRequestDto.builder()
				.channel(channel)
				.text(text)
				.build();
	}


	private ActionsDto getActionsDto(Long requestId, String name, String url) {
		ActionsDto actionsDto = ActionsDto.builder().build();
		actionsDto.setId(name);
		actionsDto.setName(name);
		IntegrationDto integrationDto = IntegrationDto.builder().build();
		integrationDto.setUrl(url);
		ContextDto contextDto = ContextDto.builder().build();
		contextDto.setAction(requestId.toString());
		integrationDto.setContext(contextDto);
		actionsDto.setIntegration(integrationDto);
		return actionsDto;
	}

	private AccessRequestDto getUserRequestCreationConfirmation(RequestAccessEntity requestAccessEntity) {
		Map<String, String> attachmentText = new LinkedHashMap<>();
		attachmentText.put("Your access request was successfully created!", "");
		attachmentText.put("Project", requestAccessEntity.getProject());
		attachmentText.put("Request", requestAccessEntity.getRequest());
		attachmentText.put("Requester", requestAccessEntity.getRequester());
		attachmentText.put("Request ID", requestAccessEntity.getId().toString());
		return getAccessRequestDto(requestAccessEntity.getId(), requestAccessEntity.getRequester(), attachmentText, Collections.EMPTY_MAP);
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

	public void answerToRequestAccessByPm(String requestBody, boolean isApproved) {
		long requestId = findRequestId(requestBody);
		if (isApproved) {
			log.info("Request with id " + requestId + " is approved by PM");
		} else {
			log.info("Request with id " + requestId + " is rejected by PM");
		}
		log.info(requestBody);
		RequestAccessEntity requestAccessEntity = requestAccessDao.getRequestAccess(requestId);
		if (requestAccessEntity == null) {
			log.error("requestAccessEntity is null");
			return;
		}
		requestAccessEntity.setState(isApproved ? RequestAccessState.APPROVED_BY_PM : RequestAccessState.REJECTED);
		requestAccessDao.updateRequestAccess(requestAccessEntity);
		try {
			sendAnswerToPm(requestAccessEntity, isApproved);
			if (isApproved) {
				AccessRequestDto accessRequestDto = getDevOpsAccessRequestDto(requestAccessEntity);
				sendRequestAccess(accessRequestDto);
			} else {
				sendAnswerToRequester(requestAccessEntity, false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private AccessRequestDto getDevOpsAccessRequestDto(RequestAccessEntity requestAccessEntity) {
		Map<String, String> attachmentText = new LinkedHashMap<>();
		attachmentText.put("Access request", "");
		attachmentText.put("Project", requestAccessEntity.getProject());
		attachmentText.put("Request", requestAccessEntity.getRequest());
		attachmentText.put("Requester", requestAccessEntity.getRequester());
		attachmentText.put("Approved by", requestAccessEntity.getManager());
		attachmentText.put("Request ID", requestAccessEntity.getId().toString());
		Map<String, String> attachmentActions = new LinkedHashMap<>();
		attachmentActions.put("Approve", "https://mattermost-slash-commands.herokuapp.com/request-access/approve/devops");
		return getAccessRequestDto(requestAccessEntity.getId(), "devops", attachmentText, attachmentActions);
	}

	public void sendAnswerToPm(RequestAccessEntity requestAccessEntity, boolean isApproved) throws IOException {
		String text = "Access request is ";
		AccessRequestDto accessRequestDto = getAccessRequestDto(requestAccessEntity.getManager(), isApproved ? text + "approved" : text + "rejected" );
		sendRequestAccess(accessRequestDto);
	}

	public void sendAnswerToRequester(RequestAccessEntity requestAccessEntity, boolean isApproved) throws IOException {
		String str = "Your access request is ";
		StringBuilder text = new StringBuilder();
		text.append(isApproved ? str + "approved" : str + "rejected");
		text.append("\nProject: " + requestAccessEntity.getProject());
		text.append("\nRequest: " + requestAccessEntity.getRequest());
		AccessRequestDto accessRequestDto = getAccessRequestDto(requestAccessEntity.getRequester(), text.toString());
		if (isApproved) {
			Map<String, String> actionsMap = new LinkedHashMap<>();
			actionsMap.put("Submit", "https://mattermost-slash-commands.herokuapp.com/request-access/approve/user");
			accessRequestDto = getAccessRequestDto(requestAccessEntity.getId(), requestAccessEntity.getRequester(), Collections.EMPTY_MAP, actionsMap);
		}
		sendRequestAccess(accessRequestDto);
	}

	public void sendAnswerToDevops() throws IOException {
		String text = "Request is approved!";
		AccessRequestDto accessRequestDto = getAccessRequestDto("devops", text);
		sendRequestAccess(accessRequestDto);
	}

	private Long findRequestId(String requestBody) {
		Matcher matcher = idPattern.matcher(requestBody);
		if (matcher.find()) {
			return NumberUtils.createLong(matcher.group(1));
		}
		return 0l;
	}

	public void answerToRequestAccessByDevOps(String requestBody) {
		long id = findRequestId(requestBody);
		log.info("Request with id " + id + " is approved by DevOps");
		log.info(requestBody);
		RequestAccessEntity requestAccessEntity = requestAccessDao.getRequestAccess(id);
		if (requestAccessEntity == null) {
			log.error("requestAccessEntity is null");
			return;
		}
		requestAccessEntity.setDevops(getDevOps(requestBody));
		requestAccessEntity.setState(RequestAccessState.APPROVED_BY_DEVOPS);
		requestAccessDao.updateRequestAccess(requestAccessEntity);
		try {
			sendAnswerToDevops();
			sendAnswerToRequester(requestAccessEntity, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getDevOps(String requestBody) {
		Matcher matcher = devOpsPattern.matcher(requestBody);
		if (matcher.find()) {
			return "@" + matcher.group(1);
		}
		return "";
	}

	public void approveRequestAccessByUser(String requestBody) {
		long id = findRequestId(requestBody);
		RequestAccessEntity requestAccessEntity = requestAccessDao.getRequestAccess(id);
		if (requestAccessEntity == null) {
			log.error("requestAccessEntity is null");
			return;
		}
		requestAccessEntity.setState(RequestAccessState.APPROVED_BY_USER);
		requestAccessDao.updateRequestAccess(requestAccessEntity);
		sendAnswerToUserOnSubmit(requestAccessEntity.getRequester());
	}

	private void sendAnswerToUserOnSubmit(String username) {
		String text = "Submitted!";
		AccessRequestDto accessRequestDto = getAccessRequestDto(username, text);
		try {
			sendRequestAccess(accessRequestDto);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
