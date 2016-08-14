package com.cloudworkers.cloudworker.repository;

import com.cloudworkers.cloudworker.domain.Worker;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Worker entity.
 */
public interface WorkerRepository extends JpaRepository<Worker,Long> {

	@Query(value = "SELECT * FROM worker WHERE node_id = ?1", nativeQuery = true)
	List<Worker> findAllByNodeId(Long id);
}
