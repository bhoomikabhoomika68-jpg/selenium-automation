package com.elpais.tests;

import com.elpais.base.DriverManager;
import com.elpais.models.ArticleData;
import com.elpais.pages.HomePage;
import com.elpais.pages.OpinionPage;
import com.elpais.utils.ImageDownloader;
import com.elpais.utils.TranslationUtil;
import com.elpais.utils.WordAnalyzer;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TestNG test class for BrowserStack execution with parallel threads
 * Tests El Pais Opinion section across multiple browsers and devices
 */
public class ElPaisBrowserStackTest {
    
    private ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private ThreadLocal<HomePage> homePage = new ThreadLocal<>();
    private ThreadLocal<OpinionPage> opinionPage = new ThreadLocal<>();
    private ThreadLocal<List<ArticleData>> articles = new ThreadLocal<>();
    private ImageDownloader imageDownloader;
    private WordAnalyzer wordAnalyzer;
    
    private static final int NUMBER_OF_ARTICLES = 5;
    
    @BeforeClass
    public void beforeClass() {
        if (!DriverManager.isBrowserStackConfigured()) {
            throw new RuntimeException(
                "BrowserStack credentials not configured. " +
                "Set BROWSERSTACK_USERNAME and BROWSERSTACK_ACCESS_KEY environment variables."
            );
        }
        
        imageDownloader = new ImageDownloader("article_images_browserstack");
        wordAnalyzer = new WordAnalyzer();
        
        System.out.println("\n========================================");
        System.out.println("BrowserStack Test Suite: STARTING");
        System.out.println("========================================\n");
    }
    
    @Parameters({"browser", "browserVersion", "os", "osVersion", "deviceName"})
    @BeforeMethod
    public void setUp(
            @Optional("chrome") String browser,
            @Optional("latest") String browserVersion,
            @Optional("Windows") String os,
            @Optional("10") String osVersion,
            @Optional("") String deviceName) throws Exception {
        
        WebDriver webDriver;
        
        if (deviceName != null && !deviceName.isEmpty()) {
            System.out.println("\nInitializing: " + deviceName + " (iOS " + osVersion + ")");
            webDriver = DriverManager.createBrowserStackMobileDriver(deviceName, osVersion);
        } else {
            System.out.println("\nInitializing: " + browser + " " + browserVersion + " on " + os + " " + osVersion);
            webDriver = DriverManager.createBrowserStackDesktopDriver(browser, browserVersion, os, osVersion);
        }
        
        driver.set(webDriver);
        homePage.set(new HomePage(webDriver));
        articles.set(new ArrayList<>());
    }
    
    @Test(priority = 1)
    public void testElPaisOpinionSectionComplete() {
        WebDriver currentDriver = driver.get();
        HomePage currentHomePage = homePage.get();
        
        try {
            System.out.println("Step 1: Navigating and verifying language");
            currentHomePage.navigateToHomePage();
            Thread.sleep(3000); // Extra wait for mobile
            currentHomePage.acceptCookies();
            
            boolean isSpanish = currentHomePage.isPageInSpanish();
            Assert.assertTrue(isSpanish, "Page should be in Spanish");
            System.out.println("Language verification: PASSED");
            
            System.out.println("\nStep 2: Navigating to Opinion section");
            OpinionPage currentOpinionPage = currentHomePage.navigateToOpinionSection();
            opinionPage.set(currentOpinionPage);
            
            Thread.sleep(2000);
            String currentUrl = currentDriver.getCurrentUrl();
            Assert.assertTrue(currentUrl.contains("opinion"), "URL should contain 'opinion'");
            System.out.println("Opinion section: ACCESSED");
            
            System.out.println("\nStep 3: Scraping articles");
            List<ArticleData> scrapedArticles = currentOpinionPage.getArticles(NUMBER_OF_ARTICLES);
            articles.set(scrapedArticles);
            
            Assert.assertTrue(scrapedArticles.size() > 0, "Should find at least 1 article");
            System.out.println("Articles scraped: " + scrapedArticles.size());
            
            for (int i = 0; i < scrapedArticles.size(); i++) {
                currentOpinionPage.printArticleDetails(scrapedArticles.get(i), i);
            }
            
            System.out.println("Step 4: Downloading images");
            int downloadedCount = 0;
            for (ArticleData article : scrapedArticles) {
                if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
                    boolean success = imageDownloader.downloadImage(
                        article.getImageUrl(), 
                        article.getSafeFileName() + "_" + System.currentTimeMillis()
                    );
                    if (success) downloadedCount++;
                }
            }
            System.out.println("Images downloaded: " + downloadedCount);
            
            System.out.println("\nStep 5: Translating headers");
            synchronized (wordAnalyzer) {
                for (ArticleData article : scrapedArticles) {
                    String translatedTitle = TranslationUtil.translate(article.getTitle());
                    article.setTranslatedTitle(translatedTitle);
                    wordAnalyzer.analyzeText(translatedTitle);
                }
            }
            System.out.println("Translation: COMPLETED");
            System.out.println("\nAll test steps: PASSED");
            
        } catch (Exception e) {
            System.err.println("Test execution failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    @AfterMethod
    public void tearDown(org.testng.ITestResult result) {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            try {
                // Mark test status on BrowserStack
                if (currentDriver instanceof org.openqa.selenium.remote.RemoteWebDriver) {
                    org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) currentDriver;
                    if (result.getStatus() == org.testng.ITestResult.SUCCESS) {
                        js.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"passed\", \"reason\": \"All test steps completed successfully\"}}");
                        System.out.println("✓ Test marked as PASSED on BrowserStack");
                    } else {
                        js.executeScript("browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\":\"failed\", \"reason\": \"" + result.getThrowable().getMessage() + "\"}}");
                        System.out.println("✗ Test marked as FAILED on BrowserStack");
                    }
                }
            } catch (Exception e) {
                // Ignore if not BrowserStack
            }
            currentDriver.quit();
            System.out.println("Session closed\n");
        }
        driver.remove();
        homePage.remove();
        opinionPage.remove();
        articles.remove();
    }
    
    @AfterClass
    public void afterClass() {
        System.out.println("\n========================================");
        System.out.println("WORD FREQUENCY ANALYSIS (Aggregated)");
        System.out.println("========================================");
        
        wordAnalyzer.printRepeatedWords(2);
        wordAnalyzer.printTopWords(15);
        
        System.out.println("\n========================================");
        System.out.println("BrowserStack Test Suite: COMPLETED");
        System.out.println("========================================");
        System.out.println("\nView detailed results:");
        System.out.println("https://automate.browserstack.com/dashboard\n");
    }
}

