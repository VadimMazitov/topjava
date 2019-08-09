package ru.javawebinar.topjava.web.selenium;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.javawebinar.topjava.TimingExtension;

import java.io.File;
import java.util.HashMap;

@ExtendWith(TimingExtension.class)
abstract public class AbstractFrontendTest {


    private static final String PATH_TO_CHROME = "D:\\ChromeDriver\\chromedriver.exe";

    protected static WebDriver driver;

    private static final HashMap<String, Object> chromePrefs = new HashMap<>();

    private static ChromeOptions options = new ChromeOptions();

    private static final String LANGUAGE = "en";

    private static ChromeDriverService service;

    private static String URL;

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
    }

    @AfterEach
    void terminateDriver() throws Exception {
        driver.quit();
    }

    @AfterAll
    static void stopService() throws Exception {
        service.stop();
    }

    private String getUrl() {

        return null;
    }

}
