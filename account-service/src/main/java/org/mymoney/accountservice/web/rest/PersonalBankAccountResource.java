package org.mymoney.accountservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mymoney.accountservice.service.PersonalBankAccountService;
import org.mymoney.accountservice.web.rest.util.HeaderUtil;
import org.mymoney.accountservice.service.dto.PersonalBankAccountDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing PersonalBankAccount.
 */
@RestController
@RequestMapping("/api")
public class PersonalBankAccountResource {

    private final Logger log = LoggerFactory.getLogger(PersonalBankAccountResource.class);
        
    @Inject
    private PersonalBankAccountService personalBankAccountService;

    /**
     * POST  /personal-bank-accounts : Create a new personalBankAccount.
     *
     * @param personalBankAccountDTO the personalBankAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new personalBankAccountDTO, or with status 400 (Bad Request) if the personalBankAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/personal-bank-accounts")
    @Timed
    public ResponseEntity<PersonalBankAccountDTO> createPersonalBankAccount(@Valid @RequestBody PersonalBankAccountDTO personalBankAccountDTO) throws URISyntaxException {
        log.debug("REST request to save PersonalBankAccount : {}", personalBankAccountDTO);
        if (personalBankAccountDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("personalBankAccount", "idexists", "A new personalBankAccount cannot already have an ID")).body(null);
        }
        PersonalBankAccountDTO result = personalBankAccountService.save(personalBankAccountDTO);
        return ResponseEntity.created(new URI("/api/personal-bank-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("personalBankAccount", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /personal-bank-accounts : Updates an existing personalBankAccount.
     *
     * @param personalBankAccountDTO the personalBankAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated personalBankAccountDTO,
     * or with status 400 (Bad Request) if the personalBankAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the personalBankAccountDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/personal-bank-accounts")
    @Timed
    public ResponseEntity<PersonalBankAccountDTO> updatePersonalBankAccount(@Valid @RequestBody PersonalBankAccountDTO personalBankAccountDTO) throws URISyntaxException {
        log.debug("REST request to update PersonalBankAccount : {}", personalBankAccountDTO);
        if (personalBankAccountDTO.getId() == null) {
            return createPersonalBankAccount(personalBankAccountDTO);
        }
        PersonalBankAccountDTO result = personalBankAccountService.save(personalBankAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("personalBankAccount", personalBankAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /personal-bank-accounts : get all the personalBankAccounts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of personalBankAccounts in body
     */
    @GetMapping("/personal-bank-accounts")
    @Timed
    public List<PersonalBankAccountDTO> getAllPersonalBankAccounts() {
        log.debug("REST request to get all PersonalBankAccounts");
        return personalBankAccountService.findAll();
    }

    /**
     * GET  /personal-bank-accounts/:id : get the "id" personalBankAccount.
     *
     * @param id the id of the personalBankAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the personalBankAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/personal-bank-accounts/{id}")
    @Timed
    public ResponseEntity<PersonalBankAccountDTO> getPersonalBankAccount(@PathVariable Long id) {
        log.debug("REST request to get PersonalBankAccount : {}", id);
        PersonalBankAccountDTO personalBankAccountDTO = personalBankAccountService.findOne(id);
        return Optional.ofNullable(personalBankAccountDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /personal-bank-accounts/:id : delete the "id" personalBankAccount.
     *
     * @param id the id of the personalBankAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/personal-bank-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deletePersonalBankAccount(@PathVariable Long id) {
        log.debug("REST request to delete PersonalBankAccount : {}", id);
        personalBankAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("personalBankAccount", id.toString())).build();
    }

}
