@echo off
setlocal
set SCRIPT_DIR=%~dp0
pushd "%SCRIPT_DIR%..\collabchat-swing"
if not exist "dist\CollabChatClient.jar" (
  echo Build not found. Running build...
  powershell -ExecutionPolicy Bypass -File "%SCRIPT_DIR%build-swing.ps1" || goto :eof
)
echo Starting CollabChat Client...
java -jar "dist\CollabChatClient.jar"
popd
endlocal

