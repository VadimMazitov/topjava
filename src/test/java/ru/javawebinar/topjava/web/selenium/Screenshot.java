package ru.javawebinar.topjava.web.selenium;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.slf4j.Logger;

import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.web.selenium.AbstractFrontendTest.driver;
import static ru.javawebinar.topjava.web.selenium.SeleniumUtil.takeScreenshot;

public class Screenshot implements TestWatcher {

    private static final Logger log = getLogger(Screenshot.class);

    @Override
    public void testSuccessful(ExtensionContext context) {
        driver.quit();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        String name = context.getRequiredTestMethod().getName();
        String now = LocalDateTime.now().toString().substring(0, 16).replace(":","-");
        try {
            String fileName = name + "_" + now;
            takeScreenshot(fileName, driver);
            log.error("Error occurred, details in {}", fileName);
        } catch (Exception e) {
            log.error("Unable to take screenshot", e);
        }
        driver.quit();
    }
}
