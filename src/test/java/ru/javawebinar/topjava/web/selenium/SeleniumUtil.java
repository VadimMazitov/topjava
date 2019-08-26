package ru.javawebinar.topjava.web.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;

public class SeleniumUtil {

    public static List<WebElement> getChildren(WebElement element) {
        if (element == null)
            return Collections.emptyList();
        return element.findElements(By.xpath(".//*"));
    }

    public static WebElement getParent(WebElement element) {
        if (element == null)
            throw new IllegalArgumentException();
        return element.findElement(By.xpath("./.."));
    }

}