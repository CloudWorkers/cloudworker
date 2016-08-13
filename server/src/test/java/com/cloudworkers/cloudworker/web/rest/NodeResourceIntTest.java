package com.cloudworkers.cloudworker.web.rest;

import com.cloudworkers.cloudworker.Application;
import com.cloudworkers.cloudworker.domain.Node;
import com.cloudworkers.cloudworker.repository.NodeRepository;
import com.cloudworkers.cloudworker.service.NodeService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cloudworkers.cloudworker.domain.enumeration.NodeStatus;

/**
 * Test class for the NodeResource REST controller.
 *
 * @see NodeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class NodeResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_SECRET = "AAAAA";
    private static final String UPDATED_SECRET = "BBBBB";


    private static final NodeStatus DEFAULT_STATUS = NodeStatus.DISABLED;
    private static final NodeStatus UPDATED_STATUS = NodeStatus.STOPPED;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);
    private static final String DEFAULT_HOSTNAME = "AAAAA";
    private static final String UPDATED_HOSTNAME = "BBBBB";
    private static final String DEFAULT_IP = "AAAAA";
    private static final String UPDATED_IP = "BBBBB";
    private static final String DEFAULT_OS = "AAAAA";
    private static final String UPDATED_OS = "BBBBB";

    @Inject
    private NodeRepository nodeRepository;

    @Inject
    private NodeService nodeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restNodeMockMvc;

    private Node node;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        NodeResource nodeResource = new NodeResource();
        ReflectionTestUtils.setField(nodeResource, "nodeService", nodeService);
        this.restNodeMockMvc = MockMvcBuilders.standaloneSetup(nodeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        node = new Node();
        node.setName(DEFAULT_NAME);
        node.setSecret(DEFAULT_SECRET);
        node.setStatus(DEFAULT_STATUS);
        node.setDate(DEFAULT_DATE);
        node.setHostname(DEFAULT_HOSTNAME);
        node.setIp(DEFAULT_IP);
        node.setOs(DEFAULT_OS);
    }

    @Test
    @Transactional
    public void createNode() throws Exception {
        int databaseSizeBeforeCreate = nodeRepository.findAll().size();

        // Create the Node

        restNodeMockMvc.perform(post("/api/nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(node)))
                .andExpect(status().isCreated());

        // Validate the Node in the database
        List<Node> nodes = nodeRepository.findAll();
        assertThat(nodes).hasSize(databaseSizeBeforeCreate + 1);
        Node testNode = nodes.get(nodes.size() - 1);
        assertThat(testNode.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNode.getSecret()).isEqualTo(DEFAULT_SECRET);
        assertThat(testNode.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testNode.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testNode.getHostname()).isEqualTo(DEFAULT_HOSTNAME);
        assertThat(testNode.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testNode.getOs()).isEqualTo(DEFAULT_OS);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeRepository.findAll().size();
        // set the field null
        node.setName(null);

        // Create the Node, which fails.

        restNodeMockMvc.perform(post("/api/nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(node)))
                .andExpect(status().isBadRequest());

        List<Node> nodes = nodeRepository.findAll();
        assertThat(nodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSecretIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeRepository.findAll().size();
        // set the field null
        node.setSecret(null);

        // Create the Node, which fails.

        restNodeMockMvc.perform(post("/api/nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(node)))
                .andExpect(status().isBadRequest());

        List<Node> nodes = nodeRepository.findAll();
        assertThat(nodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNodes() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get all the nodes
        restNodeMockMvc.perform(get("/api/nodes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(node.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].secret").value(hasItem(DEFAULT_SECRET.toString())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].hostname").value(hasItem(DEFAULT_HOSTNAME.toString())))
                .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP.toString())))
                .andExpect(jsonPath("$.[*].os").value(hasItem(DEFAULT_OS.toString())));
    }

    @Test
    @Transactional
    public void getNode() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

        // Get the node
        restNodeMockMvc.perform(get("/api/nodes/{id}", node.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(node.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.secret").value(DEFAULT_SECRET.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.hostname").value(DEFAULT_HOSTNAME.toString()))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP.toString()))
            .andExpect(jsonPath("$.os").value(DEFAULT_OS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNode() throws Exception {
        // Get the node
        restNodeMockMvc.perform(get("/api/nodes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNode() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

		int databaseSizeBeforeUpdate = nodeRepository.findAll().size();

        // Update the node
        node.setName(UPDATED_NAME);
        node.setSecret(UPDATED_SECRET);
        node.setStatus(UPDATED_STATUS);
        node.setDate(UPDATED_DATE);
        node.setHostname(UPDATED_HOSTNAME);
        node.setIp(UPDATED_IP);
        node.setOs(UPDATED_OS);

        restNodeMockMvc.perform(put("/api/nodes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(node)))
                .andExpect(status().isOk());

        // Validate the Node in the database
        List<Node> nodes = nodeRepository.findAll();
        assertThat(nodes).hasSize(databaseSizeBeforeUpdate);
        Node testNode = nodes.get(nodes.size() - 1);
        assertThat(testNode.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNode.getSecret()).isEqualTo(UPDATED_SECRET);
        assertThat(testNode.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testNode.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testNode.getHostname()).isEqualTo(UPDATED_HOSTNAME);
        assertThat(testNode.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testNode.getOs()).isEqualTo(UPDATED_OS);
    }

    @Test
    @Transactional
    public void deleteNode() throws Exception {
        // Initialize the database
        nodeRepository.saveAndFlush(node);

		int databaseSizeBeforeDelete = nodeRepository.findAll().size();

        // Get the node
        restNodeMockMvc.perform(delete("/api/nodes/{id}", node.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Node> nodes = nodeRepository.findAll();
        assertThat(nodes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
