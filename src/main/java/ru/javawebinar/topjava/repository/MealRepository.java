package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {
    // null if updated meal do not belong to userId
    Meal save(Meal meal, int userId);

    // false if not found
    boolean delete(int id);

    // null if not found
    Meal get(int id);

    Collection<Meal> getAll();
}
