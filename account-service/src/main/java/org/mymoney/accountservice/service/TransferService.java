package org.mymoney.accountservice.service;

import org.mymoney.accountservice.service.dto.TransferDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Service Interface for managing Transfer.
 */
public interface TransferService {

    /**
     * Save a transfer.
     *
     * @param transferDTO the entity to save
     * @return the persisted entity
     */
    TransferDTO save(TransferDTO transferDTO);

    /**
     *  Get all the transfers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<TransferDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" transfer.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    TransferDTO findOne(Long id);

    /**
     *  Delete the "id" transfer.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
