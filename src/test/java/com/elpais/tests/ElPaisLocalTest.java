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
 * TestNG test class for local execution
 * Tests El Pais Opinion section scraping functionality
 */
public class ElPaisLocalTest {
    
    private WebDriver driver;
    private HomePage homePage;
    private OpinionPage opinionPage;
    private ImageDownloader imageDownloader;
    private WordAnalyzer wordAnalyzer;
    private List<ArticleData> articles;
    
    private static final int NUMBER_OF_ARTICLES = 5;
    
    @Parameters({"browser"})
    @BeforeClass
    public void setUp(@Optional("chrome") String browser) {
        System.out.println("\n========================================");
        System.out.println("Starting test on browser: " + browser);
        System.out.println("========================================\n");
        
        // Initialize driver
        driver = DriverManager.createLocalDriver(browser);
        
        // Initialize page objects
        homePage = new HomePage(driver);
        
        // Initialize utilities
        imageDownloader = new ImageDownloader();
        wordAnalyzer = new WordAnalyzer();
        articles = new ArrayList<>();
    }
    
    @Test(priority = 1, description = "Navigate to El Pais and verify Spanish language")
    public void testNavigateToElPaisAndVerifyLanguage() {
        System.out.println("\nTest 1: Navigating to El Pais and verifying Spanish language");
        
        homePage.navigateToHomePage();
        homePage.acceptCookies();
        
        boolean isSpanish = homePage.isPageInSpanish();
        Assert.assertTrue(isSpanish, "Website should be displayed in Spanish");
        
        System.out.println("Language verification: PASSED");
    }
    
    @Test(priority = 2, dependsOnMethods = "testNavigateToElPaisAndVerifyLanguage",
          description = "Navigate to Opinion section")
    public void testNavigateToOpinionSection() {
        System.out.println("\nTest 2: Navigating to Opinion section");
        
        opinionPage = homePage.navigateToOpinionSection();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String currentUrl = driver.getCurrentUrl();
        Assert.assertTrue(currentUrl.contains("opinion"), 
            "URL should contain 'opinion'");
        
        System.out.println("Opinion section navigation: PASSED");
    }
    
    @Test(priority = 3, dependsOnMethods = "testNavigateToOpinionSection",
          description = "Scrape articles from Opinion section")
    public void testScrapeArticles() {
        System.out.println("\nTest 3: Scraping " + NUMBER_OF_ARTICLES + " articles from Opinion section");
        
        articles = opinionPage.getArticles(NUMBER_OF_ARTICLES);
        
        Assert.assertTrue(articles.size() > 0, "Should find at least 1 article");
        Assert.assertTrue(articles.size() <= NUMBER_OF_ARTICLES, 
            "Should not exceed " + NUMBER_OF_ARTICLES + " articles");
        
        System.out.println("Successfully scraped " + articles.size() + " articles\n");
        
        for (int i = 0; i < articles.size(); i++) {
            opinionPage.printArticleDetails(articles.get(i), i);
        }
    }
    
    @Test(priority = 4, dependsOnMethods = "testScrapeArticles",
          description = "Download cover images")
    public void testDownloadImages() {
        System.out.println("\nTest 4: Downloading cover images");
        
        int downloadedCount = 0;
        
        for (ArticleData article : articles) {
            if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
                boolean success = imageDownloader.downloadImage(
                    article.getImageUrl(), 
                    article.getSafeFileName()
                );
                if (success) {
                    downloadedCount++;
                }
            }
        }
        
        System.out.println("Downloaded " + downloadedCount + " images");
        System.out.println("Images directory: " + imageDownloader.getImagesDirectory());
    }
    
    @Test(priority = 5, dependsOnMethods = "testScrapeArticles",
          description = "Translate article headers to English")
    public void testTranslateHeaders() {
        System.out.println("\nTest 5: Translating article headers from Spanish to English");
        
        for (int i = 0; i < articles.size(); i++) {
            ArticleData article = articles.get(i);
            String translatedTitle = TranslationUtil.translate(article.getTitle());
            article.setTranslatedTitle(translatedTitle);
            
            System.out.println("\nArticle " + (i + 1) + ":");
            System.out.println("  Spanish:  " + article.getTitle());
            System.out.println("  English:  " + translatedTitle);
        }
        
        for (ArticleData article : articles) {
            Assert.assertNotNull(article.getTranslatedTitle(), 
                "Translated title should not be null");
            Assert.assertFalse(article.getTranslatedTitle().isEmpty(), 
                "Translated title should not be empty");
        }
        
        System.out.println("\nTranslation completed for " + articles.size() + " articles");
    }
    
    @Test(priority = 6, dependsOnMethods = "testTranslateHeaders",
          description = "Analyze word frequency in translated headers")
    public void testAnalyzeWordFrequency() {
        System.out.println("\nTest 6: Analyzing word frequency in translated headers");
        
        List<String> translatedTitles = new ArrayList<>();
        for (ArticleData article : articles) {
            if (article.getTranslatedTitle() != null) {
                translatedTitles.add(article.getTranslatedTitle());
            }
        }
        
        wordAnalyzer.analyzeTexts(translatedTitles);
        wordAnalyzer.printRepeatedWords(2);
        wordAnalyzer.printTopWords(10);
        
        System.out.println("Unique words: " + wordAnalyzer.getTotalUniqueWords());
        System.out.println("Total words: " + wordAnalyzer.getTotalWordCount());
        System.out.println("\nWord frequency analysis: COMPLETED");
    }
    
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("\nBrowser session closed");
        }
        
        System.out.println("\n========================================");
        System.out.println("Test Suite Execution: COMPLETED");
        System.out.println("========================================\n");
    }
}

