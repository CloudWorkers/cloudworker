package com.cloudworkers.cloudworker.service;

import com.cloudworkers.cloudworker.domain.Config;
import com.cloudworkers.cloudworker.repository.ConfigRepository;
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
 * Service Implementation for managing Config.
 */
@Service
@Transactional
public class ConfigService {

    private final Logger log = LoggerFactory.getLogger(ConfigService.class);
    
    @Inject
    private ConfigRepository configRepository;
    
    /**
     * Save a config.
     * @return the persisted entity
     */
    public Config save(Config config) {
        log.debug("Request to save Config : {}", config);
        Config result = configRepository.save(config);
        return result;
    }

    /**
     *  get all the configs.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Config> findAll(Pageable pageable) {
        log.debug("Request to get all Configs");
        Page<Config> result = configRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one config by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Config findOne(Long id) {
        log.debug("Request to get Config : {}", id);
        Config config = configRepository.findOne(id);
        return config;
    }

    /**
     *  get configs by node id.
     *  @return the configs
     */
    @Transactional(readOnly = true) 
    public List<Config> findAllByNodeId(Long id) {
        log.debug("Request to get Config for Node : {}", id);
        List<Config> configs = configRepository.findAllByNodeId(id);
        return configs;
    }
    
    
    /**
     *  delete the  config by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Config : {}", id);
        configRepository.delete(id);
    }
}
