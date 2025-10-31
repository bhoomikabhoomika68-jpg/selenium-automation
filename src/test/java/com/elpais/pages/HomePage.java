package com.elpais.pages;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for El Pais Home Page
 */
public class HomePage extends BasePage {
    
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Navigate to El Pais homepage
     */
    public void navigateToHomePage() {
        driver.get("https://elpais.com");
    }
    
    /**
     * Navigate to Opinion section
     */
    public OpinionPage navigateToOpinionSection() {
        driver.get("https://elpais.com/opinion/");
        return new OpinionPage(driver);
    }
    
    /**
     * Verify page is in Spanish
     */
    public boolean isPageInSpanish() {
        String currentUrl = driver.getCurrentUrl();
        String pageSource = driver.getPageSource();
        
        // Check URL doesn't contain other language codes
        boolean urlCheck = !currentUrl.contains("/en/") && 
                          !currentUrl.contains("/cat/") &&
                          !currentUrl.contains("/br/");
        
        // Check for Spanish content indicators
        boolean contentCheck = pageSource.contains("lang=\"es\"") || 
                              pageSource.contains("lang='es'") ||
                              pageSource.contains("Opinión");
        
        return urlCheck && contentCheck;
    }
}

