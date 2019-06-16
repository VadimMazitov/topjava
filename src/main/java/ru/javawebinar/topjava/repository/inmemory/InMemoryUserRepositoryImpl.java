package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);

    private AtomicInteger counter = new AtomicInteger(0);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return (repository.remove(id) != null);
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        List<User> users = (List<User>) repository.values();
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                int result = o1.getName().compareToIgnoreCase(o2.getName());
                if (result > 0)
                    return 1;
                else if (result < 0)
                    return -1;
                return 0;
            }
        });
        return (List<User>) repository.values();
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        for (User user : repository.values()) {
            if (user.getEmail().equalsIgnoreCase(email))
                return user;
        }
        return null;
    }
}
