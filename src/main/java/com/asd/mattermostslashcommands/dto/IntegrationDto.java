package com.asd.mattermostslashcommands.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IntelliJ IDEA.
 * mattermost-slash-commands.IntegrationDto
 *
 * @Autor: vovamv
 * @DateTime: 21.02.2022|11:08
 * @Version IntegrationDto: 1.0
 */

@Data
@Builder
public class IntegrationDto {
	private String url;
	private ContextDto context;
}
