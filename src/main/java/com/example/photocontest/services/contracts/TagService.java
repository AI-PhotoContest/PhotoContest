package com.example.photocontest.services.contracts;


import com.example.photocontest.models.Tag;

import java.util.List;
import java.util.Set;

public interface TagService {

    List<Tag> get();

    Tag getById(int id);

    void create(Tag tag);

    void update(Tag tag);

    void delete(int id);

    Set<Tag> findOrCreateTags(Set<Tag> tags);
}
