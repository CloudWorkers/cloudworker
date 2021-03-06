package com.cloudworkers.cloudworker.repository;

import com.cloudworkers.cloudworker.domain.Config;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Config entity.
 */
public interface ConfigRepository extends JpaRepository<Config,Long> {

	@Query(value = "SELECT * FROM config WHERE node_id = ?1", nativeQuery = true)
	List<Config> findAllByNodeId(Long id);
}
