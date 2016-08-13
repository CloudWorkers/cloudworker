package com.cloudworkers.cloudworker.service;

import com.cloudworkers.cloudworker.domain.Action;
import com.cloudworkers.cloudworker.domain.Config;
import com.cloudworkers.cloudworker.domain.Node;
import com.cloudworkers.cloudworker.domain.enumeration.ConfigurationKeys;
import com.cloudworkers.cloudworker.domain.enumeration.NodeStatus;
import com.cloudworkers.cloudworker.domain.util.JSR310DateConverters.DateToZonedDateTimeConverter;
import com.cloudworkers.cloudworker.repository.ActionRepository;
import com.cloudworkers.cloudworker.repository.ConfigRepository;
import com.cloudworkers.cloudworker.repository.NodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing Node.
 */
@Service
@Transactional
public class NodeService {

    private final Logger log = LoggerFactory.getLogger(NodeService.class);
    
    @Inject
    private NodeRepository nodeRepository;
    
    @Inject
    private ConfigRepository configRepository;

    @Inject
    private ActionRepository actionRepository;
    
    /**
     * Create a node.
     * @return the a node with required generated info
     */
    public Node create(Node node) {
        log.debug("Request to create Node : {}", node);
        
    	//Generate a uuid secret
    	UUID uuid = UUID.randomUUID();   	
        node.setSecret(uuid.toString());
        node.setStatus(NodeStatus.STOPPED);
        
        //Create default config for new node 
        createDefaultConfig(node);
        
        return node;
    }
    
    /**
     * Create Default Config for a Node
     */
    protected void createDefaultConfig(Node node) {
        Config c = new Config();
        c.setNode(node);
        
        c.setItem(ConfigurationKeys.MAX_WORKERS);
        c.setValue("5");
        configRepository.saveAndFlush(c);
        
        c.setItem(ConfigurationKeys.POLL_PERIOD);
        c.setValue("30");
        configRepository.saveAndFlush(c);
    }
    
    /**
     * Save a node.
     * @return the persisted entity
     */
    public Node save(Node node) {
        log.debug("Request to save Node : {}", node);
        Node result = nodeRepository.save(node);
        return result;
    }

    /**
     * Updates a node's date.
     * @return the persisted entity
     */
    public Node updateDate(Node node) {
        log.debug("Request to update Node Date: {}", node);     
        ZonedDateTime now = DateToZonedDateTimeConverter.INSTANCE.convert(new Date());
        node.setDate(now);
        Node result = nodeRepository.save(node);
        return result;
    }

    /**
     *  get all the nodes.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Node> findAll(Pageable pageable) {
        log.debug("Request to get all Nodes");
        Page<Node> result = nodeRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one node by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Node findOne(Long id) {
        log.debug("Request to get Node : {}", id);
        Node node = nodeRepository.findOne(id);
        return node;
    }

    
    /**
     *  get one node by its secret.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Node findBySecret(String secret) {
        log.debug("Request to get Node : {}", secret);
        Node node = nodeRepository.findBySecret(secret);
        return node;
    }
    
    
    /**
     *  delete the  node by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Node : {}", id);
        nodeRepository.delete(id);
    }
}
