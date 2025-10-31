package com.elpais.pages;

import com.elpais.models.ArticleData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;

/**
 * Page Object for El Pais Opinion Section
 */
public class OpinionPage extends BasePage {
    
    // Locators based on actual El Pais Opinion page structure
    private final By articleElements = By.cssSelector("section[data-dtm-region='portada_apertura'] article.c");
    private final By articleTitleLink = By.cssSelector("h2.c_t a"); // Get title from anchor directly
    private final By articleContent = By.cssSelector("p.c_d");
    private final By articleImage = By.cssSelector("img.c_m_e");
    
    public OpinionPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Get the first N articles from the Opinion section
     * @param numberOfArticles Number of articles to fetch
     * @return List of ArticleData objects
     */
    public List<ArticleData> getArticles(int numberOfArticles) {
        List<ArticleData> articles = new ArrayList<>();
        
        try {
            Thread.sleep(3000); // Wait for page load
            List<WebElement> articleList = waitForElements(articleElements);
            
            System.out.println("Found " + articleList.size() + " article elements");
            
            int count = 0;
            for (WebElement article : articleList) {
                if (count >= numberOfArticles) {
                    break;
                }
                
                try {
                    ArticleData articleData = extractArticleData(article);
                    if (articleData != null && articleData.getTitle() != null && !articleData.getTitle().isEmpty()) {
                        articles.add(articleData);
                        count++;
                        System.out.println("Extracted article " + count + ": " + 
                            articleData.getTitle().substring(0, Math.min(60, articleData.getTitle().length())) + "...");
                    }
                } catch (Exception e) {
                    // Continue to next article if extraction fails
                }
            }
            
            System.out.println("Total articles extracted: " + count);
            
        } catch (Exception e) {
            System.err.println("Error retrieving articles: " + e.getMessage());
        }
        
        return articles;
    }
    
    /**
     * Extract article data from a WebElement
     * @param article WebElement containing article information
     * @return ArticleData object or null if extraction fails
     */
    private ArticleData extractArticleData(WebElement article) {
        String title = null;
        String content;
        String imageUrl = null;
        String articleUrl = null;
        
        try {
            // Extract title and URL
            WebElement titleLinkElement = article.findElement(articleTitleLink);
            title = titleLinkElement.getText().trim();
            
            // Fallback for hidden elements
            if (title.isEmpty()) {
                title = titleLinkElement.getAttribute("textContent");
                if (title != null) {
                    title = title.trim();
                }
            }
            
            articleUrl = titleLinkElement.getAttribute("href");
            
            if (title == null || title.isEmpty() || title.length() < 3) {
                return null;
            }
            
            // Extract content
            try {
                WebElement contentElement = article.findElement(articleContent);
                content = contentElement.getText().trim();
                
                if (content.isEmpty()) {
                    content = contentElement.getAttribute("textContent");
                    if (content != null) {
                        content = content.trim();
                    }
                }
                
                if (content == null || content.isEmpty()) {
                    content = "Content not available";
                }
            } catch (NoSuchElementException e) {
                content = "Content not available";
            }
            
            // Extract image URL
            try {
                WebElement imageElement = article.findElement(articleImage);
                imageUrl = imageElement.getAttribute("src");
                
                if (imageUrl == null || imageUrl.isEmpty()) {
                    imageUrl = imageElement.getAttribute("data-src");
                }
                if (imageUrl == null || imageUrl.isEmpty()) {
                    String srcset = imageElement.getAttribute("srcset");
                    if (srcset != null && !srcset.isEmpty()) {
                        imageUrl = srcset.split(",")[0].split(" ")[0];
                    }
                }
            } catch (NoSuchElementException e) {
                // Image is optional
            }
            
            return new ArticleData(title, content, imageUrl, articleUrl);
            
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Print article details
     */
    public void printArticleDetails(ArticleData article, int index) {
        System.out.println("\n========== Article " + (index + 1) + " ==========");
        System.out.println("Title (Spanish): " + article.getTitle());
        System.out.println("Content: " + article.getContent());
        if (article.getImageUrl() != null) {
            System.out.println("Image URL: " + article.getImageUrl());
        }
        if (article.getArticleUrl() != null) {
            System.out.println("Article URL: " + article.getArticleUrl());
        }
        System.out.println("================================\n");
    }
}

