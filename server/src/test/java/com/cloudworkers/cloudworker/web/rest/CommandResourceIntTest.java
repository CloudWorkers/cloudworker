package com.cloudworkers.cloudworker.web.rest;

import com.cloudworkers.cloudworker.Application;
import com.cloudworkers.cloudworker.domain.Command;
import com.cloudworkers.cloudworker.repository.CommandRepository;

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
 * Test class for the CommandResource REST controller.
 *
 * @see CommandResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class CommandResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_COMMAND = "AAAAA";
    private static final String UPDATED_COMMAND = "BBBBB";
    private static final String DEFAULT_ARGS = "AAAAA";
    private static final String UPDATED_ARGS = "BBBBB";

    @Inject
    private CommandRepository commandRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCommandMockMvc;

    private Command command;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommandResource commandResource = new CommandResource();
        ReflectionTestUtils.setField(commandResource, "commandRepository", commandRepository);
        this.restCommandMockMvc = MockMvcBuilders.standaloneSetup(commandResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        command = new Command();
        command.setDescription(DEFAULT_DESCRIPTION);
        command.setCommand(DEFAULT_COMMAND);
        command.setArgs(DEFAULT_ARGS);
    }

    @Test
    @Transactional
    public void createCommand() throws Exception {
        int databaseSizeBeforeCreate = commandRepository.findAll().size();

        // Create the Command

        restCommandMockMvc.perform(post("/api/commands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(command)))
                .andExpect(status().isCreated());

        // Validate the Command in the database
        List<Command> commands = commandRepository.findAll();
        assertThat(commands).hasSize(databaseSizeBeforeCreate + 1);
        Command testCommand = commands.get(commands.size() - 1);
        assertThat(testCommand.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCommand.getCommand()).isEqualTo(DEFAULT_COMMAND);
        assertThat(testCommand.getArgs()).isEqualTo(DEFAULT_ARGS);
    }

    @Test
    @Transactional
    public void getAllCommands() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get all the commands
        restCommandMockMvc.perform(get("/api/commands?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(command.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].command").value(hasItem(DEFAULT_COMMAND.toString())))
                .andExpect(jsonPath("$.[*].args").value(hasItem(DEFAULT_ARGS.toString())));
    }

    @Test
    @Transactional
    public void getCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

        // Get the command
        restCommandMockMvc.perform(get("/api/commands/{id}", command.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(command.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.command").value(DEFAULT_COMMAND.toString()))
            .andExpect(jsonPath("$.args").value(DEFAULT_ARGS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommand() throws Exception {
        // Get the command
        restCommandMockMvc.perform(get("/api/commands/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

		int databaseSizeBeforeUpdate = commandRepository.findAll().size();

        // Update the command
        command.setDescription(UPDATED_DESCRIPTION);
        command.setCommand(UPDATED_COMMAND);
        command.setArgs(UPDATED_ARGS);

        restCommandMockMvc.perform(put("/api/commands")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(command)))
                .andExpect(status().isOk());

        // Validate the Command in the database
        List<Command> commands = commandRepository.findAll();
        assertThat(commands).hasSize(databaseSizeBeforeUpdate);
        Command testCommand = commands.get(commands.size() - 1);
        assertThat(testCommand.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCommand.getCommand()).isEqualTo(UPDATED_COMMAND);
        assertThat(testCommand.getArgs()).isEqualTo(UPDATED_ARGS);
    }

    @Test
    @Transactional
    public void deleteCommand() throws Exception {
        // Initialize the database
        commandRepository.saveAndFlush(command);

		int databaseSizeBeforeDelete = commandRepository.findAll().size();

        // Get the command
        restCommandMockMvc.perform(delete("/api/commands/{id}", command.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Command> commands = commandRepository.findAll();
        assertThat(commands).hasSize(databaseSizeBeforeDelete - 1);
    }
}
