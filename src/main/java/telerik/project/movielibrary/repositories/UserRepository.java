package telerik.project.movielibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import telerik.project.movielibrary.models.Role;
import telerik.project.movielibrary.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    @Query("""
            SELECT u FROM User u
            WHERE (:username IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%')))
              AND (:role IS NULL OR u.role = :role)
            """)
    List<User> search(
            @Param("username") String username,
            @Param("role") Role role
    );

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

}
