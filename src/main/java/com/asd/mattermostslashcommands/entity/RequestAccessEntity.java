package com.asd.mattermostslashcommands.entity;

import com.asd.mattermostslashcommands.enums.RequestAccessState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "request_access")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestAccessEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String project;
	private String request;
	private String manager;
	private String requester;
	private RequestAccessState state;
	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	@UpdateTimestamp
	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

}
