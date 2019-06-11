package ru.javawebinar.topjava.DAO;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDAO {

    public void add(Meal meal);

    public List<Meal> findAll();

    public void delete(int id);

    public void update(Meal meal);

    public Meal findById(int id);
}
