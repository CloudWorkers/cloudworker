package com.cloudworkers.cloudworker.repository;

import com.cloudworkers.cloudworker.domain.Node;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Node entity.
 */
public interface NodeRepository extends JpaRepository<Node,Long> {

	@Query(value = "SELECT * FROM node WHERE secret = ?1", nativeQuery = true)
	Node findBySecret(String secret);
}
