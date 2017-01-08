package org.mymoney.accountservice.service.mapper;

import org.mymoney.accountservice.domain.*;
import org.mymoney.accountservice.service.dto.TagDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Tag and its DTO TagDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TagMapper {

    TagDTO tagToTagDTO(Tag tag);

    List<TagDTO> tagsToTagDTOs(List<Tag> tags);

    Tag tagDTOToTag(TagDTO tagDTO);

    List<Tag> tagDTOsToTags(List<TagDTO> tagDTOs);
}
