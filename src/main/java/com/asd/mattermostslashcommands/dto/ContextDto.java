package com.asd.mattermostslashcommands.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 * mattermost-slash-commands.ActionDto
 *
 * @Autor: vovamv
 * @DateTime: 21.02.2022|11:09
 * @Version ActionDto: 1.0
 */

@Data
@Builder
public class ContextDto {
	private String action;

	public ContextDto() {
	}
}
