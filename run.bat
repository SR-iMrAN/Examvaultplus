@echo off
setlocal

REM ===================================================================
REM  ExamVaultPlus - Build and Run Script
REM  Requires: JDK at C:\Program Files\Java\jdk-26
REM            JavaFX SDK at C:\Program Files\Java\javafx
REM ===================================================================

set JDK=C:\Program Files\Java\jdk-26
set JAVAFX=C:\Program Files\Java\javafx
set JAVAC="%JDK%\bin\javac.exe"
set JAVA="%JDK%\bin\java.exe"
set FX_MODS=javafx.controls,javafx.fxml
set FX_PATH="%JAVAFX%\lib"
set SRC=src
set OUT=out
set ORACLE_JAR=lib\ojdbc8.jar

echo.
echo  =====================================================
echo   ExamVaultPlus - Building...
echo  =====================================================
echo.

REM Create output directories
if not exist "%OUT%" mkdir "%OUT%"
if not exist "%OUT%\css" mkdir "%OUT%\css"
if not exist "data" mkdir "data"
if not exist "data\subject" mkdir "data\subject"

REM Copy CSS
echo  [1/3] Copying resources...
copy /Y "%SRC%\css\style.css" "%OUT%\css\style.css" >nul

REM Compile
echo  [2/3] Compiling Java sources...
setlocal enabledelayedexpansion
set JAVALIST=
for /r "%SRC%" %%F in (*.java) do (
  set JAVALIST=!JAVALIST! "%%F"
)
%JAVAC% --module-path %FX_PATH% --add-modules %FX_MODS% -d "%OUT%" !JAVALIST!

if errorlevel 1 (
  echo.
  echo  [ERROR] Compilation failed. Check Java and JavaFX paths.
  echo  Expected JDK  : %JDK%
  echo  Expected FX   : %JAVAFX%
  pause
  exit /b 1
)

echo  [3/3] Launching ExamVaultPlus...
echo.

%JAVA% --enable-native-access=javafx.graphics ^
  --module-path %FX_PATH% ^
  --add-modules %FX_MODS% ^
  -cp "%OUT%;%ORACLE_JAR%" ^
  ExamVaultPlus

endlocal
