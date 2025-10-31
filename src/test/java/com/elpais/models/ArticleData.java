package com.elpais.models;

/**
 * Model class to store article data
 */
public class ArticleData {
    private String title;
    private String content;
    private String imageUrl;
    private String articleUrl;
    private String translatedTitle;
    
    public ArticleData(String title, String content, String imageUrl, String articleUrl) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.articleUrl = articleUrl;
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getArticleUrl() {
        return articleUrl;
    }
    
    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }
    
    public String getTranslatedTitle() {
        return translatedTitle;
    }
    
    public void setTranslatedTitle(String translatedTitle) {
        this.translatedTitle = translatedTitle;
    }
    
    /**
     * Generate a safe filename from the title
     */
    public String getSafeFileName() {
        if (title == null || title.isEmpty()) {
            return "article_" + System.currentTimeMillis();
        }
        String safeName = title.replaceAll("[^a-zA-Z0-9\\s]", "_")
                              .replaceAll("\\s+", "_")
                              .toLowerCase();
        return safeName.substring(0, Math.min(50, safeName.length()));
    }
    
    @Override
    public String toString() {
        return "ArticleData{" +
                "title='" + title + '\'' +
                ", translatedTitle='" + translatedTitle + '\'' +
                ", hasImage=" + (imageUrl != null) +
                '}';
    }
}

