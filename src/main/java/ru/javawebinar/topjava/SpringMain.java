package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ROLE_ADMIN));
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.create(new Meal(null, LocalDateTime.now(), "myMeal", 777, 1));
            mealRestController.getAll().stream().forEach(System.out::println);
            System.out.println(mealRestController.get(4, LocalDate.MIN, LocalDate.MAX, LocalTime.MIN, LocalTime.MAX));
            SecurityUtil.id = 1;
            mealRestController.delete(4);
            mealRestController.delete(7);
            mealRestController.getAll().stream().forEach(System.out::println);
            SecurityUtil.id = 2;
            mealRestController.getAll().stream().forEach(System.out::println);
        }
    }
}
