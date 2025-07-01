package com.example.goldenraspberry.service;

import com.example.goldenraspberry.model.Movie;
import com.example.goldenraspberry.model.MovieDTO;
import com.example.goldenraspberry.model.mapper.MovieMapper;
import com.example.goldenraspberry.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AwardService {

    private static final Logger logger = LoggerFactory.getLogger(AwardService.class);
    private static final String MIN_KEY = "min";
    private static final String MAX_KEY = "max";
    private final MovieRepository movieRepository;


    public Map<String, List<Map<String, Object>>> parseAndSaveMovies(MultipartFile file) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] nextLine = line.split(";");
                if (nextLine.length < 5) {
                    logger.info("Skipping line with less than 5 columns: {}", line);
                }
                var movieDTO = MovieDTO.builder()
                        .year(Integer.parseInt(nextLine[0]))
                        .title(nextLine[1] != null && !nextLine[1].isEmpty() ? nextLine[1] : "")
                        .studios(nextLine[2] != null && !nextLine[2].isEmpty() ? nextLine[2] : "")
                        .producers(nextLine[3] != null && !nextLine[3].isEmpty() ? nextLine[3] : "")
                        .winner(nextLine.length > 4 && nextLine[4] != null && !nextLine[4].isEmpty() ? nextLine[4] : "")
                        .build();
                var movie = MovieMapper.passAndMapToEntity(movieDTO);
                movieRepository.saveAndFlush(movie);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + e.getMessage());
        }
        return obterPremiosIntervalo();
    }

    public Map<String, List<Map<String, Object>>> obterPremiosIntervalo() {
        List<Movie> allMovies = movieRepository.findAll();

        Map<String, List<Movie>> filmsByProducer = allMovies.stream()
                .collect(Collectors.groupingBy(Movie::getProducers));

        List<Map<String, Object>> minIntervals = new ArrayList<>();
        List<Map<String, Object>> maxIntervals = new ArrayList<>();

        for (String producer : filmsByProducer.keySet()) {
            List<Movie> films = filmsByProducer.get(producer);

            if (films.size() < 2) continue;

            for (int i = 1; i < films.size(); i++) {
                Movie previousFilm = films.get(i - 1);
                Movie currentFilm = films.get(i);

                int interval = currentFilm.getYear() - previousFilm.getYear();

                Map<String, Object> intervalData = new HashMap<>();
                intervalData.put("producer", producer);
                intervalData.put("interval", interval);
                intervalData.put("previousWin", previousFilm.getYear());
                intervalData.put("followingWin", currentFilm.getYear());

                if (interval == getMinInterval(films)) {
                    minIntervals.add(intervalData);
                }

                if (interval == getMaxInterval(films)) {
                    maxIntervals.add(intervalData);
                }
            }
        }

        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        result.put("min", minIntervals);
        result.put("max", maxIntervals);

        return result;
    }

    private int getMinInterval(List<Movie> movies) {
        int minInterval = Integer.MAX_VALUE;
        for (int i = 1; i < movies.size(); i++) {
            int interval = movies.get(i).getYear() - movies.get(i - 1).getYear();
            minInterval = Math.min(minInterval, interval);
        }
        return minInterval;
    }

    private int getMaxInterval(List<Movie> movies) {
        int maxInterval = Integer.MIN_VALUE;
        for (int i = 1; i < movies.size(); i++) {
            int interval = movies.get(i).getYear() - movies.get(i - 1).getYear();
            maxInterval = Math.max(maxInterval, interval);
        }
        return maxInterval;
    }
}