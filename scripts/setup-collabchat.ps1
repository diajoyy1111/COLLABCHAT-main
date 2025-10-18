<#
Setup script for CollabChat (PowerShell)
This script will:
 - create an app DB user and grant privileges& $mysqlExe -u $rootUser -p$plainRootPass -e $createUserCmd
 - optionally import the schema
 - copy required jars into WebContent/WEB-INF/lib
 - optionally deploy the exploded webapp into a local Tomcat

Usage (run from project root):
    powershell -ExecutionPolicy Bypass -File .\scripts\setup-collabchat.ps1

You will be prompted for required paths and passwords. Do NOT store secrets in scripts for production.
#>

Write-Host "CollabChat setup script" -ForegroundColor Cyan

# Prompt for database info
$mysqlExe = Read-Host "Full path to mysql.exe (press Enter to use 'mysql' from PATH)"
if ([string]::IsNullOrWhiteSpace($mysqlExe)) { $mysqlExe = 'mysql' }

$rootUser = Read-Host "MySQL admin user (default: root)"; if ([string]::IsNullOrWhiteSpace($rootUser)) { $rootUser = 'root' }
$rootPass = Read-Host "MySQL admin password (will be used on command line)" -AsSecureString
# convert secure string to plain for command-line use (local only)
$BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($rootPass)
$plainRootPass = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)

$appUser = Read-Host "App DB username (default: appuser)"; if ([string]::IsNullOrWhiteSpace($appUser)) { $appUser = 'appuser' }
$appPass = Read-Host "App DB password (default: AppPass123)"; if ([string]::IsNullOrWhiteSpace($appPass)) { $appPass = 'AppPass123' }

$doImport = Read-Host "Import schema from db/schema.sql? (Y/n)"; if ([string]::IsNullOrWhiteSpace($doImport)) { $doImport = 'Y' }

# Create user and grant privileges for localhost and 127.0.0.1
Write-Host "Creating user and granting privileges..." -ForegroundColor Yellow
$createUserCmd = "CREATE USER IF NOT EXISTS '$appUser'@'localhost' IDENTIFIED BY '$appPass';"
$grantCmd = "GRANT SELECT, INSERT, UPDATE, DELETE ON collabchat.* TO '$appUser'@'localhost'; FLUSH PRIVILEGES;"

& $mysqlExe -u $rootUser -p$plainRootPass -e $grantCmd

# Also create/grant for 127.0.0.1 to avoid client differences
& $mysqlExe -u $rootUser -p$plainRootPass -e "CREATE USER IF NOT EXISTS '$appUser'@'127.0.0.1' IDENTIFIED BY '$appPass';"
& $mysqlExe -u $rootUser -p$plainRootPass -e "GRANT SELECT, INSERT, UPDATE, DELETE ON collabchat.* TO '$appUser'@'127.0.0.1'; FLUSH PRIVILEGES;"

if ($doImport -match '^[Yy]') {
    Write-Host "Importing schema db/schema.sql..." -ForegroundColor Yellow
    $schemaPath = Join-Path -Path (Get-Location) -ChildPath 'db\schema.sql'
    if (-Not (Test-Path $schemaPath)) { Write-Error "schema.sql not found at $schemaPath"; exit 1 }
    # Use piping instead of shell redirection which PowerShell doesn't support with external commands
    Get-Content -Raw $schemaPath | & $mysqlExe -u $rootUser -p$plainRootPass collabchat
    Write-Host "Schema import finished." -ForegroundColor Green
}

# Copy jars into WebContent/WEB-INF/lib
Write-Host "Now copy required jars into WebContent/WEB-INF/lib." -ForegroundColor Yellow
$libDir = Join-Path -Path (Get-Location) -ChildPath 'WebContent\WEB-INF\lib'
New-Item -ItemType Directory -Force -Path $libDir | Out-Null

$mysqlJar = Read-Host "Full path to mysql-connector-java jar (press Enter to skip copy)"
if (-Not [string]::IsNullOrWhiteSpace($mysqlJar)) {
    Copy-Item -Force $mysqlJar -Destination $libDir
    Write-Host "Copied mysql jar to $libDir" -ForegroundColor Green
}
$jbcryptJar = Read-Host "Full path to jbcrypt jar (press Enter to skip copy)"
if (-Not [string]::IsNullOrWhiteSpace($jbcryptJar)) {
    Copy-Item -Force $jbcryptJar -Destination $libDir
    Write-Host "Copied jbcrypt jar to $libDir" -ForegroundColor Green
}

# Optional: deploy to Tomcat
$doDeploy = Read-Host "Deploy exploded webapp to local Tomcat? (provide Tomcat home path or press Enter to skip)"
if (-Not [string]::IsNullOrWhiteSpace($doDeploy)) {
    $tomcatHome = $doDeploy
    $target = Join-Path -Path $tomcatHome -ChildPath 'webapps\CollabChat'
    Write-Host "Copying WebContent to $target ..." -ForegroundColor Yellow
    Copy-Item -Recurse -Force -Path (Join-Path (Get-Location) 'WebContent\*') -Destination $target
    Write-Host "Deployed exploded webapp to Tomcat." -ForegroundColor Green
    Write-Host "Starting Tomcat..." -ForegroundColor Yellow
    & (Join-Path $tomcatHome 'bin\startup.bat')
}

Write-Host "Setup script finished. Update src\com\collabchat\util\DBUtil.java to use the app DB credentials if needed." -ForegroundColor Cyan
