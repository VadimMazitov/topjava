package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealStorage {

    public void add(Meal meal);

    public void update(Meal meal);

    public List<Meal> getList();

    public void delete(int id);

    public Meal findById(int id);
}
