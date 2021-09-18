package ru.jm.spring_mvc_hibernate.DAO;

import org.springframework.stereotype.Repository;
import ru.jm.spring_mvc_hibernate.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("FROM User", User.class).getResultList();
    }

    @Override
    public void updateUser(final User user) {
        entityManager.merge(user);
    }

    @Override
    public User getUser(final int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void deleteUser(final int id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @Override
    public User findUserByUsername(String username) {
        return entityManager.createQuery("SELECT user FROM User user WHERE user.username = :username", User.class)
                .setParameter("username", username).getSingleResult();
    }

    @Override
    public void saveUser(String name, String surname, int age, String email, String username, String password) {
        entityManager.persist(new User(name, surname, age, email, username, password));
    }
}
