package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import javax.jws.soap.SOAPBinding;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> list = getFilteredWithExceededStreamAPI(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        for (UserMealWithExceed userMealWithExceed : list) {
            System.out.println(userMealWithExceed.toString());
        }

//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> mealWithExceedList = new ArrayList<>();
        Map<LocalDate, Integer> map = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            LocalDate localDate = userMeal.getDateTime().toLocalDate();
            if (!map.containsKey(localDate)) {
                map.put(localDate, userMeal.getCalories());
            } else {
                int oldCalories = map.get(localDate);
                map.put(localDate, oldCalories + userMeal.getCalories());
            }
        }
        for (UserMeal userMeal : mealList) {
            int mealCalories = userMeal.getCalories();
            String mealDescription = userMeal.getDescription();
            LocalDateTime mealLocalDateTime = userMeal.getDateTime();

            LocalDate mealLocalDate = mealLocalDateTime.toLocalDate();
            LocalTime mealLocalTime = mealLocalDateTime.toLocalTime();
            int caloriesOfTheDay = map.get(mealLocalDate);

            if (mealLocalTime.isAfter(startTime) && mealLocalTime.isBefore(endTime)) {
                if (caloriesOfTheDay > caloriesPerDay) {
                    mealWithExceedList.add(new UserMealWithExceed(mealLocalDateTime, mealDescription, mealCalories, true));
                } else {
                    mealWithExceedList.add(new UserMealWithExceed(mealLocalDateTime, mealDescription, mealCalories, false));
                }
            }

        }
        return mealWithExceedList;
    }

    public static List<UserMealWithExceed>  getFilteredWithExceededStreamAPI(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> mealWithExceedList = new ArrayList<>();
        Map<LocalDate, Integer> map = new HashMap<>();

        mealList.stream()
                .forEach(userMeal -> {
                    if (!map.containsKey(userMeal.getDateTime().toLocalDate())) {
                        map.put(userMeal.getDateTime().toLocalDate(), userMeal.getCalories());
                    } else {
                        int oldCalories = map.get(userMeal.getDateTime().toLocalDate());
                        map.put(userMeal.getDateTime().toLocalDate(), oldCalories + userMeal.getCalories());
                    }
                }) ;

        mealList.stream()
                .filter(userMeal -> userMeal.getDateTime().toLocalTime().isAfter(startTime) && userMeal.getDateTime().toLocalTime().isBefore(endTime))
                .forEach(userMeal -> {
                    if (map.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay) {
                        mealWithExceedList.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true));
                    } else {
                        mealWithExceedList.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), false));
                    }
                });


        return mealWithExceedList;
    }

    }
