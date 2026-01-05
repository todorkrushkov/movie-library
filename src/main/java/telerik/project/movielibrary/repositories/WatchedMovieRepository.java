package telerik.project.movielibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telerik.project.movielibrary.models.WatchedMovie;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchedMovieRepository extends JpaRepository<WatchedMovie, Long> {

    boolean existsByUserIdAndMovieId(Long userId, Long movieId);

    Optional<WatchedMovie> findByUserIdAndMovieId(Long userId, Long movieId);

    List<WatchedMovie> findAllByUserId(Long userId);
}
