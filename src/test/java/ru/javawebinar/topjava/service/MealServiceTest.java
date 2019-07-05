package ru.javawebinar.topjava.service;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.junit.rules.Stopwatch;

import org.junit.runner.Description;
import org.junit.runner.RunWith;

import org.slf4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;


import java.time.LocalDate;

import java.time.Month;


import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService service;

    private final Logger log = getLogger(MealServiceTest.class);

//    @ClassRule
//    public static ExternalResource externalResource = new ExternalResource() {
//        @Override
//        public Statement apply(Statement base, Description description) {
//            return new Statement() {
//                @Override
//                public void evaluate() throws Throwable {
//                    base.evaluate();
//                    description.getMethodName();
//                }
//            };
//        }
//
//        @Override
//        protected void after() {
//            System.out.println(LocalDateTime.now() + "testing finished");
//        }
//    };

//    @Rule
//    public TestRule testRule = new TestRule() {
//        @Override
//        public Statement apply(Statement statement, Description description) {
//            return new Statement() {
//                @Override
//                public void evaluate() throws Throwable {
//                    statement.evaluate();
//                    System.out.println(LocalDateTime.now() + " Method Name: " + description.getMethodName());
//                }
//            };
//        }
//    };

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public Stopwatch stopwatch = new Stopwatch() {

        @Override
        protected void succeeded(long nanos, Description description) {
            log.info("Method name {}, succeeded Time(ms): {}", description.getMethodName(), nanos / Math.pow(10, 6));
        }

        @Override
        protected void failed(long nanos, Throwable e, Description description) {
            log.info("Method name {}, failed Time(ms): {}", description.getMethodName(), nanos / Math.pow(10, 6));
        }

        @Override
        protected void finished(long nanos, Description description) {
            log.info("Method name {}, finished Time(ms): {}", description.getMethodName(), nanos / Math.pow(10, 6));
        }
    };

    @Test
    public void delete() throws Exception {
        service.delete(MEAL1_ID, USER_ID);
        assertMatch(service.getAll(USER_ID), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void deleteNotFound() {
        thrown.expect(NotFoundException.class);
        service.delete(1, USER_ID);
    }

    @Test
    public void deleteNotOwn() {
        thrown.expect(NotFoundException.class);
        service.delete(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = getCreated();
        Meal created = service.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(newMeal, created);
        assertMatch(service.getAll(USER_ID), newMeal, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void get() throws Exception {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getNotFound() {
        thrown.expect(NotFoundException.class);
        service.get(1, USER_ID);
    }

    @Test
    public void getNotOwn() {
        thrown.expect(NotFoundException.class);
        service.get(MEAL1_ID, ADMIN_ID);
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    public void updateNotFound() {
        thrown.expect(NotFoundException.class);
        service.update(MEAL1, ADMIN_ID);
    }

    @Test
    public void getAll() throws Exception {
        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void getBetween() throws Exception {
        assertMatch(service.getBetweenDates(
                LocalDate.of(2015, Month.MAY, 30),
                LocalDate.of(2015, Month.MAY, 30), USER_ID), MEAL3, MEAL2, MEAL1);
    }
}