package guru.sfg.brewery.repositories.security;

import guru.sfg.brewery.domain.security.LoginFailure;
import guru.sfg.brewery.domain.security.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface LoginFailureRepository extends JpaRepository<LoginFailure, Integer> {

    // No implementation needed - will work automatically.
    List<LoginFailure> findAllByUserAndCreateDateIsAfter(User user, Timestamp timestamp);
}
