package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import javax.jws.soap.SOAPBinding;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
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

    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime,
                                                                    LocalTime endTime, int caloriesPerDay) {
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
                    mealWithExceedList.add(new UserMealWithExceed(mealLocalDateTime, mealDescription, mealCalories,
                            new AtomicBoolean(caloriesOfTheDay > caloriesPerDay)));
            }

        }
        return mealWithExceedList;
    }

    public static List<UserMealWithExceed>  getFilteredWithExceededStreamAPI(List<UserMeal> mealList, LocalTime startTime,
                                                                             LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> mealsByDate = new HashMap<>();

        mealList.stream()
                .forEach(userMeal -> mealsByDate.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(),
                        Integer::sum)); // последний аргумент показывает, что делать для одинаковых ключей

        List<UserMealWithExceed> mealsWithExceedList = new ArrayList<>();

        mealList.stream()
                .filter(userMeal -> TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .forEach(userMeal -> {
                    mealsWithExceedList.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(),
                                userMeal.getCalories(),
                            new AtomicBoolean(mealsByDate.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay)));
                });

        return mealsWithExceedList;
    }

    @SuppressWarnings("Duplicates")
    public static List<UserMealWithExceed>  getFilteredWithExceededOneCycle(List<UserMeal> mealList, LocalTime startTime,
                                                                            LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> mealsWithExceedList = new ArrayList<>();
        Map<LocalDate, List<Object>> mealsByDate = new HashMap<>();

        for(UserMeal userMeal : mealList) {

            int mealCalories = userMeal.getCalories();
            String mealDescription = userMeal.getDescription();
            LocalDateTime mealLocalDateTime = userMeal.getDateTime();

            LocalDate mealLocalDate = mealLocalDateTime.toLocalDate();
            LocalTime mealLocalTime = mealLocalDateTime.toLocalTime();

            if (!mealsByDate.containsKey(mealLocalDate)) {
                List<Object> innerList = new ArrayList<>(2);
                if (mealCalories < caloriesPerDay) {
                    innerList.add(new AtomicBoolean(false));
                    innerList.add(mealCalories);
                }
                else {
                    innerList.add(new AtomicBoolean(true));
                    innerList.add(mealCalories);
                }
                mealsByDate.put(mealLocalDate, innerList);
            } else {
                int oldCalories = (Integer) mealsByDate.get(mealLocalDate).get(1);
                int totalCalories = oldCalories + mealCalories;
                List<Object> list = mealsByDate.get(mealLocalDate);
                if (totalCalories > 2000) {
                    AtomicBoolean atomicBoolean = (AtomicBoolean) list.get(0);
                    atomicBoolean.set(true);
                    list.set(0, atomicBoolean);
                } else {
                    AtomicBoolean atomicBoolean = (AtomicBoolean) list.get(0);
                    atomicBoolean.set(false);
                    list.set(0, atomicBoolean);
                }
                list.set(1, totalCalories);
                mealsByDate.put(mealLocalDate, list);
            }

            AtomicBoolean atomicBoolean = (AtomicBoolean) mealsByDate.get(mealLocalDate).get(0);

            if (mealLocalTime.isAfter(startTime) && mealLocalTime.isBefore(endTime)) {
                mealsWithExceedList.add(new UserMealWithExceed(mealLocalDateTime, mealDescription, mealCalories, atomicBoolean));
            }
        }
        return mealsWithExceedList;
    }


}
