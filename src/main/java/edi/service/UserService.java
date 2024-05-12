package edi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edi.dto.UserDataRequestDto;
import edi.model.User;
import edi.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> findOneUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        return user;
    }

    public User saveUser(User user) {
        User newUser = new User(user.getUserId(), user.getFullName(), user.getUsername(), user.getPassword(),
                user.getStatus());

        return userRepository.save(newUser);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
