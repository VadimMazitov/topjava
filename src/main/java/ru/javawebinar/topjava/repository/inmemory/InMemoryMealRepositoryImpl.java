package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

//    {
//        MealsUtil.MEALS.forEach(this::save);
//    }

    @Override
    public Meal save(Meal meal, int userID) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        if (meal.getUserID() == userID)
            return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
        return null;
    }

    @Override
    public boolean delete(int id, int userID) {
        if (repository.get(id) != null) {
            if (repository.get(id).getUserID() == userID)
                return repository.remove(id) != null;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userID, LocalDate startDate, LocalDate endDate) {
        Meal meal = null;
        if (repository.get(id) != null) {
            meal = repository.get(id);
            if (meal.getUserID() == userID && DateTimeUtil.isBetweenDate(meal.getDate(), startDate, endDate))
                return meal;
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(int userID, LocalDate startDate, LocalDate endDate) {

        return repository.values()
                .stream()
                .filter(meal -> DateTimeUtil.isBetweenDate(meal.getDate(), startDate, endDate) && meal.getUserID() == userID)
                .sorted(new Comparator<Meal>() {
                    @Override
                    public int compare(Meal o1, Meal o2) {
                        return o1.getDateTime().compareTo(o2.getDateTime()) > 0 ? -1 :
                                o1.getDateTime().compareTo(o2.getDateTime()) < 0 ? 1 : 0;
                    }
                })
                .collect(Collectors.toList());
    }

}

