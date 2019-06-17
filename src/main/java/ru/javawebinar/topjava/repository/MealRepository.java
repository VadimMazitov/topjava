package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.util.Collection;

public interface MealRepository {
    Meal save(Meal meal, int userID);

    // false if not found
    boolean delete(int id, int userID);

    // null if not found
    Meal get(int id, int userID);

    Collection<Meal> getAll(int userID, LocalDate startDate, LocalDate endDate);
}
