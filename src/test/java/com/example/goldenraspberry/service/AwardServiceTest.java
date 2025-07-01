package com.example.goldenraspberry.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.example.goldenraspberry.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class AwardServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Test
    void shouldReturnEmptyListWhenNoAwards() {
        AwardService awardService = new AwardService(movieRepository);
        when(movieRepository.findAll()).thenReturn(Collections.emptyList());

        Map<String, List<Map<String, Object>>> result = awardService.obterPremiosIntervalo();

        assertTrue(result.get("min").isEmpty());
        assertTrue(result.get("max").isEmpty());
    }

    @Test
    void shouldThrowExceptionForInvalidInputParameters() {
        AwardService awardService = new AwardService(null);

        assertThrows(NullPointerException.class, () -> {
            awardService.parseAndSaveMovies(null);
        });
    }
}
