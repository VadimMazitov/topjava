package ru.javawebinar.topjava.web.selenium;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.javawebinar.topjava.TimingExtension;
import ru.javawebinar.topjava.model.User;

import java.io.File;
import java.util.HashMap;

@ExtendWith(TimingExtension.class)
@ExtendWith(Screenshot.class)
//@ActiveProfiles(resolver = AllActiveProfileResolver.class)
abstract public class AbstractFrontendTest {
    private static final String PATH_TO_CHROME = "D:\\ChromeDriver\\chromedriver.exe";

    static WebDriver driver;

    private static final HashMap<String, Object> chromePrefs = new HashMap<>();

    private static ChromeOptions options = new ChromeOptions();

    private static final String LANGUAGE = "en";

    private static ChromeDriverService service;

    protected static final String ROOT_URL = "http://localhost:8080/topjava";

    @BeforeAll
    static void init() throws Exception {
        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(PATH_TO_CHROME))
                .usingAnyFreePort()
                .build();
        chromePrefs.put("intl.accept_languages", LANGUAGE);
        options.setExperimentalOption("prefs", chromePrefs);
        service.start();
    }

    @BeforeEach
    void createDriver() throws Exception {
        driver = new ChromeDriver(service, options);
        driver.manage().window().maximize();
    }

    @AfterAll
    static void stopService() throws Exception {
        service.stop();
    }

    protected void getPageWithLogin(User user, String url) {
        driver.get(ROOT_URL);
        WebElement login = driver.findElement(By.name("username"));
        login.sendKeys(user.getEmail());
        WebElement password = driver.findElement(By.name("password"));
        password.sendKeys(user.getPassword());
        login.submit();
        if (!url.equalsIgnoreCase(driver.getCurrentUrl()))
            driver.get(url);
    }
}