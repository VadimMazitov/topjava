package ru.javawebinar.topjava.web.meal;


import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class MealRestController {

    private static final Logger log = getLogger(MealRestController.class);

    @Autowired
    private MealService service;

    public List<MealTo> getAllFiltered(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getAllFiltered");
        return MealsUtil.getFilteredWithExcess(service.getAll(SecurityUtil.authUserId(), startDate, endDate),
                SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getWithExcess(service.getAll(SecurityUtil.authUserId(), LocalDate.MIN, LocalDate.MAX),
                SecurityUtil.authUserCaloriesPerDay());
    }

    public MealTo get(int id, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("get {}", id);
        try {
            Meal meal = service.get(id, SecurityUtil.authUserId(), startDate, endDate);
            List<MealTo> all = getAll();
            boolean excess = all.stream().filter(mealTo -> mealTo.getId() == meal.getId()).findFirst().get().isExcess();
            return MealsUtil.createWithExcess(meal, excess);
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        try {
            return service.create(meal, SecurityUtil.authUserId());
        } catch (NotFoundException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public void update(Meal meal) {
        log.info("update {}", meal);
        try {
            service.update(meal, SecurityUtil.authUserId());
        } catch (NotFoundException e) {
            log.error(e.getMessage());
        }
    }

    public void delete(int id) {
        log.info("delete {}", id);
        try {
            service.delete(id, SecurityUtil.authUserId());
        } catch (NotFoundException e) {
            log.error(e.getMessage());
        }
    }

}