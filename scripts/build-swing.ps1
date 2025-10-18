<#
Builds a runnable CollabChat Swing client JAR.
Outputs:
  - collabchat-swing\lib\Java-WebSocket-1.5.3.jar (downloaded if missing)
  - collabchat-swing\out\ (compiled classes)
  - collabchat-swing\dist\CollabChatClient.jar (double-clickable JAR)
#>

$ErrorActionPreference = 'Stop'
Write-Host "Building CollabChat Swing client..." -ForegroundColor Cyan

$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$proj = Split-Path -Parent $root
$swing = Join-Path $proj 'collabchat-swing'
$libDir = Join-Path $swing 'lib'
$outDir = Join-Path $swing 'out'
$distDir = Join-Path $swing 'dist'
New-Item -ItemType Directory -Force -Path $libDir,$outDir,$distDir | Out-Null

# Ensure Java available
if (-not (Get-Command javac -ErrorAction SilentlyContinue)) {
  throw 'javac not found on PATH. Install JDK 17 and reopen PowerShell.'
}

# Download dependency if needed
$wsJar = Join-Path $libDir 'Java-WebSocket-1.5.3.jar'
$slf4jApi = Join-Path $libDir 'slf4j-api-2.0.9.jar'
$slf4jSimple = Join-Path $libDir 'slf4j-simple-2.0.9.jar'
if (-not (Test-Path $wsJar)) {
  Write-Host "Downloading Java-WebSocket..." -ForegroundColor Yellow
  $url = 'https://repo1.maven.org/maven2/org/java-websocket/Java-WebSocket/1.5.3/Java-WebSocket-1.5.3.jar'
  Invoke-WebRequest -Uri $url -OutFile $wsJar
}
if (-not (Test-Path $slf4jApi)) {
  Write-Host "Downloading SLF4J API..." -ForegroundColor Yellow
  Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar' -OutFile $slf4jApi
}
if (-not (Test-Path $slf4jSimple)) {
  Write-Host "Downloading SLF4J Simple..." -ForegroundColor Yellow
  Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar' -OutFile $slf4jSimple
}

# Compile sources
Write-Host "Compiling sources..." -ForegroundColor Yellow
Push-Location $swing
$cp = 'lib/Java-WebSocket-1.5.3.jar;lib/slf4j-api-2.0.9.jar;lib/slf4j-simple-2.0.9.jar'
javac -cp $cp -d "out" server/CollabServer.java client/*.java
Pop-Location

# Create manifest
$manifest = @(
  'Manifest-Version: 1.0',
  'Main-Class: client.CollabClient',
  'Class-Path: lib/Java-WebSocket-1.5.3.jar lib/slf4j-api-2.0.9.jar lib/slf4j-simple-2.0.9.jar'
) -join "`r`n"
$mfPath = Join-Path $swing 'MANIFEST.MF'
Set-Content -Path $mfPath -Value $manifest -Encoding ASCII

# Package jar
Write-Host "Packaging JAR..." -ForegroundColor Yellow
Push-Location $swing
jar cfm "dist/CollabChatClient.jar" "MANIFEST.MF" -C out .
Pop-Location

Write-Host "Done. Launch with scripts/run-swing-client.bat" -ForegroundColor Green

# Generate an .ico for the shortcut (PNG-compressed ICO)
try {
  Add-Type -AssemblyName System.Drawing
  $icoPng = Join-Path $swing 'dist\collabchat.png'
  $icoFile = Join-Path $swing 'dist\collabchat.ico'
  $bmp = New-Object System.Drawing.Bitmap 256,256
  $g = [System.Drawing.Graphics]::FromImage($bmp)
  $g.SmoothingMode = 'AntiAlias'
  $g.Clear([System.Drawing.Color]::FromArgb(255,41,128,185))
  # speech bubble
  $white = [System.Drawing.Brushes]::White
  $gp = New-Object System.Drawing.Drawing2D.GraphicsPath
  $gp.AddArc(30,40,30,30,180,90)
  $gp.AddArc(196,40,30,30,270,90)
  $gp.AddArc(196,150,30,30,0,90)
  $gp.AddArc(30,150,30,30,90,90)
  $gp.CloseFigure()
  $g.FillPath($white,$gp)
  # tail
  $pts = [System.Drawing.Point[]]@(
    (New-Object System.Drawing.Point -ArgumentList 90,180),
    (New-Object System.Drawing.Point -ArgumentList 110,220),
    (New-Object System.Drawing.Point -ArgumentList 130,180)
  )
  $g.FillPolygon($white, $pts)
  # small graduation cap
  $black = [System.Drawing.Brushes]::Black
  $cap = [System.Drawing.Point[]]@(
    (New-Object System.Drawing.Point -ArgumentList 120,70),
    (New-Object System.Drawing.Point -ArgumentList 180,90),
    (New-Object System.Drawing.Point -ArgumentList 120,110),
    (New-Object System.Drawing.Point -ArgumentList 60,90)
  )
  $g.FillPolygon($black, $cap)
  $g.Dispose()
  $bmp.Save($icoPng, [System.Drawing.Imaging.ImageFormat]::Png)
  # Write ICO header that embeds the PNG
  $pngBytes = [System.IO.File]::ReadAllBytes($icoPng)
  $fs = [System.IO.File]::Open($icoFile,[System.IO.FileMode]::Create)
  $bw = New-Object System.IO.BinaryWriter($fs)
  $bw.Write([UInt16]0)      # reserved
  $bw.Write([UInt16]1)      # image type = icon
  $bw.Write([UInt16]1)      # number of images
  $bw.Write([Byte]0)        # width (0 means 256)
  $bw.Write([Byte]0)        # height (0 means 256)
  $bw.Write([Byte]0)        # colors
  $bw.Write([Byte]0)        # reserved
  $bw.Write([UInt16]0)      # color planes
  $bw.Write([UInt16]32)     # bits per pixel
  $bw.Write([UInt32]$pngBytes.Length)  # size of image data
  $bw.Write([UInt32]22)     # offset (6+16)
  $bw.Write($pngBytes)
  $bw.Close(); $fs.Close()
} catch { Write-Warning "Icon generation failed: $_" }

# Create Desktop shortcut (uses javaw.exe to avoid console window)
try {
  $desktop = [Environment]::GetFolderPath('Desktop')
  $lnk = Join-Path $desktop 'CollabChat Client.lnk'
  $shell = New-Object -ComObject WScript.Shell
  $sc = $shell.CreateShortcut($lnk)
  $javaw = (Get-Command javaw -ErrorAction SilentlyContinue).Source
  if (-not $javaw) { $javaw = Join-Path $env:JAVA_HOME 'bin\javaw.exe' }
  $jarPath = Join-Path $swing 'dist\CollabChatClient.jar'
  $sc.TargetPath = $javaw
  $sc.Arguments = "-jar `"$jarPath`""
  $sc.WorkingDirectory = $swing
  $sc.IconLocation = (Join-Path $swing 'dist\collabchat.ico')
  $sc.Save()
  Write-Host "Desktop shortcut created: $lnk" -ForegroundColor Green
} catch { Write-Warning "Could not create desktop shortcut: $_" }


