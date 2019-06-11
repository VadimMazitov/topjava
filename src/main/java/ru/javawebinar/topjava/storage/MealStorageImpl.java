package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MealStorageImpl implements MealStorage {

    private static List<Meal> meals = new ArrayList<>();

    private static AtomicInteger count = new AtomicInteger(6);

    static {
        meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500, 1));
        meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000, 2));
        meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500, 3));
        meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000, 4));
        meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500, 5));
        meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510, 6));
    }

    public void add(Meal meal) {
        meal.setId(count.incrementAndGet());
        meals.add(meal);
    }

    @Override
    public Meal findById(int id) {
        for (Meal m : meals) {
            if (m.getId() == id)
                return m;
        }
        return null;
    }

    public void update(Meal meal) {
        for (int i = 0; i < meals.size(); i++) {
            Meal m = meals.get(i);
            if (meal.getId() == m.getId()) {
                int id = m.getId();
                meals.remove(id - 1);
                meals.add(id - 1, meal);
                break;
            }
        }

    }

    public List<Meal> getList() {
        return meals;
    }

    public void delete(int id) {
        for (int i = 0; i < meals.size(); i++) {
            Meal m = meals.get(i);
            if (m.getId() == id) {
                meals.remove(i);
                break;
            }
        }
    }



}
