package com.asd.mattermostslashcommands.repository;

import com.asd.mattermostslashcommands.entity.RequestAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestAccessRepository extends JpaRepository<RequestAccessEntity, Long> {
}
