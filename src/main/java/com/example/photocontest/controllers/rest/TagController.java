package com.example.photocontest.controllers.rest;


import com.example.photocontest.exceptions.AuthorizationException;
import com.example.photocontest.exceptions.EntityDuplicateException;
import com.example.photocontest.exceptions.EntityNotFoundException;
import com.example.photocontest.mappers.TagMapper;
import com.example.photocontest.models.Tag;
import com.example.photocontest.models.dto.TagDto;
import com.example.photocontest.services.contracts.TagService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    public TagController(TagService tagService, TagMapper tagMapper) {
        this.tagService = tagService;
        this.tagMapper = tagMapper;
    }

    @GetMapping
    public List<Tag> findAll(){
        return tagService.get();
    }

    @GetMapping("/{id}")
    public Tag get(@PathVariable int id) {
        try {
            return tagService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public Tag create(@Valid @RequestBody TagDto tagDto) {
        try {
            Tag tag = tagMapper.fromDto(tagDto);
            tagService.create(tag);
            return tag;
        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Tag update(@PathVariable int id, @Valid @RequestBody TagDto tagDto) {
        try {
            Tag tag = tagMapper.fromDto(id, tagDto);
            tagService.update(tag);
            return tag;
        }catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        try {
            tagService.delete(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthorizationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
