package com.example.goldenraspberry.repository;

import com.example.goldenraspberry.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByWinnerTrue();
//    @Query("SELECT m.producers, MIN(m.year) AS previousWin, MAX(m.year) AS followingWin, " +
//            "MAX(m.year) - MIN(m.year) AS interval " +
//            "FROM Movie m WHERE m.winner = true GROUP BY m.producers " +
//            "HAVING MAX(m.year) - MIN(m.year) > 0")
//    List<Object[]> findProducerWinIntervals();
}
