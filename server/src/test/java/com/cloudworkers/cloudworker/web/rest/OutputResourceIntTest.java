package com.cloudworkers.cloudworker.web.rest;

import com.cloudworkers.cloudworker.Application;
import com.cloudworkers.cloudworker.domain.Output;
import com.cloudworkers.cloudworker.repository.OutputRepository;
import com.cloudworkers.cloudworker.service.OutputService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the OutputResource REST controller.
 *
 * @see OutputResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OutputResourceIntTest {

    private static final String DEFAULT_MESSAGE = "AAAAA";
    private static final String UPDATED_MESSAGE = "BBBBB";

    @Inject
    private OutputRepository outputRepository;

    @Inject
    private OutputService outputService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOutputMockMvc;

    private Output output;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OutputResource outputResource = new OutputResource();
        ReflectionTestUtils.setField(outputResource, "outputService", outputService);
        this.restOutputMockMvc = MockMvcBuilders.standaloneSetup(outputResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        output = new Output();
        output.setMessage(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    public void createOutput() throws Exception {
        int databaseSizeBeforeCreate = outputRepository.findAll().size();

        // Create the Output

        restOutputMockMvc.perform(post("/api/outputs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(output)))
                .andExpect(status().isCreated());

        // Validate the Output in the database
        List<Output> outputs = outputRepository.findAll();
        assertThat(outputs).hasSize(databaseSizeBeforeCreate + 1);
        Output testOutput = outputs.get(outputs.size() - 1);
        assertThat(testOutput.getMessage()).isEqualTo(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    public void checkMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = outputRepository.findAll().size();
        // set the field null
        output.setMessage(null);

        // Create the Output, which fails.

        restOutputMockMvc.perform(post("/api/outputs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(output)))
                .andExpect(status().isBadRequest());

        List<Output> outputs = outputRepository.findAll();
        assertThat(outputs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOutputs() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get all the outputs
        restOutputMockMvc.perform(get("/api/outputs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(output.getId().intValue())))
                .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void getOutput() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

        // Get the output
        restOutputMockMvc.perform(get("/api/outputs/{id}", output.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(output.getId().intValue()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOutput() throws Exception {
        // Get the output
        restOutputMockMvc.perform(get("/api/outputs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOutput() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

		int databaseSizeBeforeUpdate = outputRepository.findAll().size();

        // Update the output
        output.setMessage(UPDATED_MESSAGE);

        restOutputMockMvc.perform(put("/api/outputs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(output)))
                .andExpect(status().isOk());

        // Validate the Output in the database
        List<Output> outputs = outputRepository.findAll();
        assertThat(outputs).hasSize(databaseSizeBeforeUpdate);
        Output testOutput = outputs.get(outputs.size() - 1);
        assertThat(testOutput.getMessage()).isEqualTo(UPDATED_MESSAGE);
    }

    @Test
    @Transactional
    public void deleteOutput() throws Exception {
        // Initialize the database
        outputRepository.saveAndFlush(output);

		int databaseSizeBeforeDelete = outputRepository.findAll().size();

        // Get the output
        restOutputMockMvc.perform(delete("/api/outputs/{id}", output.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Output> outputs = outputRepository.findAll();
        assertThat(outputs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
