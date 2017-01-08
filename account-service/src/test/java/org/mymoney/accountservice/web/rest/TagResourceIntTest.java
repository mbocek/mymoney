package org.mymoney.accountservice.web.rest;

import org.mymoney.accountservice.AccountServiceApp;

import org.mymoney.accountservice.domain.Tag;
import org.mymoney.accountservice.repository.TagRepository;
import org.mymoney.accountservice.service.TagService;
import org.mymoney.accountservice.service.dto.TagDTO;
import org.mymoney.accountservice.service.mapper.TagMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TagResource REST controller.
 *
 * @see TagResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountServiceApp.class)
public class TagResourceIntTest {

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    @Inject
    private TagRepository tagRepository;

    @Inject
    private TagMapper tagMapper;

    @Inject
    private TagService tagService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTagMockMvc;

    private Tag tag;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TagResource tagResource = new TagResource();
        ReflectionTestUtils.setField(tagResource, "tagService", tagService);
        this.restTagMockMvc = MockMvcBuilders.standaloneSetup(tagResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createEntity(EntityManager em) {
        Tag tag = new Tag()
                .owner(DEFAULT_OWNER)
                .name(DEFAULT_NAME)
                .color(DEFAULT_COLOR);
        return tag;
    }

    @Before
    public void initTest() {
        tag = createEntity(em);
    }

    @Test
    @Transactional
    public void createTag() throws Exception {
        int databaseSizeBeforeCreate = tagRepository.findAll().size();

        // Create the Tag
        TagDTO tagDTO = tagMapper.tagToTagDTO(tag);

        restTagMockMvc.perform(post("/api/tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isCreated());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate + 1);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testTag.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTag.getColor()).isEqualTo(DEFAULT_COLOR);
    }

    @Test
    @Transactional
    public void createTagWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = tagRepository.findAll().size();

        // Create the Tag with an existing ID
        Tag existingTag = new Tag();
        existingTag.setId(1L);
        TagDTO existingTagDTO = tagMapper.tagToTagDTO(existingTag);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagMockMvc.perform(post("/api/tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRepository.findAll().size();
        // set the field null
        tag.setName(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.tagToTagDTO(tag);

        restTagMockMvc.perform(post("/api/tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = tagRepository.findAll().size();
        // set the field null
        tag.setColor(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.tagToTagDTO(tag);

        restTagMockMvc.perform(post("/api/tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTags() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get all the tagList
        restTagMockMvc.perform(get("/api/tags?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())));
    }

    @Test
    @Transactional
    public void getTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);

        // Get the tag
        restTagMockMvc.perform(get("/api/tags/{id}", tag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tag.getId().intValue()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTag() throws Exception {
        // Get the tag
        restTagMockMvc.perform(get("/api/tags/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Update the tag
        Tag updatedTag = tagRepository.findOne(tag.getId());
        updatedTag
                .owner(UPDATED_OWNER)
                .name(UPDATED_NAME)
                .color(UPDATED_COLOR);
        TagDTO tagDTO = tagMapper.tagToTagDTO(updatedTag);

        restTagMockMvc.perform(put("/api/tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isOk());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate);
        Tag testTag = tagList.get(tagList.size() - 1);
        assertThat(testTag.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testTag.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTag.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void updateNonExistingTag() throws Exception {
        int databaseSizeBeforeUpdate = tagRepository.findAll().size();

        // Create the Tag
        TagDTO tagDTO = tagMapper.tagToTagDTO(tag);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTagMockMvc.perform(put("/api/tags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tagDTO)))
            .andExpect(status().isCreated());

        // Validate the Tag in the database
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTag() throws Exception {
        // Initialize the database
        tagRepository.saveAndFlush(tag);
        int databaseSizeBeforeDelete = tagRepository.findAll().size();

        // Get the tag
        restTagMockMvc.perform(delete("/api/tags/{id}", tag.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tag> tagList = tagRepository.findAll();
        assertThat(tagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
