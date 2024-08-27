package com.example.photocontest.models.enums;

public enum Role {
    ORGANIZER,
    JUNKIE,
    ENTHUSIAST,
    MASTER,
    WISEANDBENEVOLENTPHOTODICTATOR;

    public String toString(){
        switch(this){
            case ORGANIZER:
                return "Organizer";
            case JUNKIE:
                return "Junkie";
            case ENTHUSIAST:
                return "Enthusiast";
            case MASTER:
                return "Master";
            case WISEANDBENEVOLENTPHOTODICTATOR:
                 return "WiseAndBenevolentPhotoDictator";
            default:
                throw new IllegalArgumentException("Unexpected value: " + this);
        }
    }
}
