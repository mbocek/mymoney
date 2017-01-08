package org.mymoney.accountservice.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.mymoney.accountservice.domain.enumeration.Side;

/**
 * A DTO for the Transfer entity.
 */
public class TransferDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal ambount;

    @NotNull
    private Side side;

    @NotNull
    private String number;

    @NotNull
    private String code;

    @NotNull
    private ZonedDateTime transactionDate;


    private Long personalBankAccountId;
    
    private Long tagId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public BigDecimal getAmbount() {
        return ambount;
    }

    public void setAmbount(BigDecimal ambount) {
        this.ambount = ambount;
    }
    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public ZonedDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(ZonedDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Long getPersonalBankAccountId() {
        return personalBankAccountId;
    }

    public void setPersonalBankAccountId(Long personalBankAccountId) {
        this.personalBankAccountId = personalBankAccountId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransferDTO transferDTO = (TransferDTO) o;

        if ( ! Objects.equals(id, transferDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TransferDTO{" +
            "id=" + id +
            ", ambount='" + ambount + "'" +
            ", side='" + side + "'" +
            ", number='" + number + "'" +
            ", code='" + code + "'" +
            ", transactionDate='" + transactionDate + "'" +
            '}';
    }
}
