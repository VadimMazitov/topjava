package ru.javawebinar.topjava.repository.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.jws.soap.SOAPBinding;
import javax.persistence.EntityManager;
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
        User user = em.find(User.class, userId);
        if (user != null) {
            if (meal.isNew()) {
                meal.setUser(user);
                em.persist(meal);
                return meal;
            } else {
                if (meal.getUser().equals(user)) {
                    return em.merge(meal);
                }
            }
        }
        return null;
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
        return em.createNamedQuery(Meal.DELETE, Meal.class)
                .setParameter("user", u)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        User u = em.getReference(User.class, userId);
        Query query = em.createQuery("SELECT m FROM Meal m where m.id=:id and m.user=:user");
        return (Meal) query.setParameter("id", id)
                .setParameter("user", u)
                .getSingleResult();
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class).getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        User u = em.getReference(User.class, userId);

        return em.createNamedQuery(Meal.ALL_SORTED_BETWEEN, Meal.class)
                .setParameter("user", u)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();
    }
}