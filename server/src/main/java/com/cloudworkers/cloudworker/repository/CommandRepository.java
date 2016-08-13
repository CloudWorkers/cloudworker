package com.cloudworkers.cloudworker.repository;

import com.cloudworkers.cloudworker.domain.Command;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Command entity.
 */
public interface CommandRepository extends JpaRepository<Command,Long> {

}
