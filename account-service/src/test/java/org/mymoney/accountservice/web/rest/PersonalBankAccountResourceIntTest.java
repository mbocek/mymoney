package org.mymoney.accountservice.web.rest;

import org.mymoney.accountservice.AccountServiceApp;

import org.mymoney.accountservice.domain.PersonalBankAccount;
import org.mymoney.accountservice.repository.PersonalBankAccountRepository;
import org.mymoney.accountservice.service.PersonalBankAccountService;
import org.mymoney.accountservice.service.dto.PersonalBankAccountDTO;
import org.mymoney.accountservice.service.mapper.PersonalBankAccountMapper;

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
 * Test class for the PersonalBankAccountResource REST controller.
 *
 * @see PersonalBankAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountServiceApp.class)
public class PersonalBankAccountResourceIntTest {

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    @Inject
    private PersonalBankAccountRepository personalBankAccountRepository;

    @Inject
    private PersonalBankAccountMapper personalBankAccountMapper;

    @Inject
    private PersonalBankAccountService personalBankAccountService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPersonalBankAccountMockMvc;

    private PersonalBankAccount personalBankAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PersonalBankAccountResource personalBankAccountResource = new PersonalBankAccountResource();
        ReflectionTestUtils.setField(personalBankAccountResource, "personalBankAccountService", personalBankAccountService);
        this.restPersonalBankAccountMockMvc = MockMvcBuilders.standaloneSetup(personalBankAccountResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalBankAccount createEntity(EntityManager em) {
        PersonalBankAccount personalBankAccount = new PersonalBankAccount()
                .owner(DEFAULT_OWNER)
                .name(DEFAULT_NAME)
                .number(DEFAULT_NUMBER)
                .code(DEFAULT_CODE);
        return personalBankAccount;
    }

    @Before
    public void initTest() {
        personalBankAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createPersonalBankAccount() throws Exception {
        int databaseSizeBeforeCreate = personalBankAccountRepository.findAll().size();

        // Create the PersonalBankAccount
        PersonalBankAccountDTO personalBankAccountDTO = personalBankAccountMapper.personalBankAccountToPersonalBankAccountDTO(personalBankAccount);

        restPersonalBankAccountMockMvc.perform(post("/api/personal-bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personalBankAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the PersonalBankAccount in the database
        List<PersonalBankAccount> personalBankAccountList = personalBankAccountRepository.findAll();
        assertThat(personalBankAccountList).hasSize(databaseSizeBeforeCreate + 1);
        PersonalBankAccount testPersonalBankAccount = personalBankAccountList.get(personalBankAccountList.size() - 1);
        assertThat(testPersonalBankAccount.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testPersonalBankAccount.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPersonalBankAccount.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testPersonalBankAccount.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createPersonalBankAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personalBankAccountRepository.findAll().size();

        // Create the PersonalBankAccount with an existing ID
        PersonalBankAccount existingPersonalBankAccount = new PersonalBankAccount();
        existingPersonalBankAccount.setId(1L);
        PersonalBankAccountDTO existingPersonalBankAccountDTO = personalBankAccountMapper.personalBankAccountToPersonalBankAccountDTO(existingPersonalBankAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalBankAccountMockMvc.perform(post("/api/personal-bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingPersonalBankAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<PersonalBankAccount> personalBankAccountList = personalBankAccountRepository.findAll();
        assertThat(personalBankAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalBankAccountRepository.findAll().size();
        // set the field null
        personalBankAccount.setOwner(null);

        // Create the PersonalBankAccount, which fails.
        PersonalBankAccountDTO personalBankAccountDTO = personalBankAccountMapper.personalBankAccountToPersonalBankAccountDTO(personalBankAccount);

        restPersonalBankAccountMockMvc.perform(post("/api/personal-bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personalBankAccountDTO)))
            .andExpect(status().isBadRequest());

        List<PersonalBankAccount> personalBankAccountList = personalBankAccountRepository.findAll();
        assertThat(personalBankAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalBankAccountRepository.findAll().size();
        // set the field null
        personalBankAccount.setName(null);

        // Create the PersonalBankAccount, which fails.
        PersonalBankAccountDTO personalBankAccountDTO = personalBankAccountMapper.personalBankAccountToPersonalBankAccountDTO(personalBankAccount);

        restPersonalBankAccountMockMvc.perform(post("/api/personal-bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personalBankAccountDTO)))
            .andExpect(status().isBadRequest());

        List<PersonalBankAccount> personalBankAccountList = personalBankAccountRepository.findAll();
        assertThat(personalBankAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalBankAccountRepository.findAll().size();
        // set the field null
        personalBankAccount.setNumber(null);

        // Create the PersonalBankAccount, which fails.
        PersonalBankAccountDTO personalBankAccountDTO = personalBankAccountMapper.personalBankAccountToPersonalBankAccountDTO(personalBankAccount);

        restPersonalBankAccountMockMvc.perform(post("/api/personal-bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personalBankAccountDTO)))
            .andExpect(status().isBadRequest());

        List<PersonalBankAccount> personalBankAccountList = personalBankAccountRepository.findAll();
        assertThat(personalBankAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalBankAccountRepository.findAll().size();
        // set the field null
        personalBankAccount.setCode(null);

        // Create the PersonalBankAccount, which fails.
        PersonalBankAccountDTO personalBankAccountDTO = personalBankAccountMapper.personalBankAccountToPersonalBankAccountDTO(personalBankAccount);

        restPersonalBankAccountMockMvc.perform(post("/api/personal-bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personalBankAccountDTO)))
            .andExpect(status().isBadRequest());

        List<PersonalBankAccount> personalBankAccountList = personalBankAccountRepository.findAll();
        assertThat(personalBankAccountList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPersonalBankAccounts() throws Exception {
        // Initialize the database
        personalBankAccountRepository.saveAndFlush(personalBankAccount);

        // Get all the personalBankAccountList
        restPersonalBankAccountMockMvc.perform(get("/api/personal-bank-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalBankAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getPersonalBankAccount() throws Exception {
        // Initialize the database
        personalBankAccountRepository.saveAndFlush(personalBankAccount);

        // Get the personalBankAccount
        restPersonalBankAccountMockMvc.perform(get("/api/personal-bank-accounts/{id}", personalBankAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(personalBankAccount.getId().intValue()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPersonalBankAccount() throws Exception {
        // Get the personalBankAccount
        restPersonalBankAccountMockMvc.perform(get("/api/personal-bank-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonalBankAccount() throws Exception {
        // Initialize the database
        personalBankAccountRepository.saveAndFlush(personalBankAccount);
        int databaseSizeBeforeUpdate = personalBankAccountRepository.findAll().size();

        // Update the personalBankAccount
        PersonalBankAccount updatedPersonalBankAccount = personalBankAccountRepository.findOne(personalBankAccount.getId());
        updatedPersonalBankAccount
                .owner(UPDATED_OWNER)
                .name(UPDATED_NAME)
                .number(UPDATED_NUMBER)
                .code(UPDATED_CODE);
        PersonalBankAccountDTO personalBankAccountDTO = personalBankAccountMapper.personalBankAccountToPersonalBankAccountDTO(updatedPersonalBankAccount);

        restPersonalBankAccountMockMvc.perform(put("/api/personal-bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personalBankAccountDTO)))
            .andExpect(status().isOk());

        // Validate the PersonalBankAccount in the database
        List<PersonalBankAccount> personalBankAccountList = personalBankAccountRepository.findAll();
        assertThat(personalBankAccountList).hasSize(databaseSizeBeforeUpdate);
        PersonalBankAccount testPersonalBankAccount = personalBankAccountList.get(personalBankAccountList.size() - 1);
        assertThat(testPersonalBankAccount.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testPersonalBankAccount.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPersonalBankAccount.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testPersonalBankAccount.getCode()).isEqualTo(UPDATED_CODE);
    }

    @Test
    @Transactional
    public void updateNonExistingPersonalBankAccount() throws Exception {
        int databaseSizeBeforeUpdate = personalBankAccountRepository.findAll().size();

        // Create the PersonalBankAccount
        PersonalBankAccountDTO personalBankAccountDTO = personalBankAccountMapper.personalBankAccountToPersonalBankAccountDTO(personalBankAccount);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPersonalBankAccountMockMvc.perform(put("/api/personal-bank-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personalBankAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the PersonalBankAccount in the database
        List<PersonalBankAccount> personalBankAccountList = personalBankAccountRepository.findAll();
        assertThat(personalBankAccountList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePersonalBankAccount() throws Exception {
        // Initialize the database
        personalBankAccountRepository.saveAndFlush(personalBankAccount);
        int databaseSizeBeforeDelete = personalBankAccountRepository.findAll().size();

        // Get the personalBankAccount
        restPersonalBankAccountMockMvc.perform(delete("/api/personal-bank-accounts/{id}", personalBankAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PersonalBankAccount> personalBankAccountList = personalBankAccountRepository.findAll();
        assertThat(personalBankAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
