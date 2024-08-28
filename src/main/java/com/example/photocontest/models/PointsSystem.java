package com.example.photocontest.models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "points_system")
public class PointsSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int points;

    public String toString() {
        String result;
        if (points >= 0 && points <= 50) {
            result = "Junkie";
        } else if (points <= 150) {
            result = "Enthusiast";
        } else if (points <= 1000) {
            result = "Master";
        } else if (points > 1000) {
            result = "Wise and Benevolent Photodictator";
        } else {
            throw new IllegalArgumentException("Unexpected value: " + this);
        }
        return result;
    }

}
