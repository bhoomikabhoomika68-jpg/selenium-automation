#!/bin/bash

# Script to run El Pais tests locally
# Usage: ./run-local-tests.sh

echo "=========================================="
echo "El Pais Scraper - Local Test Execution"
echo "=========================================="
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed. Please install Maven first."
    exit 1
fi

echo "🔍 Checking Java version..."
java -version

echo ""
echo "🧹 Cleaning previous build..."
mvn clean

echo ""
echo "📦 Compiling project..."
mvn compile test-compile

echo ""
echo "🚀 Running tests on local Chrome browser..."
mvn test -DsuiteXmlFile=testng.xml

echo ""
echo "=========================================="
echo "✅ Test execution completed!"
echo "=========================================="
echo ""
echo "📊 Check test results in: target/surefire-reports/"
echo "🖼️  Downloaded images in: article_images/"

