package com.elpais.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

/**
 * Base Page class containing common methods for all page objects
 */
public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    
    /**
     * Wait for element to be visible
     */
    protected WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    /**
     * Wait for element to be clickable
     */
    protected WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }
    
    /**
     * Wait for all elements to be present
     */
    protected List<WebElement> waitForElements(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }
    
    /**
     * Click element with wait
     */
    protected void clickElement(By locator) {
        waitForClickable(locator).click();
    }
    
    /**
     * Get text from element
     */
    protected String getElementText(By locator) {
        return waitForElement(locator).getText();
    }
    
    /**
     * Check if element exists
     */
    protected boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Accept cookies banner if present
     */
    public void acceptCookies() {
        try {
            By cookieButton = By.cssSelector("button[id*='didomi'], button.didomi-components-button--primary, button#didomi-notice-agree-button");
            WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(cookieButton));
            acceptButton.click();
            Thread.sleep(1000); // Wait for cookie banner to disappear
        } catch (Exception e) {
            System.out.println("No cookie banner found or already accepted");
        }
    }
}

