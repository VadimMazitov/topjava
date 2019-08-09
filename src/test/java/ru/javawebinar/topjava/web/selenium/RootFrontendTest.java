package ru.javawebinar.topjava.web.selenium;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.javawebinar.topjava.web.selenium.SeleniumUtil.SeleniumUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
public class RootFrontendTest extends AbstractFrontendTest {

    private static final String ROOT_URL = "http://localhost:8080/topjava";

    @Test
    void indexTest() throws Exception {
        driver.get(ROOT_URL);
        WebElement selectUser = driver.findElement(By.name("userId"));
        List<WebElement> selectUserOptions = SeleniumUtil.getChildren(selectUser);
        selectUserOptions.stream().forEach(webElement -> assertThat(webElement.getTagName()).isEqualToIgnoringCase("option"));

        assertThat(driver.getTitle()).isEqualTo("Calories management");

    }

    @Test
    void userLogin() throws Exception {
        driver.get(ROOT_URL);
        WebElement selectUser = driver.findElement(By.name("userId"));
        List<WebElement> selectUserOptions = SeleniumUtil.getChildren(selectUser);
        WebElement admin = selectUserOptions.get(1);
        admin.click();
        admin.submit();
        assertThat(driver.getCurrentUrl()).isEqualToIgnoringCase("http://localhost:8080/topjava/meals");
    }

}
