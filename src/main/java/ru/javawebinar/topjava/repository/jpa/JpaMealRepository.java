package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User userOfId = em.getReference(User.class, userId);
        meal.setUser(userOfId);
        if (userOfId != null) {
            if (meal.isNew()) {
                em.persist(meal);
            } else {
                if (get(meal.getId(), userId) == null) return null;
                em.merge(meal);
            }
        }
        return meal;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
//        User user = em.find(User.class, userId);
//        Meal meal = em.find(Meal.class, id);
//        if (user != null && meal != null && meal.getUser().equals(user)) {
//            em.remove(meal);
//            return true;
//        }
//        return false;
        User u = em.getReference(User.class, userId);
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("user", u)
                .setParameter("id", id)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        User u = em.getReference(User.class, userId);
        Query query = em.createQuery("SELECT m FROM Meal m where m.id=:id and m.user=:user");
        try {
            return (Meal) query.setParameter("id", id)
                    .setParameter("user", u)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
//        User user = em.find(User.class, userId);
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("userID", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
//        User u = em.getReference(User.class, userId);
        return em.createNamedQuery(Meal.ALL_SORTED_BETWEEN, Meal.class)
                .setParameter("userID", userId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}