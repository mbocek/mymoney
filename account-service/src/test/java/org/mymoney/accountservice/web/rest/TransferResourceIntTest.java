package org.mymoney.accountservice.web.rest;

import org.mymoney.accountservice.AccountServiceApp;

import org.mymoney.accountservice.domain.Transfer;
import org.mymoney.accountservice.repository.TransferRepository;
import org.mymoney.accountservice.service.TransferService;
import org.mymoney.accountservice.service.dto.TransferDTO;
import org.mymoney.accountservice.service.mapper.TransferMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.mymoney.accountservice.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.mymoney.accountservice.domain.enumeration.Side;
/**
 * Test class for the TransferResource REST controller.
 *
 * @see TransferResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountServiceApp.class)
public class TransferResourceIntTest {

    private static final BigDecimal DEFAULT_AMBOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMBOUNT = new BigDecimal(2);

    private static final Side DEFAULT_SIDE = Side.FROM;
    private static final Side UPDATED_SIDE = Side.TO;

    private static final String DEFAULT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TRANSACTION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TRANSACTION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Inject
    private TransferRepository transferRepository;

    @Inject
    private TransferMapper transferMapper;

    @Inject
    private TransferService transferService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTransferMockMvc;

    private Transfer transfer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransferResource transferResource = new TransferResource();
        ReflectionTestUtils.setField(transferResource, "transferService", transferService);
        this.restTransferMockMvc = MockMvcBuilders.standaloneSetup(transferResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transfer createEntity(EntityManager em) {
        Transfer transfer = new Transfer()
                .ambount(DEFAULT_AMBOUNT)
                .side(DEFAULT_SIDE)
                .number(DEFAULT_NUMBER)
                .code(DEFAULT_CODE)
                .transactionDate(DEFAULT_TRANSACTION_DATE);
        return transfer;
    }

    @Before
    public void initTest() {
        transfer = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransfer() throws Exception {
        int databaseSizeBeforeCreate = transferRepository.findAll().size();

        // Create the Transfer
        TransferDTO transferDTO = transferMapper.transferToTransferDTO(transfer);

        restTransferMockMvc.perform(post("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isCreated());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeCreate + 1);
        Transfer testTransfer = transferList.get(transferList.size() - 1);
        assertThat(testTransfer.getAmbount()).isEqualTo(DEFAULT_AMBOUNT);
        assertThat(testTransfer.getSide()).isEqualTo(DEFAULT_SIDE);
        assertThat(testTransfer.getNumber()).isEqualTo(DEFAULT_NUMBER);
        assertThat(testTransfer.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testTransfer.getTransactionDate()).isEqualTo(DEFAULT_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void createTransferWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = transferRepository.findAll().size();

        // Create the Transfer with an existing ID
        Transfer existingTransfer = new Transfer();
        existingTransfer.setId(1L);
        TransferDTO existingTransferDTO = transferMapper.transferToTransferDTO(existingTransfer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransferMockMvc.perform(post("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingTransferDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAmbountIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferRepository.findAll().size();
        // set the field null
        transfer.setAmbount(null);

        // Create the Transfer, which fails.
        TransferDTO transferDTO = transferMapper.transferToTransferDTO(transfer);

        restTransferMockMvc.perform(post("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isBadRequest());

        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSideIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferRepository.findAll().size();
        // set the field null
        transfer.setSide(null);

        // Create the Transfer, which fails.
        TransferDTO transferDTO = transferMapper.transferToTransferDTO(transfer);

        restTransferMockMvc.perform(post("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isBadRequest());

        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferRepository.findAll().size();
        // set the field null
        transfer.setNumber(null);

        // Create the Transfer, which fails.
        TransferDTO transferDTO = transferMapper.transferToTransferDTO(transfer);

        restTransferMockMvc.perform(post("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isBadRequest());

        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferRepository.findAll().size();
        // set the field null
        transfer.setCode(null);

        // Create the Transfer, which fails.
        TransferDTO transferDTO = transferMapper.transferToTransferDTO(transfer);

        restTransferMockMvc.perform(post("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isBadRequest());

        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferRepository.findAll().size();
        // set the field null
        transfer.setTransactionDate(null);

        // Create the Transfer, which fails.
        TransferDTO transferDTO = transferMapper.transferToTransferDTO(transfer);

        restTransferMockMvc.perform(post("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isBadRequest());

        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransfers() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);

        // Get all the transferList
        restTransferMockMvc.perform(get("/api/transfers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].ambount").value(hasItem(DEFAULT_AMBOUNT.intValue())))
            .andExpect(jsonPath("$.[*].side").value(hasItem(DEFAULT_SIDE.toString())))
            .andExpect(jsonPath("$.[*].number").value(hasItem(DEFAULT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(sameInstant(DEFAULT_TRANSACTION_DATE))));
    }

    @Test
    @Transactional
    public void getTransfer() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);

        // Get the transfer
        restTransferMockMvc.perform(get("/api/transfers/{id}", transfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transfer.getId().intValue()))
            .andExpect(jsonPath("$.ambount").value(DEFAULT_AMBOUNT.intValue()))
            .andExpect(jsonPath("$.side").value(DEFAULT_SIDE.toString()))
            .andExpect(jsonPath("$.number").value(DEFAULT_NUMBER.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.transactionDate").value(sameInstant(DEFAULT_TRANSACTION_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingTransfer() throws Exception {
        // Get the transfer
        restTransferMockMvc.perform(get("/api/transfers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransfer() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);
        int databaseSizeBeforeUpdate = transferRepository.findAll().size();

        // Update the transfer
        Transfer updatedTransfer = transferRepository.findOne(transfer.getId());
        updatedTransfer
                .ambount(UPDATED_AMBOUNT)
                .side(UPDATED_SIDE)
                .number(UPDATED_NUMBER)
                .code(UPDATED_CODE)
                .transactionDate(UPDATED_TRANSACTION_DATE);
        TransferDTO transferDTO = transferMapper.transferToTransferDTO(updatedTransfer);

        restTransferMockMvc.perform(put("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isOk());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeUpdate);
        Transfer testTransfer = transferList.get(transferList.size() - 1);
        assertThat(testTransfer.getAmbount()).isEqualTo(UPDATED_AMBOUNT);
        assertThat(testTransfer.getSide()).isEqualTo(UPDATED_SIDE);
        assertThat(testTransfer.getNumber()).isEqualTo(UPDATED_NUMBER);
        assertThat(testTransfer.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testTransfer.getTransactionDate()).isEqualTo(UPDATED_TRANSACTION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTransfer() throws Exception {
        int databaseSizeBeforeUpdate = transferRepository.findAll().size();

        // Create the Transfer
        TransferDTO transferDTO = transferMapper.transferToTransferDTO(transfer);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTransferMockMvc.perform(put("/api/transfers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(transferDTO)))
            .andExpect(status().isCreated());

        // Validate the Transfer in the database
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTransfer() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);
        int databaseSizeBeforeDelete = transferRepository.findAll().size();

        // Get the transfer
        restTransferMockMvc.perform(delete("/api/transfers/{id}", transfer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Transfer> transferList = transferRepository.findAll();
        assertThat(transferList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
