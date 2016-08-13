package com.cloudworkers.cloudworker.repository;

import com.cloudworkers.cloudworker.domain.Output;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Output entity.
 */
public interface OutputRepository extends JpaRepository<Output,Long> {

}
