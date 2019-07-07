package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private static final Sort SORT_BY_TIME = new Sort(Sort.Direction.DESC, "date_time");

    @Autowired
    private CrudMealRepository crudRepository;

    @Autowired
    private CrudUserRepository userCrudRepository;

    @Override
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
        meal.setUser(userCrudRepository.getOne(userId));
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal meal = crudRepository.findById(id).orElse(null);
        return (meal != null && meal.getUser().getId() == userId) ? meal : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        User user = userCrudRepository.getOne(userId);
        List<Meal> meals = crudRepository.findAllByUserOrderByDateTimeDesc(user);
        return meals.isEmpty() ? Collections.emptyList() : meals;
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        User user = userCrudRepository.getOne(userId);
        List<Meal> meals = crudRepository.findAllByUserAndDateTimeBetweenOrderByDateTimeDesc(user, startDate, endDate);
        return meals.isEmpty() ? Collections.emptyList() : meals;
    }
}
