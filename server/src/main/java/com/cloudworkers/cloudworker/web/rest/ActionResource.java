package com.cloudworkers.cloudworker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cloudworkers.cloudworker.domain.Action;
import com.cloudworkers.cloudworker.domain.Node;
import com.cloudworkers.cloudworker.domain.enumeration.ActionStatus;
import com.cloudworkers.cloudworker.repository.ActionRepository;
import com.cloudworkers.cloudworker.service.NodeService;
import com.cloudworkers.cloudworker.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Action.
 */
@RestController
@RequestMapping("/api")
public class ActionResource {

    private final Logger log = LoggerFactory.getLogger(ActionResource.class);
        
    @Inject
    private ActionRepository actionRepository;

    @Inject
    private NodeService nodeService;
    
    /**
     * POST  /actions -> Create a new action.
     */
    @RequestMapping(value = "/actions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Action> createAction(@Valid @RequestBody Action action) throws URISyntaxException {
        log.debug("REST request to save Action : {}", action);
        if (action.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("action", "idexists", "A new action cannot already have an ID")).body(null);
        }
        Action result = actionRepository.save(action);
        return ResponseEntity.created(new URI("/api/actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("action", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /actions -> Updates an existing action.
     */
    @RequestMapping(value = "/actions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Action> updateAction(@Valid @RequestBody Action action) throws URISyntaxException {
        log.debug("REST request to update Action : {}", action);
        if (action.getId() == null) {
            return createAction(action);
        }
        Action result = actionRepository.save(action);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("action", action.getId().toString()))
            .body(result);
    }

    /**
     * GET  /actions -> get all the actions.
     */
    @RequestMapping(value = "/actions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Action> getAllActions() {
        log.debug("REST request to get all Actions");
        return actionRepository.findAll();
            }

    /**
     * GET  /actions/pending/:id -> get the pending actions for "id" node.
     */
    @RequestMapping(value = "/actions/pending/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Action>> getPending(@PathVariable Long id) {
        log.debug("REST request to get pending Actions for Node: {}", id);

        Node node = nodeService.findOne(id);
        if (node.getId() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        List<Action> actions = actionRepository.findByNodeIdAndStatus(id, ActionStatus.PENDING.name());
        return Optional.ofNullable(actions)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }    

    /**
     * GET  /actions/:id -> get the "id" action.
     */
    @RequestMapping(value = "/actions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Action> getAction(@PathVariable Long id) {
        log.debug("REST request to get Action : {}", id);
        Action action = actionRepository.findOne(id);
        return Optional.ofNullable(action)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /actions/:id -> delete the "id" action.
     */
    @RequestMapping(value = "/actions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAction(@PathVariable Long id) {
        log.debug("REST request to delete Action : {}", id);
        actionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("action", id.toString())).build();
    }
}
