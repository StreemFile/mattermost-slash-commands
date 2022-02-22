package com.asd.mattermostslashcommands.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntegrationDto {
	private String url;
	private ContextDto context;
}
