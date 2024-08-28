package com.example.photocontest.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "camera_details")
public class CameraDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "camera_make")
    private String cameraModel;
    @Column(name = "camera_model")
    private String lensMake;
    @Column(name = "lens_model")
    private String lensModel;
    @Column(name = "shutter_speed")
    private String shutterSpeed;
    private String aperture;
    @Column(name = "focal_length")
    private String focalLength;
    private String iso;

    @OneToOne
    @JoinColumn(name = "photo_post_id")
    private PhotoPost photoPost;


}
