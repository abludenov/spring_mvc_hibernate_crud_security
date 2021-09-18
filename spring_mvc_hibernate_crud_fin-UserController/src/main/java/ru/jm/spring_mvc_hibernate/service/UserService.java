package ru.jm.spring_mvc_hibernate.service;

import ru.jm.spring_mvc_hibernate.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    void updateUser(User user);

    User getUser(int id);

    void deleteUser(int id);

    User findByUsername(String username);

    void saveUser(String name, String surname, int age, String email, String username, String password);
}
