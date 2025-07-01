package com.example.goldenraspberry.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieDTO {
    private int year;
    private String title;
    private String studios;
    private String producers;
    private String winner;
}
