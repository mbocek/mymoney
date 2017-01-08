package org.mymoney.accountservice.service;

import org.mymoney.accountservice.service.dto.PersonalBankAccountDTO;
import java.util.List;

/**
 * Service Interface for managing PersonalBankAccount.
 */
public interface PersonalBankAccountService {

    /**
     * Save a personalBankAccount.
     *
     * @param personalBankAccountDTO the entity to save
     * @return the persisted entity
     */
    PersonalBankAccountDTO save(PersonalBankAccountDTO personalBankAccountDTO);

    /**
     *  Get all the personalBankAccounts.
     *  
     *  @return the list of entities
     */
    List<PersonalBankAccountDTO> findAll();

    /**
     *  Get the "id" personalBankAccount.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PersonalBankAccountDTO findOne(Long id);

    /**
     *  Delete the "id" personalBankAccount.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
