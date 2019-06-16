package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Collection;

public interface MealService {

    Meal create(Meal meal, int userID);

    void update(Meal meal, int userID) throws NotFoundException;

    void delete(int id, int userID) throws NotFoundException;

    Collection<Meal> getAll(int userID, LocalDate startDate, LocalDate endDate);

    Meal get(int id, int userID, LocalDate startDate, LocalDate endDate) throws NotFoundException;

}