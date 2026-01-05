package telerik.project.movielibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import telerik.project.movielibrary.models.Movie;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("""
            SELECT m FROM Movie m
            WHERE (:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')))
              AND (:director IS NULL OR LOWER(m.director) LIKE LOWER(CONCAT('%', :director, '%')))
              AND (:dateFrom IS NULL OR m.releaseDate >= :dateFrom)
              AND (:dateTo IS NULL OR m.releaseDate <= :dateTo)
              AND (:ratingMin IS NULL OR m.rating >= :ratingMin)
              AND (:ratingMax IS NULL OR m.rating <= :ratingMax)
    """)
    List<Movie> search(
            @Param("title") String title,
            @Param("director") String director,
            @Param("dateFrom") LocalDate dateFrom,
            @Param("dateTo") LocalDate dateTo,
            @Param("ratingMin") Double ratingMin,
            @Param("ratingMax") Double ratingMax
    );

    boolean existsByTitle(String title);
}
