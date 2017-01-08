package org.mymoney.accountservice.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Tag entity.
 */
public class TagDTO implements Serializable {

    private Long id;

    private String owner;

    @NotNull
    private String name;

    @NotNull
    private String color;


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
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TagDTO tagDTO = (TagDTO) o;

        if ( ! Objects.equals(id, tagDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TagDTO{" +
            "id=" + id +
            ", owner='" + owner + "'" +
            ", name='" + name + "'" +
            ", color='" + color + "'" +
            '}';
    }
}
