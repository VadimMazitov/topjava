package ru.javawebinar.topjava.web.selenium;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
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

    static void takeScreenshot(String name, WebDriver driver) throws Exception {
        File screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenShot, new File("c:\\tmp\\" + name + ".png"));
    }

}