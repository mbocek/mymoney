package org.mymoney.accountservice.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.mymoney.accountservice.service.TagService;
import org.mymoney.accountservice.web.rest.util.HeaderUtil;
import org.mymoney.accountservice.service.dto.TagDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Tag.
 */
@RestController
@RequestMapping("/api")
public class TagResource {

    private final Logger log = LoggerFactory.getLogger(TagResource.class);
        
    @Inject
    private TagService tagService;

    /**
     * POST  /tags : Create a new tag.
     *
     * @param tagDTO the tagDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tagDTO, or with status 400 (Bad Request) if the tag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tags")
    @Timed
    public ResponseEntity<TagDTO> createTag(@Valid @RequestBody TagDTO tagDTO) throws URISyntaxException {
        log.debug("REST request to save Tag : {}", tagDTO);
        if (tagDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tag", "idexists", "A new tag cannot already have an ID")).body(null);
        }
        TagDTO result = tagService.save(tagDTO);
        return ResponseEntity.created(new URI("/api/tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tag", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tags : Updates an existing tag.
     *
     * @param tagDTO the tagDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tagDTO,
     * or with status 400 (Bad Request) if the tagDTO is not valid,
     * or with status 500 (Internal Server Error) if the tagDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tags")
    @Timed
    public ResponseEntity<TagDTO> updateTag(@Valid @RequestBody TagDTO tagDTO) throws URISyntaxException {
        log.debug("REST request to update Tag : {}", tagDTO);
        if (tagDTO.getId() == null) {
            return createTag(tagDTO);
        }
        TagDTO result = tagService.save(tagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tag", tagDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tags : get all the tags.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tags in body
     */
    @GetMapping("/tags")
    @Timed
    public List<TagDTO> getAllTags() {
        log.debug("REST request to get all Tags");
        return tagService.findAll();
    }

    /**
     * GET  /tags/:id : get the "id" tag.
     *
     * @param id the id of the tagDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tagDTO, or with status 404 (Not Found)
     */
    @GetMapping("/tags/{id}")
    @Timed
    public ResponseEntity<TagDTO> getTag(@PathVariable Long id) {
        log.debug("REST request to get Tag : {}", id);
        TagDTO tagDTO = tagService.findOne(id);
        return Optional.ofNullable(tagDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tags/:id : delete the "id" tag.
     *
     * @param id the id of the tagDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tags/{id}")
    @Timed
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        log.debug("REST request to delete Tag : {}", id);
        tagService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tag", id.toString())).build();
    }

}
