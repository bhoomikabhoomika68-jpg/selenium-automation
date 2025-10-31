package com.elpais.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.MutableCapabilities;
import java.net.URI;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Driver Manager to handle WebDriver initialization for local and BrowserStack
 */
public class DriverManager {
    
    private static final String BROWSERSTACK_USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String BROWSERSTACK_ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String BROWSERSTACK_URL = "https://" + BROWSERSTACK_USERNAME + ":" + 
                                                    BROWSERSTACK_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";
    
    /**
     * Create local WebDriver
     */
    public static WebDriver createLocalDriver(String browser) {
        WebDriver driver;
        
        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized");
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--lang=es");
                driver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addPreference("intl.accept_languages", "es-ES");
                driver = new FirefoxDriver(firefoxOptions);
                break;
                
            case "edge":
                WebDriverManager.edgedriver().setup();
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                driver = new EdgeDriver(edgeOptions);
                break;
                
            case "safari":
                driver = new SafariDriver();
                break;
                
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        
        driver.manage().window().maximize();
        return driver;
    }
    
    /**
     * Create BrowserStack WebDriver for desktop
     */
    public static WebDriver createBrowserStackDesktopDriver(String browser, String browserVersion, String os, String osVersion) 
            throws MalformedURLException {
        
        MutableCapabilities capabilities = new MutableCapabilities();
        
        // Browser capabilities
        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("browserVersion", browserVersion);
        
        // OS capabilities
        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("os", os);
        bstackOptions.put("osVersion", osVersion);
        bstackOptions.put("projectName", "El Pais Scraper");
        bstackOptions.put("buildName", "Opinion Section Test Build");
        bstackOptions.put("sessionName", browser + " " + browserVersion + " on " + os + " " + osVersion);
        bstackOptions.put("local", "false");
        bstackOptions.put("seleniumVersion", "4.15.0");
        bstackOptions.put("debug", "true");
        bstackOptions.put("networkLogs", "true");
        bstackOptions.put("consoleLogs", "info");
        
        capabilities.setCapability("bstack:options", bstackOptions);
        
        return new RemoteWebDriver(URI.create(BROWSERSTACK_URL).toURL(), capabilities);
    }
    
    /**
     * Create BrowserStack WebDriver for mobile
     */
    public static WebDriver createBrowserStackMobileDriver(String deviceName, String osVersion) 
            throws MalformedURLException {
        
        MutableCapabilities capabilities = new MutableCapabilities();
        
        // Mobile capabilities
        Map<String, Object> bstackOptions = new HashMap<>();
        bstackOptions.put("deviceName", deviceName);
        bstackOptions.put("osVersion", osVersion);
        bstackOptions.put("realMobile", "true");
        bstackOptions.put("projectName", "El Pais Scraper");
        bstackOptions.put("buildName", "Opinion Section Test Build - Mobile");
        bstackOptions.put("sessionName", deviceName + " - " + osVersion);
        bstackOptions.put("local", "false");
        bstackOptions.put("debug", "true");
        bstackOptions.put("networkLogs", "true");
        bstackOptions.put("consoleLogs", "info");
        
        capabilities.setCapability("bstack:options", bstackOptions);
        
        return new RemoteWebDriver(URI.create(BROWSERSTACK_URL).toURL(), capabilities);
    }
    
    /**
     * Check if BrowserStack credentials are configured
     */
    public static boolean isBrowserStackConfigured() {
        return BROWSERSTACK_USERNAME != null && !BROWSERSTACK_USERNAME.isEmpty() &&
               BROWSERSTACK_ACCESS_KEY != null && !BROWSERSTACK_ACCESS_KEY.isEmpty();
    }
    
    /**
     * Get BrowserStack URL
     */
    public static String getBrowserStackUrl() {
        return BROWSERSTACK_URL;
    }
}

