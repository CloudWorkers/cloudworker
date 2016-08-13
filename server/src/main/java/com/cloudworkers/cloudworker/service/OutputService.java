package com.cloudworkers.cloudworker.service;

import com.cloudworkers.cloudworker.domain.Output;
import com.cloudworkers.cloudworker.repository.OutputRepository;
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
 * Service Implementation for managing Output.
 */
@Service
@Transactional
public class OutputService {

    private final Logger log = LoggerFactory.getLogger(OutputService.class);
    
    @Inject
    private OutputRepository outputRepository;
    
    /**
     * Save a output.
     * @return the persisted entity
     */
    public Output save(Output output) {
        log.debug("Request to save Output : {}", output);
        Output result = outputRepository.save(output);
        return result;
    }

    /**
     *  get all the outputs.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Output> findAll(Pageable pageable) {
        log.debug("Request to get all Outputs");
        Page<Output> result = outputRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one output by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Output findOne(Long id) {
        log.debug("Request to get Output : {}", id);
        Output output = outputRepository.findOne(id);
        return output;
    }

    /**
     *  delete the  output by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Output : {}", id);
        outputRepository.delete(id);
    }
}
