package com.asd.mattermostslashcommands.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * mattermost-slash-commands.AttachmentDto
 *
 * @Autor: vovamv
 * @DateTime: 21.02.2022|11:02
 * @Version AttachmentDto: 1.0
 */

@Data
@Builder
public class AttachmentDto {
	private String text;
	private List<ActionsDto> actions;

	public AttachmentDto() {
	}
}
