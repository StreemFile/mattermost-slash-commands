package com.asd.mattermostslashcommands.entity;

import com.asd.mattermostslashcommands.enums.RequestAccessState;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "request_access")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class RequestAccessEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String project;
	private String request;
	private String manager;
	private String requester;
	private RequestAccessState state;
	@CreatedDate
	@Setter(value = AccessLevel.NONE)
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	@LastModifiedDate
	@Setter(value = AccessLevel.NONE)
	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

}
