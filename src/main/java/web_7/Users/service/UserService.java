package web_7.Users.service;

import org.springframework.data.domain.Page;
import web_7.Users.model.UserModel;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Page<UserModel> getAllUsers(int page, int size);
    void addUser(UserModel user);
    void updateUser(UserModel user);
    void deleteUser(Long userId);
    Optional<UserModel> findById(Long userId);
    UserModel findByLogin(String login);
    List<UserModel> searchUsers(String keyword, String searchBy);
    List<UserModel> getAllUsers();

    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phone_number);
}