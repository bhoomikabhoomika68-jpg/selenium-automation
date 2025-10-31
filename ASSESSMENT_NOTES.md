# El Pais Opinion Section Scraper - Assessment Submission

## 📋 Overview
This is a comprehensive Selenium WebDriver automation project implementing the Page Object Model (POM) design pattern to scrape, translate, and analyze articles from the El País Opinion section.

## ✅ Completed Requirements

### 1. Web Scraping
- ✓ Visit El País website (https://elpais.com)
- ✓ Verify website content is displayed in Spanish
- ✓ Navigate to Opinion section
- ✓ Scrape first 5 articles from the Opinion section
- ✓ Extract title, content, and image URL for each article

### 2. Image Handling
- ✓ Download cover images for articles (when available)
- ✓ Save images to local directory (`article_images/`)
- ✓ Generate safe filenames from article titles

### 3. Translation
- ✓ Translate article titles from Spanish to English
- ✓ Support multiple translation APIs (Google Translate, RapidAPI)
- ✓ Fallback to mock translation when no API keys configured
- ✓ Display both original and translated titles

### 4. Word Frequency Analysis
- ✓ Analyze translated article headers
- ✓ Identify words repeated more than twice
- ✓ Display word count for repeated words
- ✓ Filter common stop words

### 5. Cross-Browser Testing
- ✓ Local execution on Chrome browser
- ✓ BrowserStack integration for cloud testing
- ✓ Parallel execution on 5 different browser/device combinations:
  - Windows 10 - Chrome (Latest)
  - Windows 11 - Edge (Latest)
  - macOS Ventura - Safari (Latest)
  - iPhone 14 Pro - iOS 16
  - Samsung Galaxy S23 - Android 13

### 6. Code Quality
- ✓ Page Object Model (POM) design pattern
- ✓ Clean separation of concerns (pages, tests, utils, models)
- ✓ Comprehensive error handling
- ✓ TestNG for test organization and execution
- ✓ Maven for dependency management
- ✓ Thread-safe parallel execution

## 🗂️ Project Structure

```
ElPaisScraper/
├── src/test/java/com/elpais/
│   ├── base/
│   │   └── DriverManager.java          # WebDriver factory for local & BrowserStack
│   ├── models/
│   │   └── ArticleData.java            # Article data model
│   ├── pages/
│   │   ├── BasePage.java               # Base page with common methods
│   │   ├── HomePage.java               # Home page object
│   │   └── OpinionPage.java            # Opinion section page object
│   ├── tests/
│   │   ├── ElPaisLocalTest.java        # Local test execution
│   │   └── ElPaisBrowserStackTest.java # BrowserStack parallel tests
│   └── utils/
│       ├── ImageDownloader.java        # Image download utility
│       ├── TranslationUtil.java        # Translation service
│       └── WordAnalyzer.java           # Word frequency analyzer
├── testng.xml                           # TestNG config for local execution
├── testng-browserstack.xml              # TestNG config for BrowserStack
├── pom.xml                              # Maven dependencies
├── run-local-tests.sh                   # Script for local testing
├── run-browserstack-tests.sh            # Script for BrowserStack testing
└── README.md                            # Complete documentation
```

## 🚀 Quick Start

### Local Execution
```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

### BrowserStack Execution (requires credentials)
```bash
export BROWSERSTACK_USERNAME="your_username"
export BROWSERSTACK_ACCESS_KEY="your_access_key"
mvn clean test -DsuiteXmlFile=testng-browserstack.xml
```

## 🎯 Key Features

### 1. Robust Scraping Logic
- CSS selectors based on actual El País HTML structure
- Fallback mechanisms for hidden/lazy-loaded elements
- Uses `textContent` attribute when `getText()` fails

### 2. Flexible Translation
- Supports Google Cloud Translate API
- Supports RapidAPI Translation
- Mock translation for testing without API keys

### 3. Parallel Testing
- BrowserStack integration with ThreadLocal for thread safety
- 5 parallel threads for different browser configurations
- Aggregated word frequency analysis across all threads

### 4. Clean Architecture
- Page Object Model separates UI logic from test logic
- Factory pattern for WebDriver creation
- Utility classes for reusable functionality
- TestNG for test organization and dependency management

## 📊 Test Results

### Test Suite Includes:
1. **testNavigateToElPaisAndVerifyLanguage** - Verifies Spanish language
2. **testNavigateToOpinionSection** - Navigates to Opinion section
3. **testScrapeArticles** - Scrapes 5 articles with title, content, and images
4. **testDownloadImages** - Downloads and saves article images
5. **testTranslateHeaders** - Translates titles to English
6. **testAnalyzeWordFrequency** - Analyzes word patterns

### Test Execution Flow:
```
Navigate → Verify Language → Opinion Section → 
Scrape Articles → Download Images → Translate → Analyze
```

## 🛠️ Technologies Used

- **Java 21** - Programming language
- **Selenium WebDriver 4.15.0** - Browser automation
- **TestNG 7.8.0** - Test framework with parallel execution
- **Maven 3.x** - Build and dependency management
- **WebDriverManager 5.6.2** - Automatic driver management
- **BrowserStack** - Cloud testing platform
- **Apache HttpClient 5.2.1** - HTTP requests for translation API
- **Gson 2.10.1** - JSON processing

## 🎨 Design Patterns Implemented

1. **Page Object Model (POM)** - UI interactions encapsulated in page classes
2. **Factory Pattern** - DriverManager creates WebDriver instances
3. **Singleton Pattern** - Utility classes
4. **ThreadLocal Pattern** - Thread-safe BrowserStack execution

## 📝 Notes

- The scraping logic is resilient to website structure changes
- Images are optional - tests continue if images are not available
- Translation uses mock implementation when API keys are not configured
- All tests pass with proper assertions and validations
- Code is clean, documented, and ready for production use

## 🔍 Code Quality Highlights

- ✓ No hardcoded values
- ✓ Comprehensive error handling
- ✓ Clean logging for debugging
- ✓ Thread-safe parallel execution
- ✓ Proper test dependencies and priorities
- ✓ Professional naming conventions
- ✓ Well-documented code

---

**Project Status**: ✅ Ready for Assessment

**Execution Environment**: Local Chrome & BrowserStack Cloud (5 parallel configurations)

