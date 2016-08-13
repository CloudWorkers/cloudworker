package com.cloudworkers.cloudworker.web.rest;

import com.cloudworkers.cloudworker.Application;
import com.cloudworkers.cloudworker.domain.Config;
import com.cloudworkers.cloudworker.repository.ConfigRepository;
import com.cloudworkers.cloudworker.service.ConfigService;

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

import com.cloudworkers.cloudworker.domain.enumeration.ConfigurationKeys;

/**
 * Test class for the ConfigResource REST controller.
 *
 * @see ConfigResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ConfigResourceIntTest {



    private static final ConfigurationKeys DEFAULT_KEY = ConfigurationKeys.VERSION;
    private static final ConfigurationKeys UPDATED_KEY = ConfigurationKeys.MAX_WORKERS;
    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";

    @Inject
    private ConfigRepository configRepository;

    @Inject
    private ConfigService configService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restConfigMockMvc;

    private Config config;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConfigResource configResource = new ConfigResource();
        ReflectionTestUtils.setField(configResource, "configService", configService);
        this.restConfigMockMvc = MockMvcBuilders.standaloneSetup(configResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        config = new Config();
        config.setKey(DEFAULT_KEY);
        config.setValue(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createConfig() throws Exception {
        int databaseSizeBeforeCreate = configRepository.findAll().size();

        // Create the Config

        restConfigMockMvc.perform(post("/api/configs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(config)))
                .andExpect(status().isCreated());

        // Validate the Config in the database
        List<Config> configs = configRepository.findAll();
        assertThat(configs).hasSize(databaseSizeBeforeCreate + 1);
        Config testConfig = configs.get(configs.size() - 1);
        assertThat(testConfig.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testConfig.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = configRepository.findAll().size();
        // set the field null
        config.setKey(null);

        // Create the Config, which fails.

        restConfigMockMvc.perform(post("/api/configs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(config)))
                .andExpect(status().isBadRequest());

        List<Config> configs = configRepository.findAll();
        assertThat(configs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = configRepository.findAll().size();
        // set the field null
        config.setValue(null);

        // Create the Config, which fails.

        restConfigMockMvc.perform(post("/api/configs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(config)))
                .andExpect(status().isBadRequest());

        List<Config> configs = configRepository.findAll();
        assertThat(configs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllConfigs() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configs
        restConfigMockMvc.perform(get("/api/configs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(config.getId().intValue())))
                .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }

    @Test
    @Transactional
    public void getConfig() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get the config
        restConfigMockMvc.perform(get("/api/configs/{id}", config.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(config.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConfig() throws Exception {
        // Get the config
        restConfigMockMvc.perform(get("/api/configs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfig() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

		int databaseSizeBeforeUpdate = configRepository.findAll().size();

        // Update the config
        config.setKey(UPDATED_KEY);
        config.setValue(UPDATED_VALUE);

        restConfigMockMvc.perform(put("/api/configs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(config)))
                .andExpect(status().isOk());

        // Validate the Config in the database
        List<Config> configs = configRepository.findAll();
        assertThat(configs).hasSize(databaseSizeBeforeUpdate);
        Config testConfig = configs.get(configs.size() - 1);
        assertThat(testConfig.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testConfig.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteConfig() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

		int databaseSizeBeforeDelete = configRepository.findAll().size();

        // Get the config
        restConfigMockMvc.perform(delete("/api/configs/{id}", config.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Config> configs = configRepository.findAll();
        assertThat(configs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
