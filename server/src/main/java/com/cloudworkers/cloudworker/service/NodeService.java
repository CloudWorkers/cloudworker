package com.cloudworkers.cloudworker.service;

import com.cloudworkers.cloudworker.domain.Node;
import com.cloudworkers.cloudworker.repository.NodeRepository;
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
 * Service Implementation for managing Node.
 */
@Service
@Transactional
public class NodeService {

    private final Logger log = LoggerFactory.getLogger(NodeService.class);
    
    @Inject
    private NodeRepository nodeRepository;
    
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
     *  delete the  node by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Node : {}", id);
        nodeRepository.delete(id);
    }
}
