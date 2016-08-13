package com.cloudworkers.cloudworker.service;

import com.cloudworkers.cloudworker.domain.Worker;
import com.cloudworkers.cloudworker.repository.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Worker.
 */
@Service
@Transactional
public class WorkerService {

    private final Logger log = LoggerFactory.getLogger(WorkerService.class);
    
    @Inject
    private WorkerRepository workerRepository;
    
    /**
     * Save a worker.
     * @return the persisted entity
     */
    public Worker save(Worker worker) {
        log.debug("Request to save Worker : {}", worker);
        Worker result = workerRepository.save(worker);
        return result;
    }

    /**
     *  get all the workers.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Worker> findAll(Pageable pageable) {
        log.debug("Request to get all Workers");
        Page<Worker> result = workerRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one worker by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Worker findOne(Long id) {
        log.debug("Request to get Worker : {}", id);
        Worker worker = workerRepository.findOne(id);
        return worker;
    }

    /**
     *  delete the  worker by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Worker : {}", id);
        workerRepository.delete(id);
    }
}
