# Deploy CollabChat HTML to Tomcat
$TOMCAT_HOME = "C:\Users\tonyh\Downloads\apache-tomcat-9.0.110-windows-x64\apache-tomcat-9.0.110"
$SOURCE = "F:\SCET\SEM 3\JAVA\COLLABCHAT\WebContent\collabchat-fx.html"
$TARGET_DIR = "$TOMCAT_HOME\webapps\CollabChat"

Write-Host "Deploying CollabChat HTML to Tomcat..." -ForegroundColor Cyan

# Create target directory if it doesn't exist
if (-not (Test-Path $TARGET_DIR)) {
    New-Item -ItemType Directory -Force -Path $TARGET_DIR | Out-Null
    Write-Host "Created directory: $TARGET_DIR" -ForegroundColor Green
}

# Copy HTML file
Copy-Item -Force $SOURCE -Destination $TARGET_DIR
Write-Host "Copied: collabchat-fx.html -> $TARGET_DIR" -ForegroundColor Green

Write-Host "`nDeployment complete!" -ForegroundColor Green
Write-Host "Access at: http://localhost:8080/CollabChat/collabchat-fx.html" -ForegroundColor Yellow

