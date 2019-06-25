package ru.javawebinar.topjava.service;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() throws Exception {
        Meal meal = service.get(100003, 100000);
        Assertions.assertThat(meal).isEqualTo(new Meal(100003,
                        LocalDateTime.of(2019, Month.JUNE, 22, 10, 10, 10),
                "Завтрак", 510));
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception{
        service.get(100003, 100001);
    }

    @Test
    public void delete() throws Exception {
        Meal meal = new Meal(100003,
                LocalDateTime.of(2019, Month.JUNE, 22, 10, 10, 10),
                "Завтрак", 510);
        List<Meal> before = service.getAll(100000);
        List<Meal> afterTest = Arrays.asList(
                new Meal(100002,
                        LocalDateTime.of(2019, Month.JUNE, 22, 17, 17, 17),
                        "Ужин", 1000),
                new Meal(100004,
                        LocalDateTime.of(2019, Month.JUNE, 22, 14, 14, 14),
                        "Обед", 500)
        );
        service.delete(100003, 100000);
        List<Meal> afterService = service.getAll(100000);
        Assertions.assertThat(afterService).doesNotContain(meal);
        Assertions.assertThat(before).contains(meal);
        Assertions.assertThat(afterService).isEqualTo(afterTest);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception{
        service.delete(100003, 100001);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> actual = service.getBetweenDates(LocalDate.of(2019, Month.JUNE, 22),
                LocalDate.of(2019, Month.JUNE, 22),100000);
        List<Meal> expected = Arrays.asList(
                new Meal(100002,
                        LocalDateTime.of(2019, Month.JUNE, 22, 17, 17, 17),
                        "Ужин", 1000),
                new Meal(100004,
                        LocalDateTime.of(2019, Month.JUNE, 22, 10, 10, 10),
                        "Завтрак", 510),
                new Meal(100003,
                        LocalDateTime.of(2019, Month.JUNE, 22, 14, 14, 14),
                        "Обед", 500)
        );
        List<Meal> actual2 = service.getBetweenDates(LocalDate.of(2019, Month.MAY, 23),
                LocalDate.of(2019, Month.MAY, 24), 100000);
        List<Meal> expected2 = Collections.emptyList();
        Assertions.assertThat(actual).isEqualTo(expected);
        Assertions.assertThat(actual2).isEqualTo(expected2);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> actual = service.getBetweenDateTimes(
                LocalDateTime.of(2019, Month.JUNE, 22, 9, 10, 10),
                LocalDateTime.of(2019, Month.JUNE, 22, 17, 10, 10),
                100000);
        List<Meal> expected = Arrays.asList(
                new Meal(100004,
                        LocalDateTime.of(2019, Month.JUNE, 22, 10, 10, 10),
                        "Завтрак", 510),
                new Meal(100003,
                        LocalDateTime.of(2019, Month.JUNE, 22, 14, 14, 14),
                        "Обед", 500)
        );
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(100000);
        List<Meal> expected = Arrays.asList(
                new Meal(100002,
                        LocalDateTime.of(2019, Month.JUNE, 22, 17, 17, 17),
                        "Ужин", 1000),
                new Meal(100004,
                        LocalDateTime.of(2019, Month.JUNE, 22, 10, 10, 10),
                        "Завтрак", 510),
                new Meal(100003,
                        LocalDateTime.of(2019, Month.JUNE, 22, 14, 14, 14),
                        "Обед", 500)
        );
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void update() {
        service.update(new Meal(100004,
                LocalDateTime.of(2020, Month.JUNE, 22, 10, 10, 10),
                "ЗавтракUpdated", 520), 100000);
        List<Meal> expected = Arrays.asList(
                new Meal(100004,
                        LocalDateTime.of(2020, Month.JUNE, 22, 10, 10, 10),
                        "ЗавтракUpdated", 520),
                new Meal(100002,
                        LocalDateTime.of(2019, Month.JUNE, 22, 17, 17, 17),
                        "Ужин", 1000),
                new Meal(100003,
                        LocalDateTime.of(2019, Month.JUNE, 22, 14, 14, 14),
                        "Обед", 500)
        );
        List<Meal> actual = service.getAll(100000);
        Assertions.assertThat(actual).isEqualTo(expected);
    }

//    @Test(expected = NotFoundException.class)
//    public void updateNotFound() throws Exception {
//        service.update(new Meal(100003,
//                LocalDateTime.of(2020, Month.JUNE, 22, 14, 14, 14),
//                "Обедmjk", 500), 100001);
//    }

    @Test
    public void create() {
        service.create(new Meal(null,
                LocalDateTime.of(2020, Month.JUNE, 22, 11, 11, 11),
                "ЗавтракCreated", 520),100000);
        List<Meal> expected = Arrays.asList(
                new Meal(100008,
                        LocalDateTime.of(2020, Month.JUNE, 22, 11, 11, 11),
                        "ЗавтракCreated", 520),
                new Meal(100002,
                        LocalDateTime.of(2019, Month.JUNE, 22, 17, 17, 17),
                        "Ужин", 1000),
                new Meal(100004,
                        LocalDateTime.of(2020, Month.JUNE, 22, 10, 10, 10),
                        "ЗавтракUpdated", 520),
                new Meal(100003,
                        LocalDateTime.of(2019, Month.JUNE, 22, 14, 14, 14),
                        "Обед", 500)
        );
        List<Meal> actual = service.getAll(100000);
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}