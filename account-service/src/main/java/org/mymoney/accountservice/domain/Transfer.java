package org.mymoney.accountservice.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

import org.mymoney.accountservice.domain.enumeration.Side;

/**
 * A Transfer.
 */
@Entity
@Table(name = "transfer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "ambount", precision=10, scale=2, nullable = false)
    private BigDecimal ambount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "side", nullable = false)
    private Side side;

    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private ZonedDateTime transactionDate;

    @ManyToOne
    private PersonalBankAccount personalBankAccount;

    @ManyToOne
    private Tag tag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmbount() {
        return ambount;
    }

    public Transfer ambount(BigDecimal ambount) {
        this.ambount = ambount;
        return this;
    }

    public void setAmbount(BigDecimal ambount) {
        this.ambount = ambount;
    }

    public Side getSide() {
        return side;
    }

    public Transfer side(Side side) {
        this.side = side;
        return this;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public String getNumber() {
        return number;
    }

    public Transfer number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public Transfer code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ZonedDateTime getTransactionDate() {
        return transactionDate;
    }

    public Transfer transactionDate(ZonedDateTime transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public void setTransactionDate(ZonedDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public PersonalBankAccount getPersonalBankAccount() {
        return personalBankAccount;
    }

    public Transfer personalBankAccount(PersonalBankAccount personalBankAccount) {
        this.personalBankAccount = personalBankAccount;
        return this;
    }

    public void setPersonalBankAccount(PersonalBankAccount personalBankAccount) {
        this.personalBankAccount = personalBankAccount;
    }

    public Tag getTag() {
        return tag;
    }

    public Transfer tag(Tag tag) {
        this.tag = tag;
        return this;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Transfer transfer = (Transfer) o;
        if (transfer.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, transfer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Transfer{" +
            "id=" + id +
            ", ambount='" + ambount + "'" +
            ", side='" + side + "'" +
            ", number='" + number + "'" +
            ", code='" + code + "'" +
            ", transactionDate='" + transactionDate + "'" +
            '}';
    }
}
