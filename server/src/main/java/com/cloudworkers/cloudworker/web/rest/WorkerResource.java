package com.cloudworkers.cloudworker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cloudworkers.cloudworker.domain.Worker;
import com.cloudworkers.cloudworker.service.WorkerService;
import com.cloudworkers.cloudworker.web.rest.util.HeaderUtil;
import com.cloudworkers.cloudworker.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Worker.
 */
@RestController
@RequestMapping("/api")
public class WorkerResource {

    private final Logger log = LoggerFactory.getLogger(WorkerResource.class);
        
    @Inject
    private WorkerService workerService;
    
    /**
     * POST  /workers -> Create a new worker.
     */
    @RequestMapping(value = "/workers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Worker> createWorker(@RequestBody Worker worker) throws URISyntaxException {
        log.debug("REST request to save Worker : {}", worker);
        if (worker.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("worker", "idexists", "A new worker cannot already have an ID")).body(null);
        }
        Worker result = workerService.save(worker);
        return ResponseEntity.created(new URI("/api/workers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("worker", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /workers -> Updates an existing worker.
     */
    @RequestMapping(value = "/workers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Worker> updateWorker(@RequestBody Worker worker) throws URISyntaxException {
        log.debug("REST request to update Worker : {}", worker);
        if (worker.getId() == null) {
            return createWorker(worker);
        }
        Worker result = workerService.save(worker);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("worker", worker.getId().toString()))
            .body(result);
    }

    /**
     * GET  /workers -> get all the workers.
     */
    @RequestMapping(value = "/workers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Worker>> getAllWorkers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Workers");
        Page<Worker> page = workerService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/workers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /workers/:id -> get the "id" worker.
     */
    @RequestMapping(value = "/workers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Worker> getWorker(@PathVariable Long id) {
        log.debug("REST request to get Worker : {}", id);
        Worker worker = workerService.findOne(id);
        return Optional.ofNullable(worker)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /workers/:id -> delete the "id" worker.
     */
    @RequestMapping(value = "/workers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWorker(@PathVariable Long id) {
        log.debug("REST request to delete Worker : {}", id);
        workerService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("worker", id.toString())).build();
    }
}
