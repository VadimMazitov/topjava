package ru.javawebinar.topjava.web.selenium.SeleniumUtil;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class UrlExtension implements BeforeAllCallback, BeforeEachCallback {

    private static String URL;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        String testClass = extensionContext.getRequiredTestClass().getSimpleName();
        switch (testClass) {
            case "RootFrontendTest" : URL = "http://localhost:8080/topjava";
            break;
            case "MealFrontendTest" : URL = "http://localhost:8080/topjava/meals";
            break;
            case "UserFrontendTest" : URL = "http://localhost:8080/topjava/users";
            break;
        }
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) throws Exception {

    }
}
