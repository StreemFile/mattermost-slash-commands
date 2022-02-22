package com.asd.mattermostslashcommands.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccessRequestDto {
	private String channel;
	private String text;
	private List<AttachmentDto> attachments;
}
