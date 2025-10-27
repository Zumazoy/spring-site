package web_7.Users.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web_7.Users.model.UserModel;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    UserModel findByLogin(String login);
    List<UserModel> findByName(String name);
    List<UserModel> findByEmail(String email);
    Page<UserModel> findAllByOrderByIdAsc(Pageable pageable);
    List<UserModel> findAllByOrderByIdAsc();

    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phone_number);
}