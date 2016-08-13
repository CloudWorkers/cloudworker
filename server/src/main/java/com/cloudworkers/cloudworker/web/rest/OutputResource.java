package com.cloudworkers.cloudworker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cloudworkers.cloudworker.domain.Output;
import com.cloudworkers.cloudworker.service.OutputService;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Output.
 */
@RestController
@RequestMapping("/api")
public class OutputResource {

    private final Logger log = LoggerFactory.getLogger(OutputResource.class);
        
    @Inject
    private OutputService outputService;
    
    /**
     * POST  /outputs -> Create a new output.
     */
    @RequestMapping(value = "/outputs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Output> createOutput(@Valid @RequestBody Output output) throws URISyntaxException {
        log.debug("REST request to save Output : {}", output);
        if (output.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("output", "idexists", "A new output cannot already have an ID")).body(null);
        }
        Output result = outputService.save(output);
        return ResponseEntity.created(new URI("/api/outputs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("output", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /outputs -> Updates an existing output.
     */
    @RequestMapping(value = "/outputs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Output> updateOutput(@Valid @RequestBody Output output) throws URISyntaxException {
        log.debug("REST request to update Output : {}", output);
        if (output.getId() == null) {
            return createOutput(output);
        }
        Output result = outputService.save(output);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("output", output.getId().toString()))
            .body(result);
    }

    /**
     * GET  /outputs -> get all the outputs.
     */
    @RequestMapping(value = "/outputs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Output>> getAllOutputs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Outputs");
        Page<Output> page = outputService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/outputs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /outputs/:id -> get the "id" output.
     */
    @RequestMapping(value = "/outputs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Output> getOutput(@PathVariable Long id) {
        log.debug("REST request to get Output : {}", id);
        Output output = outputService.findOne(id);
        return Optional.ofNullable(output)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /outputs/:id -> delete the "id" output.
     */
    @RequestMapping(value = "/outputs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOutput(@PathVariable Long id) {
        log.debug("REST request to delete Output : {}", id);
        outputService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("output", id.toString())).build();
    }
}
