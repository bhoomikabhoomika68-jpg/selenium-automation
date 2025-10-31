#!/bin/bash

# Script to run El Pais tests on BrowserStack
# Usage: ./run-browserstack-tests.sh

echo "=========================================="
echo "El Pais Scraper - BrowserStack Parallel Execution"
echo "=========================================="
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven first."
    exit 1
fi

# Check BrowserStack credentials
if [ -z "$BROWSERSTACK_USERNAME" ] || [ -z "$BROWSERSTACK_ACCESS_KEY" ]; then
    echo "❌ BrowserStack credentials not found!"
    echo ""
    echo "Please set the following environment variables:"
    echo "  export BROWSERSTACK_USERNAME='your_username'"
    echo "  export BROWSERSTACK_ACCESS_KEY='your_access_key'"
    echo ""
    echo "Get your credentials from: https://automate.browserstack.com/"
    echo "Sign up for free trial: https://www.browserstack.com/users/sign_up"
    exit 1
fi

echo "✅ BrowserStack credentials configured"
echo "   Username: $BROWSERSTACK_USERNAME"
echo ""

echo "🔍 Checking Java version..."
java -version

echo ""
echo "🧹 Cleaning previous build..."
mvn clean

echo ""
echo "📦 Compiling project..."
mvn compile test-compile

echo ""
echo "🚀 Running tests on BrowserStack (5 parallel threads)..."
echo "   - Windows 10 - Chrome Latest"
echo "   - Windows 11 - Edge Latest"
echo "   - macOS Ventura - Safari Latest"
echo "   - iPhone 14 Pro - iOS 16"
echo "   - Samsung Galaxy S23 - Android 13"
echo ""

mvn test -DsuiteXmlFile=testng-browserstack.xml

echo ""
echo "=========================================="
echo "✅ Test execution completed!"
echo "=========================================="
echo ""
echo "📊 View detailed results on BrowserStack Dashboard:"
echo "   https://automate.browserstack.com/dashboard"
echo ""
echo "📁 Local test reports: target/surefire-reports/"
echo "🖼️  Downloaded images: article_images_browserstack/"

