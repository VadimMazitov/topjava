package ru.javawebinar.topjava.web.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;


public class ProxyTest {



    @Test
    void proxyTest() throws Exception {
        System.setProperty("webdriver.chrome.driver", "D:\\ChromeDriver\\chromedriver.exe");
        BrowserProxy proxy = new BrowserProxy();
        DesiredCapabilities caps = DesiredCapabilities.chrome();
        proxy.startServer(caps);
        WebDriver driver = new ChromeDriver();
        driver.get("http://127.0.0.1:8080/topjava");
        Thread.sleep(5000);
        System.out.println(proxy.getHar().getLog().getBrowser().getName());
        driver.quit();
        proxy.stopServer();

    }

}
