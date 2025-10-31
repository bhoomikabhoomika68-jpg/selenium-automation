# El Pais Opinion Section Scraper

A comprehensive web scraping automation project using Selenium WebDriver with Page Object Model (POM) design pattern. This project scrapes articles from El País Opinion section, translates them, analyzes word frequency, and supports parallel execution on BrowserStack.

## 🎯 Features

- **Web Scraping**: Scrapes first 5 articles from El País Opinion section
- **Language Verification**: Ensures website content is in Spanish
- **Image Download**: Downloads and saves article cover images
- **Translation**: Translates article titles from Spanish to English
- **Word Analysis**: Identifies frequently repeated words across translated headers
- **Cross-Browser Testing**: Supports local and BrowserStack execution
- **Parallel Execution**: Runs tests on 5 parallel threads on BrowserStack
- **Page Object Model**: Clean, maintainable test architecture

## 📋 Requirements

- Java 21 or higher
- Maven 3.6+
- ChromeDriver (automatically managed by WebDriverManager)
- BrowserStack account (for cloud testing)

## 🚀 Project Structure

```
ElPaisScraper/
├── src/
│   └── test/java/com/elpais/
│       ├── base/
│       │   └── DriverManager.java          # WebDriver factory
│       ├── models/
│       │   └── ArticleData.java            # Article data model
│       ├── pages/
│       │   ├── BasePage.java               # Base page object
│       │   ├── HomePage.java               # Home page object
│       │   └── OpinionPage.java            # Opinion page object
│       ├── utils/
│       │   ├── TranslationUtil.java        # Translation service
│       │   ├── ImageDownloader.java        # Image download utility
│       │   └── WordAnalyzer.java           # Word frequency analyzer
│       └── tests/
│           ├── ElPaisLocalTest.java        # Local execution tests
│           └── ElPaisBrowserStackTest.java # BrowserStack tests
├── testng.xml                               # TestNG config for local
├── testng-browserstack.xml                  # TestNG config for BrowserStack
├── pom.xml                                  # Maven configuration
└── README.md                                # This file
```

## 🔧 Setup Instructions

### 1. Clone and Build

```bash
cd ElPaisScraper
mvn clean install
```

### 2. Configure Translation API (Optional)

The project supports multiple translation options:

