package ru.javawebinar.topjava.web.selenium;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javawebinar.topjava.UserTestData.USER;
import static org.assertj.core.api.Assertions.assertThat;

public class RootFrontendTest extends AbstractFrontendTest {

    @BeforeEach
    void initRoot() {
        driver.get(ROOT_URL);
    }

    @Test
    void localizationChange() throws Exception {
        assertThat("Calories management").isEqualToIgnoringCase(driver.getTitle());
        WebElement locale = driver.findElement(By.id("locale"));
        WebElement localeRu = driver.findElement(By.id("locale_ru"));
        locale.click();
        localeRu.click();
        assertThat("Подсчет калорий").isEqualToIgnoringCase(driver.getTitle());
    }


    @Test
    void authorization() throws Exception {
        getPageWithLogin(USER, ROOT_URL + "/meals");
        assertThat(driver.getCurrentUrl()).isEqualToIgnoringCase(ROOT_URL + "/meals");
    }

    @Test
    void security() throws Exception {
        driver.get(ROOT_URL + "/meals");
        assertThat(driver.getCurrentUrl()).isEqualToIgnoringCase(ROOT_URL + "/login");
    }

    @Test
    void toRegistration() throws Exception {
        WebElement registration = driver.findElement(By.id("registration"));
        registration.click();
        Thread.sleep(5000);
        String id = driver.findElement(By.name("id")).getAttribute("value");
        String name = driver.findElement(By.name("name")).getAttribute("value");
        String email = driver.findElement(By.name("email")).getAttribute("value");
        String password = driver.findElement(By.name("password")).getAttribute("value");
        int caloriesPerDay = Integer.parseInt(driver.findElement(By.name("caloriesPerDay")).getAttribute("value"));
        Assertions.assertAll(
                () -> assertThat(driver.getCurrentUrl()).isEqualToIgnoringCase(ROOT_URL + "/profile/register"),
                () -> assertThat(id).isNullOrEmpty(),
                () -> assertThat(name).isNullOrEmpty(),
                () -> assertThat(password).isNullOrEmpty(),
                () -> assertThat(email).isNullOrEmpty(),
                () -> assertEquals(caloriesPerDay ,2000)
        );
    }

}