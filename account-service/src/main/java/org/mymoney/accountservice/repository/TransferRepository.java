package org.mymoney.accountservice.repository;

import org.mymoney.accountservice.domain.Transfer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Transfer entity.
 */
@SuppressWarnings("unused")
public interface TransferRepository extends JpaRepository<Transfer,Long> {

}
