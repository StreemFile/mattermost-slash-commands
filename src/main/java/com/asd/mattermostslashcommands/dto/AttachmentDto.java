package com.asd.mattermostslashcommands.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AttachmentDto {
	private String text;
	private List<ActionsDto> actions;
}
