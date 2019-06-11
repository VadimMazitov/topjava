package ru.javawebinar.topjava.DAO;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.storage.MealStorageImpl;

import java.util.List;


public class MealDAOImpl implements MealDAO {

    private static final MealStorage mealStorage;

    static {
        mealStorage = new MealStorageImpl();
    }

    public void add(Meal meal) {
        mealStorage.add(meal);
    }

    @Override
    public Meal findById(int id) {
        return mealStorage.findById(id);
    }

    @Override
    public List<Meal> findAll() {
        return mealStorage.getList();
    }

    @Override
    public void delete(int id) {
        mealStorage.delete(id);
    }

    @Override
    public void update(Meal meal) {
        mealStorage.update(meal);
    }


}
