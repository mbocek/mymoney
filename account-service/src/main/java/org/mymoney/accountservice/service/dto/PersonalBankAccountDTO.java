package org.mymoney.accountservice.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the PersonalBankAccount entity.
 */
public class PersonalBankAccountDTO implements Serializable {

    private Long id;

    @NotNull
    private String owner;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    private String number;

    @NotNull
    private String code;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersonalBankAccountDTO personalBankAccountDTO = (PersonalBankAccountDTO) o;

        if ( ! Objects.equals(id, personalBankAccountDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PersonalBankAccountDTO{" +
            "id=" + id +
            ", owner='" + owner + "'" +
            ", name='" + name + "'" +
            ", number='" + number + "'" +
            ", code='" + code + "'" +
            '}';
    }
}
