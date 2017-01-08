package org.mymoney.accountservice.repository;

import org.mymoney.accountservice.domain.PersonalBankAccount;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PersonalBankAccount entity.
 */
@SuppressWarnings("unused")
public interface PersonalBankAccountRepository extends JpaRepository<PersonalBankAccount,Long> {

}
