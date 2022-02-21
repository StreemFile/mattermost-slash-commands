package com.asd.mattermostslashcommands.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 * mattermost-slash-commands.ActionsDto
 *
 * @Autor: vovamv
 * @DateTime: 21.02.2022|11:07
 * @Version ActionsDto: 1.0
 */

@Data
@Builder
public class ActionsDto {
	private String id;
	private String name;
	private IntegrationDto integration;

	public ActionsDto() {
	}
}
