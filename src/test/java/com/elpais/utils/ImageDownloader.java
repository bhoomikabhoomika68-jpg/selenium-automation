package com.elpais.utils;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Utility class for downloading and saving images
 */
public class ImageDownloader {
    
    private static final String DEFAULT_IMAGES_DIR = "article_images";
    private String imagesDirectory;
    
    public ImageDownloader() {
        this.imagesDirectory = DEFAULT_IMAGES_DIR;
        createImagesDirectory();
    }
    
    public ImageDownloader(String customDirectory) {
        this.imagesDirectory = customDirectory;
        createImagesDirectory();
    }
    
    /**
     * Create images directory if it doesn't exist
     */
    private void createImagesDirectory() {
        try {
            Path path = Paths.get(imagesDirectory);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
                System.out.println("Created directory: " + imagesDirectory);
            }
        } catch (Exception e) {
            System.out.println("Error creating images directory: " + e.getMessage());
        }
    }
    
    /**
     * Download image from URL and save to local directory
     */
    public boolean downloadImage(String imageUrl, String fileName) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            System.out.println("Image URL is null or empty");
            return false;
        }
        
        try {
            // Clean the URL
            imageUrl = imageUrl.trim();
            
            // Handle protocol-relative URLs
            if (imageUrl.startsWith("//")) {
                imageUrl = "https:" + imageUrl;
            }
            
            // Determine file extension
            String extension = getFileExtension(imageUrl);
            String fullFileName = fileName + extension;
            
            // Create URL and download
            URL url = URI.create(imageUrl).toURL();
            Path targetPath = Paths.get(imagesDirectory, fullFileName);
            
            try (InputStream in = url.openStream()) {
                Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("✓ Image downloaded successfully: " + fullFileName);
                return true;
            }
            
        } catch (Exception e) {
            System.out.println("✗ Error downloading image from " + imageUrl + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Extract file extension from URL
     */
    private String getFileExtension(String url) {
        try {
            // Remove query parameters
            String urlWithoutParams = url.split("\\?")[0];
            
            // Check for common image extensions
            if (urlWithoutParams.toLowerCase().endsWith(".jpg") || 
                urlWithoutParams.toLowerCase().endsWith(".jpeg")) {
                return ".jpg";
            } else if (urlWithoutParams.toLowerCase().endsWith(".png")) {
                return ".png";
            } else if (urlWithoutParams.toLowerCase().endsWith(".gif")) {
                return ".gif";
            } else if (urlWithoutParams.toLowerCase().endsWith(".webp")) {
                return ".webp";
            } else {
                // Default to jpg
                return ".jpg";
            }
        } catch (Exception e) {
            return ".jpg";
        }
    }
    
    /**
     * Get the images directory path
     */
    public String getImagesDirectory() {
        return imagesDirectory;
    }
    
    /**
     * Delete all images in the directory
     */
    public void cleanImagesDirectory() {
        try {
            Path path = Paths.get(imagesDirectory);
            if (Files.exists(path)) {
                Files.walk(path)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (Exception e) {
                            System.out.println("Error deleting file: " + e.getMessage());
                        }
                    });
            }
        } catch (Exception e) {
            System.out.println("Error cleaning images directory: " + e.getMessage());
        }
    }
}

