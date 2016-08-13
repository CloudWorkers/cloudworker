package com.cloudworkers.cloudworker.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.cloudworkers.cloudworker.domain.Config;
import com.cloudworkers.cloudworker.service.ConfigService;
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
 * REST controller for managing Config.
 */
@RestController
@RequestMapping("/api")
public class ConfigResource {

    private final Logger log = LoggerFactory.getLogger(ConfigResource.class);
        
    @Inject
    private ConfigService configService;
    
    /**
     * POST  /configs -> Create a new config.
     */
    @RequestMapping(value = "/configs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Config> createConfig(@Valid @RequestBody Config config) throws URISyntaxException {
        log.debug("REST request to save Config : {}", config);
        if (config.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("config", "idexists", "A new config cannot already have an ID")).body(null);
        }
        Config result = configService.save(config);
        return ResponseEntity.created(new URI("/api/configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("config", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /configs -> Updates an existing config.
     */
    @RequestMapping(value = "/configs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Config> updateConfig(@Valid @RequestBody Config config) throws URISyntaxException {
        log.debug("REST request to update Config : {}", config);
        if (config.getId() == null) {
            return createConfig(config);
        }
        Config result = configService.save(config);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("config", config.getId().toString()))
            .body(result);
    }

    /**
     * GET  /configs -> get all the configs.
     */
    @RequestMapping(value = "/configs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Config>> getAllConfigs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Configs");
        Page<Config> page = configService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/configs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /configs/:id -> get the "id" config.
     */
    @RequestMapping(value = "/configs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Config> getConfig(@PathVariable Long id) {
        log.debug("REST request to get Config : {}", id);
        Config config = configService.findOne(id);
        return Optional.ofNullable(config)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /configs/:id -> delete the "id" config.
     */
    @RequestMapping(value = "/configs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteConfig(@PathVariable Long id) {
        log.debug("REST request to delete Config : {}", id);
        configService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("config", id.toString())).build();
    }
}
