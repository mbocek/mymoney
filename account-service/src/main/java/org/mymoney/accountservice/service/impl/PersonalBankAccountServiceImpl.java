package org.mymoney.accountservice.service.impl;

import org.mymoney.accountservice.service.PersonalBankAccountService;
import org.mymoney.accountservice.domain.PersonalBankAccount;
import org.mymoney.accountservice.repository.PersonalBankAccountRepository;
import org.mymoney.accountservice.service.dto.PersonalBankAccountDTO;
import org.mymoney.accountservice.service.mapper.PersonalBankAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PersonalBankAccount.
 */
@Service
@Transactional
public class PersonalBankAccountServiceImpl implements PersonalBankAccountService{

    private final Logger log = LoggerFactory.getLogger(PersonalBankAccountServiceImpl.class);
    
    @Inject
    private PersonalBankAccountRepository personalBankAccountRepository;

    @Inject
    private PersonalBankAccountMapper personalBankAccountMapper;

    /**
     * Save a personalBankAccount.
     *
     * @param personalBankAccountDTO the entity to save
     * @return the persisted entity
     */
    public PersonalBankAccountDTO save(PersonalBankAccountDTO personalBankAccountDTO) {
        log.debug("Request to save PersonalBankAccount : {}", personalBankAccountDTO);
        PersonalBankAccount personalBankAccount = personalBankAccountMapper.personalBankAccountDTOToPersonalBankAccount(personalBankAccountDTO);
        personalBankAccount = personalBankAccountRepository.save(personalBankAccount);
        PersonalBankAccountDTO result = personalBankAccountMapper.personalBankAccountToPersonalBankAccountDTO(personalBankAccount);
        return result;
    }

    /**
     *  Get all the personalBankAccounts.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<PersonalBankAccountDTO> findAll() {
        log.debug("Request to get all PersonalBankAccounts");
        List<PersonalBankAccountDTO> result = personalBankAccountRepository.findAll().stream()
            .map(personalBankAccountMapper::personalBankAccountToPersonalBankAccountDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one personalBankAccount by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PersonalBankAccountDTO findOne(Long id) {
        log.debug("Request to get PersonalBankAccount : {}", id);
        PersonalBankAccount personalBankAccount = personalBankAccountRepository.findOne(id);
        PersonalBankAccountDTO personalBankAccountDTO = personalBankAccountMapper.personalBankAccountToPersonalBankAccountDTO(personalBankAccount);
        return personalBankAccountDTO;
    }

    /**
     *  Delete the  personalBankAccount by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PersonalBankAccount : {}", id);
        personalBankAccountRepository.delete(id);
    }
}
