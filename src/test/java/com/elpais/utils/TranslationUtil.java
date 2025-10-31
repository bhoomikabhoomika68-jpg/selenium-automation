package com.elpais.utils;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

/**
 * Utility class for translating text from Spanish to English
 * Supports multiple translation APIs
 */
public class TranslationUtil {
    
    private static final String RAPID_API_KEY = System.getenv("RAPID_API_KEY");
    private static final String RAPID_API_HOST = "rapid-translate-multi-traduction.p.rapidapi.com";
    private static final String RAPID_API_URL = "https://rapid-translate-multi-traduction.p.rapidapi.com/t";
    
    /**
     * Translate text using RapidAPI (Free alternative)
     * To use: Sign up at https://rapidapi.com/sibaridev/api/rapid-translate-multi-traduction
     * and set RAPID_API_KEY environment variable
     */
    public static String translateWithRapidAPI(String text) {
        if (RAPID_API_KEY == null || RAPID_API_KEY.isEmpty()) {
            System.out.println("RAPID_API_KEY not set. Using mock translation.");
            return translateMock(text);
        }
        
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(RAPID_API_URL);
            
            // Set headers
            request.setHeader("content-type", "application/json");
            request.setHeader("X-RapidAPI-Key", RAPID_API_KEY);
            request.setHeader("X-RapidAPI-Host", RAPID_API_HOST);
            
            // Set body
            JsonObject body = new JsonObject();
            body.addProperty("from", "es");
            body.addProperty("to", "en");
            body.addProperty("q", text);
            
            request.setEntity(new StringEntity(body.toString()));
            
            // Execute request
            try (ClassicHttpResponse response = client.executeOpen(null, request, null)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                
                // Parse response
                JsonArray jsonArray = JsonParser.parseString(responseBody).getAsJsonArray();
                if (jsonArray.size() > 0) {
                    return jsonArray.get(0).getAsString();
                }
            }
        } catch (Exception e) {
            System.out.println("Error translating with RapidAPI: " + e.getMessage());
        }
        
        return translateMock(text);
    }
    
    /**
     * Translate text using Google Cloud Translate API
     * Requires GOOGLE_APPLICATION_CREDENTIALS environment variable
     */
    public static String translateWithGoogleCloud(String text) {
        try {
            // Check if Google credentials are set
            String credentials = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            if (credentials == null || credentials.isEmpty()) {
                System.out.println("GOOGLE_APPLICATION_CREDENTIALS not set. Using mock translation.");
                return translateMock(text);
            }
            
            Translate translate = TranslateOptions.getDefaultInstance().getService();
            Translation translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage("es"),
                Translate.TranslateOption.targetLanguage("en")
            );
            
            return translation.getTranslatedText();
        } catch (Exception e) {
            System.out.println("Error translating with Google Cloud: " + e.getMessage());
            return translateMock(text);
        }
    }
    
    /**
     * Simple mock translation for demonstration purposes
     * In production, you should use a real translation API
     */
    public static String translateMock(String text) {
        // Basic Spanish to English word mapping for common words
        String translated = text
            .replace("El", "The")
            .replace("La", "The")
            .replace("Los", "The")
            .replace("Las", "The")
            .replace("de", "of")
            .replace("del", "of the")
            .replace("en", "in")
            .replace("por", "by")
            .replace("para", "for")
            .replace("con", "with")
            .replace("sin", "without")
            .replace("sobre", "about")
            .replace("entre", "between")
            .replace("España", "Spain")
            .replace("Europa", "Europe")
            .replace("guerra", "war")
            .replace("paz", "peace")
            .replace("gobierno", "government")
            .replace("presidente", "president")
            .replace("política", "politics")
            .replace("economía", "economy")
            .replace("crisis", "crisis")
            .replace("mundo", "world")
            .replace("país", "country")
            .replace("año", "year")
            .replace("años", "years")
            .replace("día", "day")
            .replace("días", "days")
            .replace("vez", "time")
            .replace("veces", "times")
            .replace("más", "more")
            .replace("menos", "less")
            .replace("mejor", "better")
            .replace("peor", "worse")
            .replace("nuevo", "new")
            .replace("nueva", "new")
            .replace("viejo", "old")
            .replace("vieja", "old")
            .replace("grande", "big")
            .replace("pequeño", "small")
            .replace("pequeña", "small");
        
        return "[Translated] " + translated;
    }
    
    /**
     * Default translation method
     * Uses RapidAPI if key is available, otherwise uses mock translation
     */
    public static String translate(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        // Try RapidAPI first
        if (RAPID_API_KEY != null && !RAPID_API_KEY.isEmpty()) {
            return translateWithRapidAPI(text);
        }
        
        // Fallback to Google Cloud if credentials are set
        String googleCreds = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
        if (googleCreds != null && !googleCreds.isEmpty()) {
            return translateWithGoogleCloud(text);
        }
        
        // Use mock translation as last resort
        return translateMock(text);
    }
}

