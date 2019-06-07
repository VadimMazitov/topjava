package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class mealServlet extends HttpServlet {

    private static final Logger log = getLogger(mealServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.debug("start doGet of /meals");

        List<MealTo> meals = MealsUtil.getFilteredWithExcess(mealsInit(), LocalTime.of(0, 0),
                LocalTime.of(23, 59), 2000);

        request.setAttribute("meals", meals);
        request.setAttribute("formatter", new SimpleDateFormat("yyyy-MM-dd'T'hh:mm"));

        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }

    @SuppressWarnings("Duplicates")
    private static List<Meal> mealsInit() {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        return meals;
    }
}