**Option A: RapidAPI (Recommended for testing)**
1. Sign up at [RapidAPI](https://rapidapi.com/sibaridev/api/rapid-translate-multi-traduction)
2. Subscribe to the free tier
3. Set environment variable:
```bash
export RAPID_API_KEY="your_api_key_here"
```

**Option B: Google Cloud Translate**
1. Set up Google Cloud project
2. Enable Cloud Translation API
3. Download service account JSON
4. Set environment variable:
```bash
export GOOGLE_APPLICATION_CREDENTIALS="/path/to/service-account.json"
```

**Option C: Mock Translation (Default)**
- No setup required
- Uses basic Spanish-English word substitution
- Suitable for testing purposes

### 3. Configure BrowserStack

1. Sign up for free trial: [BrowserStack Sign Up](https://www.browserstack.com/users/sign_up)
2. Get your credentials from [Automate Dashboard](https://automate.browserstack.com/)
3. Set environment variables:

```bash
export BROWSERSTACK_USERNAME="your_username"
export BROWSERSTACK_ACCESS_KEY="your_access_key"
```

## 🏃 Running Tests

### Local Execution (Chrome)

```bash
# Run on local Chrome browser
mvn clean test -DsuiteXmlFile=testng.xml
```

### BrowserStack Parallel Execution (5 threads)

```bash
# Run on BrowserStack with 5 parallel configurations
mvn clean test -DsuiteXmlFile=testng-browserstack.xml
```

The BrowserStack test will run on:
1. **Windows 10** - Chrome (Latest)
2. **Windows 11** - Edge (Latest)
3. **macOS Ventura** - Safari (Latest)
4. **iPhone 14 Pro** - iOS 16
5. **Samsung Galaxy S23** - Android 13

### Run Specific Test Class

```bash
# Run only local tests
mvn test -Dtest=ElPaisLocalTest

# Run only BrowserStack tests
mvn test -Dtest=ElPaisBrowserStackTest
```

## 📊 Test Scenarios

### Test 1: Language Verification
- Navigate to El País homepage
- Verify content is displayed in Spanish
- Accept cookie consent if present

### Test 2: Navigate to Opinion Section
- Click/navigate to Opinion section
- Verify URL contains "opinion"

### Test 3: Scrape Articles
- Fetch first 5 articles from Opinion section
- Extract title, content, and image URL
- Print article details

### Test 4: Download Images
- Download cover images for each article
- Save to `article_images/` directory
- Handle missing images gracefully

### Test 5: Translate Headers
- Translate article titles from Spanish to English
- Print original and translated versions
- Support multiple translation APIs

### Test 6: Analyze Word Frequency
- Parse translated headers
- Count word occurrences
- Print words repeated more than 2 times

## 📁 Output

### Images Directory
```
article_images/
├── article_1_safe_filename.jpg
├── article_2_safe_filename.jpg
└── ...
```

### Console Output
```
========== Article 1 ==========
Title (Spanish): [Original Spanish title]
Content: [Article summary]
Image URL: [URL]
================================

Original: [Spanish title]
Translated: [English translation]

========== Word Frequency Analysis ==========
Words repeated more than 2 times:
  government        : 4 occurrences
  president         : 3 occurrences
  spain            : 3 occurrences
=============================================
```

## 🔍 BrowserStack Dashboard

After running BrowserStack tests, view detailed results:
- 📊 **Dashboard**: https://automate.browserstack.com/dashboard
- 📹 Video recordings of test execution
- 📝 Network logs and console logs
- 🐛 Screenshots on failures

## 🛠️ Troubleshooting

### Issue: Cookie consent not accepted
**Solution**: The test automatically handles cookie consent. If it fails, check the selector in `BasePage.acceptCookies()`

### Issue: Articles not found
**Solution**: El País website structure may change. Update selectors in `OpinionPage.java`:
- `articleElements`
- `articleTitle`
- `articleContent`

### Issue: BrowserStack authentication failed
**Solution**: Verify environment variables:
```bash
echo $BROWSERSTACK_USERNAME
echo $BROWSERSTACK_ACCESS_KEY
```

### Issue: Translation not working
**Solution**: Check if API keys are set or use mock translation for testing

## 📚 Technologies Used

- **Selenium WebDriver 4.15.0** - Browser automation
- **TestNG 7.8.0** - Testing framework
- **WebDriverManager 5.5.3** - Automatic driver management
- **BrowserStack** - Cloud testing platform
- **Google Cloud Translate** - Translation API
- **Apache HttpClient 5** - HTTP requests
- **Gson** - JSON processing
- **Maven** - Build automation

## 🎨 Design Patterns

- **Page Object Model (POM)** - Separates page structure from test logic
- **Factory Pattern** - DriverManager for creating WebDriver instances
- **Singleton Pattern** - Utility classes for shared functionality
- **ThreadLocal** - Thread-safe parallel execution on BrowserStack

## 📝 Notes

- Tests use explicit waits for robust element location
- Images are downloaded asynchronously
- Word analysis filters common stop words
- BrowserStack tests aggregate word frequency across all threads
- Mock translation is used when no API keys are configured

## 📤 Submission Instructions

### For BrowserStack Assessment Submission

This project is ready for submission! Follow these steps:

#### 1. ✅ GitHub Repository (Completed)
Your code is in a public GitHub repository.

#### 2. 🔗 BrowserStack Automate Public Link

**To create a public BrowserStack build link:**

1. Run tests on BrowserStack:
   ```bash
   ./run-browserstack-tests.sh
   ```

2. Go to [BrowserStack Automate Dashboard](https://automate.browserstack.com/dashboard)

3. Find your build: "El Pais Opinion Section Tests"

4. Click **"Share"** button → **"Make Public"** → Copy the public URL

5. Verify the link works in an incognito browser window

**Your public link will look like:**
```
https://automate.browserstack.com/dashboard/v2/public-build/XXXXX/YYYYY/ZZZZZ
```

#### 3. 📸 Screenshot Upload to Google Drive

**To upload and share your screenshot:**

1. Take a screenshot of your BrowserStack build running (dashboard view)

2. Upload to [Google Drive](https://drive.google.com)

3. Right-click → **"Share"** → Set to **"Anyone with the link"** (Viewer)

4. Copy the shareable link

**Your Google Drive link will look like:**
```
https://drive.google.com/file/d/XXXXXXXXXXXXXXXXXXXXX/view?usp=sharing
```

#### 📋 Final Submission Format

```
GitHub Repository: [YOUR_GITHUB_REPO_URL]

BrowserStack Automate Build (Public): 
[YOUR_BROWSERSTACK_PUBLIC_URL]

Screenshot of the build running:
[YOUR_GOOGLE_DRIVE_SCREENSHOT_URL]
```

**📖 Detailed Instructions:** See [SUBMISSION_GUIDE.md](./SUBMISSION_GUIDE.md) for step-by-step instructions with screenshots and troubleshooting.

---

## 📄 License

This project is for educational and assessment purposes.

---

**Happy Testing! 🚀**

