package org.mymoney.accountservice.service.mapper;

import org.mymoney.accountservice.domain.*;
import org.mymoney.accountservice.service.dto.PersonalBankAccountDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity PersonalBankAccount and its DTO PersonalBankAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PersonalBankAccountMapper {

    PersonalBankAccountDTO personalBankAccountToPersonalBankAccountDTO(PersonalBankAccount personalBankAccount);

    List<PersonalBankAccountDTO> personalBankAccountsToPersonalBankAccountDTOs(List<PersonalBankAccount> personalBankAccounts);

    @Mapping(target = "transfers", ignore = true)
    PersonalBankAccount personalBankAccountDTOToPersonalBankAccount(PersonalBankAccountDTO personalBankAccountDTO);

    List<PersonalBankAccount> personalBankAccountDTOsToPersonalBankAccounts(List<PersonalBankAccountDTO> personalBankAccountDTOs);
}
