package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JSPMealController {

    @Autowired
    MealService mealService;

    @GetMapping("/meals")
    public String mealsGet(Model model,
                           @RequestParam(name = "action", required = false) String action,
                           @RequestParam(name = "startDate", required = false) String startDate,
                           @RequestParam(name = "endDate", required = false) String endDate,
                           @RequestParam(name = "startTime", required = false) String startTime,
                           @RequestParam(name = "endTime", required = false) String endIme,
                           @RequestParam(name = "id", required = false) String mealId) {
        int userId = SecurityUtil.authUserId();
        int userCalories = SecurityUtil.authUserCaloriesPerDay();
        List<MealTo> meals = null;
        switch (action == null ? "all" : action) {
            case "create":
            case "update":
                final Meal meal = "create".equalsIgnoreCase(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealService.get(Integer.parseInt(mealId), userId);
                model.addAttribute("meal", meal);
                return "mealForm";
            case "delete":
                mealService.delete(Integer.parseInt(mealId), userId);
                meals = MealsUtil.getWithExcess(mealService.getAll(userId), userCalories);
                break;
            case "filter":
                meals = MealsUtil.getFilteredWithExcess(mealService.getBetweenDates(
                            parseLocalDate(startDate),
                            parseLocalDate(endDate),
                            userId),
                        userCalories,
                        parseLocalTime(startTime),
                        parseLocalTime(endIme));
                break;
            case "all":
            default:
                meals = MealsUtil.getWithExcess(mealService.getAll(userId), userCalories);
                break;
        }
        model.addAttribute("meals", meals);
        return "meals";
    }

    @PostMapping("/meals")
    public String mealForm(Model model, HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        int userId = SecurityUtil.authUserId();
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.isEmpty(request.getParameter("id"))) {
            mealService.create(meal, userId);
        } else {
            meal.setId(Integer.parseInt(request.getParameter("id")));
            mealService.update(meal, userId);
        }
        return "redirect:/meals";
    }
}
