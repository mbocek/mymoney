package org.mymoney.accountservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PersonalBankAccount.
 */
@Entity
@Table(name = "personal_bank_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PersonalBankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "owner", nullable = false)
    private String owner;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotNull
    @Column(name = "number", nullable = false)
    private String number;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @OneToMany(mappedBy = "personalBankAccount")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Transfer> transfers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public PersonalBankAccount owner(String owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public PersonalBankAccount name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public PersonalBankAccount number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public PersonalBankAccount code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Set<Transfer> getTransfers() {
        return transfers;
    }

    public PersonalBankAccount transfers(Set<Transfer> transfers) {
        this.transfers = transfers;
        return this;
    }

    public PersonalBankAccount addTransfers(Transfer transfer) {
        transfers.add(transfer);
        transfer.setPersonalBankAccount(this);
        return this;
    }

    public PersonalBankAccount removeTransfers(Transfer transfer) {
        transfers.remove(transfer);
        transfer.setPersonalBankAccount(null);
        return this;
    }

    public void setTransfers(Set<Transfer> transfers) {
        this.transfers = transfers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersonalBankAccount personalBankAccount = (PersonalBankAccount) o;
        if (personalBankAccount.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, personalBankAccount.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PersonalBankAccount{" +
            "id=" + id +
            ", owner='" + owner + "'" +
            ", name='" + name + "'" +
            ", number='" + number + "'" +
            ", code='" + code + "'" +
            '}';
    }
}
