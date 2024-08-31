package com.example.photocontest.filters;


import com.example.photocontest.models.Contest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhotoPostFilterOptions {

    private String title;
    private Contest contest;

}