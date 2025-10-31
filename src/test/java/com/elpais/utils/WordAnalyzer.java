package com.elpais.utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for analyzing word frequency in text
 */
public class WordAnalyzer {
    
    // Common English stop words to filter out (optional)
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "the", "a", "an", "and", "or", "but", "in", "on", "at", "to", "for",
        "of", "with", "by", "from", "as", "is", "was", "are", "were", "been",
        "be", "have", "has", "had", "do", "does", "did", "will", "would",
        "could", "should", "may", "might", "must", "can", "this", "that",
        "these", "those", "i", "you", "he", "she", "it", "we", "they",
        "translated"
    ));
    
    private Map<String, Integer> wordFrequency;
    
    public WordAnalyzer() {
        this.wordFrequency = new HashMap<>();
    }
    
    /**
     * Analyze a list of translated titles
     */
    public void analyzeTexts(List<String> texts) {
        for (String text : texts) {
            if (text != null && !text.isEmpty()) {
                analyzeText(text);
            }
        }
    }
    
    /**
     * Analyze a single text and update word frequency
     */
    public void analyzeText(String text) {
        if (text == null || text.isEmpty()) {
            return;
        }
        
        // Split text into words (remove punctuation and convert to lowercase)
        String[] words = text.toLowerCase()
                            .replaceAll("[^a-zA-Z\\s]", " ")
                            .split("\\s+");
        
        for (String word : words) {
            word = word.trim();
            
            // Skip short words (less than 3 characters) and stop words
            if (word.length() >= 3 && !STOP_WORDS.contains(word)) {
                wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
            }
        }
    }
    
    /**
     * Get words that appear more than a specified threshold
     */
    public Map<String, Integer> getWordsRepeatedMoreThan(int threshold) {
        return wordFrequency.entrySet().stream()
                .filter(entry -> entry.getValue() > threshold)
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                ));
    }
    
    /**
     * Get all word frequencies sorted by count (descending)
     */
    public Map<String, Integer> getAllWordsSorted() {
        return wordFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1,
                    LinkedHashMap::new
                ));
    }
    
    /**
     * Print words repeated more than specified threshold
     */
    public void printRepeatedWords(int threshold) {
        System.out.println("\n========== Word Frequency Analysis ==========");
        System.out.println("Words repeated more than " + threshold + " times:");
        System.out.println("=============================================");
        
        Map<String, Integer> repeatedWords = getWordsRepeatedMoreThan(threshold);
        
        if (repeatedWords.isEmpty()) {
            System.out.println("No words found repeated more than " + threshold + " times.");
        } else {
            // Sort by frequency (descending)
            repeatedWords.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                    .forEach(entry -> 
                        System.out.printf("  %-20s : %d occurrences%n", 
                            entry.getKey(), entry.getValue())
                    );
        }
        
        System.out.println("=============================================\n");
    }
    
    /**
     * Print top N most frequent words
     */
    public void printTopWords(int n) {
        System.out.println("\n========== Top " + n + " Most Frequent Words ==========");
        
        getAllWordsSorted().entrySet().stream()
                .limit(n)
                .forEach(entry -> 
                    System.out.printf("  %-20s : %d occurrences%n", 
                        entry.getKey(), entry.getValue())
                );
        
        System.out.println("================================================\n");
    }
    
    /**
     * Get total unique words
     */
    public int getTotalUniqueWords() {
        return wordFrequency.size();
    }
    
    /**
     * Get total word count
     */
    public int getTotalWordCount() {
        return wordFrequency.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
    
    /**
     * Clear the word frequency map
     */
    public void clear() {
        wordFrequency.clear();
    }
}

