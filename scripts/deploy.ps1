# Deploy Complete CollabChat Application to Tomcat
$TOMCAT_HOME = $env:CATALINA_HOME
if (-not $TOMCAT_HOME) {
    $TOMCAT_HOME = "C:\Program Files\Apache Software Foundation\Tomcat 9.0"
    if (-not (Test-Path $TOMCAT_HOME)) {
        Write-Host "ERROR: Tomcat not found. Please set CATALINA_HOME environment variable." -ForegroundColor Red
        exit 1
    }
}

$PROJECT_ROOT = Split-Path -Parent $PSScriptRoot
$SOURCE = Join-Path $PROJECT_ROOT "WebContent"
$TARGET_DIR = Join-Path $TOMCAT_HOME "webapps\CollabChat"

Write-Host "Deploying Complete CollabChat Application to Tomcat..." -ForegroundColor Cyan
Write-Host "From: $SOURCE" -ForegroundColor Yellow
Write-Host "To: $TARGET_DIR" -ForegroundColor Yellow

# Stop Tomcat if running
$tomcatService = Get-Service -Name "Tomcat*" -ErrorAction SilentlyContinue
if ($tomcatService -and $tomcatService.Status -eq 'Running') {
    Write-Host "Stopping Tomcat service..." -ForegroundColor Yellow
    Stop-Service $tomcatService.Name
    Start-Sleep -Seconds 5
}

# Create target directory if it doesn't exist
if (-not (Test-Path $TARGET_DIR)) {
    New-Item -ItemType Directory -Force -Path $TARGET_DIR | Out-Null
    Write-Host "Created directory: $TARGET_DIR" -ForegroundColor Green
}

# Copy all web content
Write-Host "Copying web content..." -ForegroundColor Yellow
Copy-Item "$SOURCE\*" -Destination $TARGET_DIR -Recurse -Force

# Create WEB-INF\classes if it doesn't exist
$CLASSES_TARGET = Join-Path $TARGET_DIR "WEB-INF\classes"
if (-not (Test-Path $CLASSES_TARGET)) {
    New-Item -ItemType Directory -Force -Path $CLASSES_TARGET | Out-Null
}

# Compile Java files
Write-Host "Compiling Java files..." -ForegroundColor Yellow
$javaFiles = Get-ChildItem -Path (Join-Path $PROJECT_ROOT "src") -Filter "*.java" -Recurse
$classpath = "$TOMCAT_HOME\lib\servlet-api.jar;$TOMCAT_HOME\lib\*"

# Create temp directory for compilation
$tempDir = Join-Path $env:TEMP "collabchat_compile"
if (-not (Test-Path $tempDir)) {
    New-Item -ItemType Directory -Force -Path $tempDir | Out-Null
}

# Compile all Java files
javac -cp $classpath -d $tempDir $javaFiles.FullName

if ($LASTEXITCODE -eq 0) {
    Write-Host "Compilation successful!" -ForegroundColor Green
    # Copy compiled classes
    Copy-Item "$tempDir\*" -Destination $CLASSES_TARGET -Recurse -Force
} else {
    Write-Host "Compilation failed!" -ForegroundColor Red
    exit 1
}

# Clean up temp directory
Remove-Item -Path $tempDir -Recurse -Force

# Start Tomcat
if ($tomcatService) {
    Write-Host "Starting Tomcat service..." -ForegroundColor Yellow
    Start-Service $tomcatService.Name
    Start-Sleep -Seconds 5
}

Write-Host "`nDeployment complete!" -ForegroundColor Green
Write-Host "Access your application at:" -ForegroundColor Yellow
Write-Host "  Main App: http://localhost:8080/CollabChat/collabchat-fx.html" -ForegroundColor White
Write-Host "  Login: http://localhost:8080/CollabChat/login.jsp" -ForegroundColor White