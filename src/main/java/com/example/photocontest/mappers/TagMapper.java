package com.example.photocontest.mappers;

import com.example.photocontest.models.Tag;
import com.example.photocontest.models.dto.TagDto;
import com.example.photocontest.services.contracts.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    private final TagService tagService;

    @Autowired
    public TagMapper(TagService tagService) {
        this.tagService = tagService;
    }

    public Tag fromDto(int id, TagDto tagDto) {
        Tag tag = fromDto(tagDto);
        tag.setId(id);
        tag.setName(tagDto.getTagName());
        return tag;
    }

    public Tag fromDto(TagDto tagDto) {
        Tag tag = new Tag();
        tag.setName(tagDto.getTagName());
        return tag;
    }
}
