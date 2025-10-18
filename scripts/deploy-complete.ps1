# Deploy Complete CollabChat Application to Tomcat
$TOMCAT_HOME = "C:\Users\tonyh\Downloads\apache-tomcat-9.0.110-windows-x64\apache-tomcat-9.0.110"
$SOURCE = "F:\SCET\SEM 3\JAVA\COLLABCHAT\WebContent"
$TARGET_DIR = "$TOMCAT_HOME\webapps\CollabChat"

Write-Host "Deploying Complete CollabChat Application to Tomcat..." -ForegroundColor Cyan

# Create target directory if it doesn't exist
if (-not (Test-Path $TARGET_DIR)) {
    New-Item -ItemType Directory -Force -Path $TARGET_DIR | Out-Null
    Write-Host "Created directory: $TARGET_DIR" -ForegroundColor Green
}

# Copy all web content
Write-Host "Copying web content..." -ForegroundColor Yellow
robocopy $SOURCE $TARGET_DIR /E /NFL /NDL /NJH /NJS /NC

# Copy compiled classes
$CLASSES_SOURCE = "F:\SCET\SEM 3\JAVA\COLLABCHAT\WebContent\WEB-INF\classes"
$CLASSES_TARGET = "$TARGET_DIR\WEB-INF\classes"

if (Test-Path $CLASSES_SOURCE) {
    Write-Host "Copying compiled classes..." -ForegroundColor Yellow
    robocopy $CLASSES_SOURCE $CLASSES_TARGET /E /NFL /NDL /NJH /NJS /NC
}

Write-Host "`nDeployment complete!" -ForegroundColor Green
Write-Host "Access your application at:" -ForegroundColor Yellow
Write-Host "  Main App: http://localhost:8080/CollabChat/" -ForegroundColor White
Write-Host "  Login: http://localhost:8080/CollabChat/login.jsp" -ForegroundColor White
Write-Host "  Register: http://localhost:8080/CollabChat/register.jsp" -ForegroundColor White
Write-Host "  Modern UI: http://localhost:8080/CollabChat/collabchat-fx.html" -ForegroundColor White

Write-Host "`nFeatures Available:" -ForegroundColor Cyan
Write-Host "  ✅ Modern responsive UI with animations" -ForegroundColor Green
Write-Host "  ✅ User registration and login with BCrypt" -ForegroundColor Green
Write-Host "  ✅ Group creation and joining" -ForegroundColor Green
Write-Host "  ✅ Real-time chat with enhanced UI" -ForegroundColor Green
Write-Host "  ✅ Collaborative whiteboard with tools" -ForegroundColor Green
Write-Host "  ✅ Educational games (Tic-Tac-Toe)" -ForegroundColor Green
Write-Host "  ✅ Live quiz system" -ForegroundColor Green
Write-Host "  ✅ Export chat messages" -ForegroundColor Green
Write-Host "  ✅ Browser notifications" -ForegroundColor Green
Write-Host "  ✅ Mobile responsive design" -ForegroundColor Green
