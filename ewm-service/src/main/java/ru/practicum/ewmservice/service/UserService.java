package ru.practicum.ewmservice.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmservice.model.User;
import ru.practicum.ewmservice.repository.UserRepository;

import java.util.List;


@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public User createUser(String name, String email) {
        return userRepository.save(new User(null, name, email));
    }

    public List<User> getUsers(int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);
        return userRepository.findAll(page).getContent();
    }

    public List<User> getUsersByIds(List<Long> ids) {
        return userRepository.findAllByIdIn(ids);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
