package com.asd.mattermostslashcommands.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * mattermost-slash-commands.PmAccessRequestDto
 *
 * @Autor: vovamv
 * @DateTime: 21.02.2022|10:24
 * @Version PmAccessRequestDto: 1.0
 */

@Data
@Builder
public class AccessRequestDto {
	private String channel;
	private List<AttachmentDto> attachments;
}
