package ru.javawebinar.topjava.web.selenium.meal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.selenium.AbstractFrontendTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javawebinar.topjava.MealTestData.MEAL1;
import static ru.javawebinar.topjava.UserTestData.USER;
import static org.assertj.core.api.Assertions.assertThat;

public class MealFrontendTest extends AbstractFrontendTest {
    private static final String MEAL_URL = "http://localhost:8080/topjava/meals";

    @BeforeEach
    void initMeal() throws Exception {
        getPageWithLogin(USER, MEAL_URL);
    }

    @Test
    void userMeal() throws Exception {
        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.xpath("//a[@href='/users']")));
    }

    @RepeatedTest(2)
    void save() throws Exception {
        WebElement add = driver.findElement(By.id("add"));
        add.click();

//        https://stackoverflow.com/a/10950905
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement description = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("description")));
        WebElement dateTime = driver.findElement(By.id("dateTime"));
        WebElement calories = driver.findElement(By.id("calories"));

        Meal mealToSave = new Meal(null,
                LocalDateTime.of(2015, 4, 4, 11, 0),
                MEAL1.getDescription(),
                MEAL1.getCalories());
        description.sendKeys(mealToSave.getDescription());
        dateTime.sendKeys(mealToSave.getDateTime().toString().replace('T', ' '));
        calories.sendKeys(String.valueOf(mealToSave.getCalories()));

        fail();
        WebElement save = driver.findElement(By.id("save"));
        save.click();
        WebElement noty = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("noty")));
        if (noty != null && noty.isDisplayed()) {
            String id = noty.getAttribute("id");
            if (id.equals("noty_suc"))
                assertFalse(description.isDisplayed());
            else if (id.equals("noty_fail"))
                assertTrue(description.isDisplayed());
        } else
            fail("None of noties appeared");
    }

    @Test
    void logout() throws Exception {
        WebElement logoutForm = driver.findElement(By.id("logout"));
        logoutForm.submit();
        assertThat(driver.getCurrentUrl()).isEqualToIgnoringCase(ROOT_URL + "/login");
    }

    @Test
    void toProfile() throws Exception {
        WebElement toProfile = driver.findElement(By.xpath("//*[contains(text(), '" + USER.getName() + " profile')]"));
        toProfile.click();

        String id = driver.findElement(By.name("id")).getAttribute("value");
        String name = driver.findElement(By.name("name")).getAttribute("value");
        String email = driver.findElement(By.name("email")).getAttribute("value");
        String password = driver.findElement(By.name("password")).getAttribute("value");
        int caloriesPerDay = Integer.parseInt(driver.findElement(By.name("caloriesPerDay")).getAttribute("value"));
        assertAll(() -> assertThat(id).isEqualTo(String.valueOf(USER.getId())),
                  () -> assertThat(name).isEqualTo(USER.getName()),
                  () -> assertThat(email).isEqualTo(USER.getEmail()),
                  () -> assertThat(caloriesPerDay).isEqualTo(USER.getCaloriesPerDay()),
                  () -> assertThat(password).isNullOrEmpty(),
                  () -> assertThat(driver.getCurrentUrl()).isEqualToIgnoringCase(ROOT_URL + "/profile"));
    }
}
