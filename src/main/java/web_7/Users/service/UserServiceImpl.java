package web_7.Users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import web_7.Users.model.UserModel;
import web_7.Users.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Page<UserModel> getAllUsers(int page, int size) {
        return userRepository.findAllByOrderByIdAsc(PageRequest.of(page, size));
    }

    @Override
    public List<UserModel> getAllUsers() {
        return userRepository.findAllByOrderByIdAsc();
    }

    @Override
    public void addUser(UserModel user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(UserModel user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserModel> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserModel findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public List<UserModel> searchUsers(String keyword, String searchBy) {
        List<UserModel> users;
        switch (searchBy) {
            case "id":
                try {
                    Long id = Long.parseLong(keyword);
                    return userRepository.findById(id).map(List::of).orElse(List.of());
                } catch (NumberFormatException e) {
                    return List.of();
                }
            case "name":
                users = userRepository.findByName(keyword);
                break;
            case "email":
                users = userRepository.findByEmail(keyword);
                break;
            default:
                users = userRepository.findAllByOrderByIdAsc();
        }
        return users.stream()
                .sorted(Comparator.comparing(UserModel::getId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByLogin(String login) {
        return userRepository.existsByLogin(login);
    }
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    @Override
    public boolean existsByPhoneNumber(String phone_number) {
        return userRepository.existsByPhoneNumber(phone_number);
    }
}