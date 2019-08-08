package ru.javawebinar.topjava.web.meal;


import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/ajax/meals")
public class MealAjaxController extends AbstractMealController {

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @Override
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @PostMapping
    public void createOrUpdate(@RequestParam("id") Integer id,
                               @RequestParam("description") String description,
                               @RequestParam("dateTime") String dateTimeString,
                               @RequestParam("calories") Integer calories) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);
        Meal meal = new Meal(id, dateTime, description, calories);
        if (meal.isNew())
            super.create(meal);
    }

//    @PostMapping()
//    public void createOrUpdate(@RequestBody Meal meal) {
//        if (meal.isNew())
//            super.create(meal);
//    }

    @Override
    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealTo> getBetween(@RequestParam(value = "startDate", required = false) LocalDate startDate,
                                   @RequestParam(value = "startTime", required = false) LocalTime startTime,
                                   @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                   @RequestParam(value = "endTime", required = false) LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }
}
