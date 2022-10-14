package antifraud.persistence;

import antifraud.business.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);

    @Transactional
    void deleteUserByUsername(String username);

    List<User> findAll();
}