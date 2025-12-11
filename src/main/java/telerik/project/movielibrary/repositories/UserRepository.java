package telerik.project.movielibrary.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import telerik.project.movielibrary.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findByUsername(String username);

    boolean existsByUsername(String username);
    
}
