package org.mymoney.accountservice.service.impl;

import org.mymoney.accountservice.service.TagService;
import org.mymoney.accountservice.domain.Tag;
import org.mymoney.accountservice.repository.TagRepository;
import org.mymoney.accountservice.service.dto.TagDTO;
import org.mymoney.accountservice.service.mapper.TagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Tag.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService{

    private final Logger log = LoggerFactory.getLogger(TagServiceImpl.class);
    
    @Inject
    private TagRepository tagRepository;

    @Inject
    private TagMapper tagMapper;

    /**
     * Save a tag.
     *
     * @param tagDTO the entity to save
     * @return the persisted entity
     */
    public TagDTO save(TagDTO tagDTO) {
        log.debug("Request to save Tag : {}", tagDTO);
        Tag tag = tagMapper.tagDTOToTag(tagDTO);
        tag = tagRepository.save(tag);
        TagDTO result = tagMapper.tagToTagDTO(tag);
        return result;
    }

    /**
     *  Get all the tags.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<TagDTO> findAll() {
        log.debug("Request to get all Tags");
        List<TagDTO> result = tagRepository.findAll().stream()
            .map(tagMapper::tagToTagDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     *  Get one tag by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TagDTO findOne(Long id) {
        log.debug("Request to get Tag : {}", id);
        Tag tag = tagRepository.findOne(id);
        TagDTO tagDTO = tagMapper.tagToTagDTO(tag);
        return tagDTO;
    }

    /**
     *  Delete the  tag by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Tag : {}", id);
        tagRepository.delete(id);
    }
}
