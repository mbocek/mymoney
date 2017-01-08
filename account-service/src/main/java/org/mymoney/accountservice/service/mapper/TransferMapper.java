package org.mymoney.accountservice.service.mapper;

import org.mymoney.accountservice.domain.*;
import org.mymoney.accountservice.service.dto.TransferDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Transfer and its DTO TransferDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TransferMapper {

    @Mapping(source = "personalBankAccount.id", target = "personalBankAccountId")
    @Mapping(source = "tag.id", target = "tagId")
    TransferDTO transferToTransferDTO(Transfer transfer);

    List<TransferDTO> transfersToTransferDTOs(List<Transfer> transfers);

    @Mapping(source = "personalBankAccountId", target = "personalBankAccount")
    @Mapping(source = "tagId", target = "tag")
    Transfer transferDTOToTransfer(TransferDTO transferDTO);

    List<Transfer> transferDTOsToTransfers(List<TransferDTO> transferDTOs);

    default PersonalBankAccount personalBankAccountFromId(Long id) {
        if (id == null) {
            return null;
        }
        PersonalBankAccount personalBankAccount = new PersonalBankAccount();
        personalBankAccount.setId(id);
        return personalBankAccount;
    }

    default Tag tagFromId(Long id) {
        if (id == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setId(id);
        return tag;
    }
}
