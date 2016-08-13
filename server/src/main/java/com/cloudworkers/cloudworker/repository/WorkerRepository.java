package com.cloudworkers.cloudworker.repository;

import com.cloudworkers.cloudworker.domain.Worker;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Worker entity.
 */
public interface WorkerRepository extends JpaRepository<Worker,Long> {

}
