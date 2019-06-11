package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

import ru.javawebinar.topjava.DAO.MealDAO;
import ru.javawebinar.topjava.DAO.MealDAOImpl;
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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class mealServlet extends HttpServlet {

    private static final Logger log = getLogger(mealServlet.class);

    private static final MealDAO mealDAO;

    static {
        mealDAO = new MealDAOImpl();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), formatter);
        String id = request.getParameter("id");

        Meal meal = new Meal(dateTime, description, calories);
        if (id == null || id.isEmpty()) {
            mealDAO.add(meal);
        } else {
            meal.setId(Integer.parseInt(id));
            mealDAO.update(meal);
        }
        request.setAttribute("meals", MealsUtil.getFilteredWithExcess(mealDAO.findAll(), LocalTime.of(0,
                0), LocalTime.of(23, 59), 2000));
        String page = "meals.jsp";
        request.getRequestDispatcher(page).forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.debug("start doGet of /meals");

        String action = request.getParameter("action");
        String page = "meals.jsp";

        if (action != null) {
            if (action.equalsIgnoreCase("delete")) {
                int id = Integer.parseInt(request.getParameter("id"));
                mealDAO.delete(id);
                request.setAttribute("meals", MealsUtil.getFilteredWithExcess(mealDAO.findAll(), LocalTime.of(0,
                        0), LocalTime.of(23, 59), 2000));
                page = "meals.jsp";
            } else if (action.equalsIgnoreCase("update")) {
                int id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("meal", mealDAO.findById(id));
                page = "add-edit.jsp";
            } else if (action.equalsIgnoreCase("listMeals")) {
                request.setAttribute("meals", MealsUtil.getFilteredWithExcess(mealDAO.findAll(), LocalTime.of(0,
                        0), LocalTime.of(23, 59), 2000));
                page = "meals.jsp";
            } else if (action.equalsIgnoreCase("add")) {
                page = "add-edit.jsp";
            }
        } else {
            request.setAttribute("meals", MealsUtil.getFilteredWithExcess(mealDAO.findAll(), LocalTime.of(0,
                    0), LocalTime.of(23, 59), 2000));
            page = "meals.jsp";
        }

        request.getRequestDispatcher(page).forward(request, response);
    }

}
