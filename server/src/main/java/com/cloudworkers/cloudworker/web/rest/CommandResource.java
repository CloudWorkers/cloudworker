package com.cloudworkers.cloudworker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cloudworkers.cloudworker.domain.Command;
import com.cloudworkers.cloudworker.repository.CommandRepository;
import com.cloudworkers.cloudworker.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Command.
 */
@RestController
@RequestMapping("/api")
public class CommandResource {

    private final Logger log = LoggerFactory.getLogger(CommandResource.class);
        
    @Inject
    private CommandRepository commandRepository;
    
    /**
     * POST  /commands -> Create a new command.
     */
    @RequestMapping(value = "/commands",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Command> createCommand(@RequestBody Command command) throws URISyntaxException {
        log.debug("REST request to save Command : {}", command);
        if (command.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("command", "idexists", "A new command cannot already have an ID")).body(null);
        }
        Command result = commandRepository.save(command);
        return ResponseEntity.created(new URI("/api/commands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("command", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /commands -> Updates an existing command.
     */
    @RequestMapping(value = "/commands",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Command> updateCommand(@RequestBody Command command) throws URISyntaxException {
        log.debug("REST request to update Command : {}", command);
        if (command.getId() == null) {
            return createCommand(command);
        }
        Command result = commandRepository.save(command);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("command", command.getId().toString()))
            .body(result);
    }

    /**
     * GET  /commands -> get all the commands.
     */
    @RequestMapping(value = "/commands",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Command> getAllCommands(@RequestParam(required = false) String filter) {
        if ("worker-is-null".equals(filter)) {
            log.debug("REST request to get all Commands where worker is null");
            return StreamSupport
                .stream(commandRepository.findAll().spliterator(), false)
                .filter(command -> command.getWorker() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Commands");
        return commandRepository.findAll();
            }

    /**
     * GET  /commands/:id -> get the "id" command.
     */
    @RequestMapping(value = "/commands/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Command> getCommand(@PathVariable Long id) {
        log.debug("REST request to get Command : {}", id);
        Command command = commandRepository.findOne(id);
        return Optional.ofNullable(command)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /commands/:id -> delete the "id" command.
     */
    @RequestMapping(value = "/commands/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCommand(@PathVariable Long id) {
        log.debug("REST request to delete Command : {}", id);
        commandRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("command", id.toString())).build();
    }
}
