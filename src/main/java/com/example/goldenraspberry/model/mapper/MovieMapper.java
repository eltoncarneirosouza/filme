package com.example.goldenraspberry.model.mapper;

import com.example.goldenraspberry.model.Movie;
import com.example.goldenraspberry.model.MovieDTO;


public class MovieMapper {
    public static Movie passAndMapToEntity(MovieDTO movieDTO) {
        return Movie.builder()
                .year(movieDTO.getYear())
                .title(movieDTO.getTitle())
                .studios(movieDTO.getStudios())
                .producers(movieDTO.getProducers())
                .winner(movieDTO.getWinner())
                .build();
    }
}