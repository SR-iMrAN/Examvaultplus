# ExamVaultPlus - PowerShell Build Script
# Usage: Right-click -> Run with PowerShell

$JDK    = "C:\Program Files\Java\jdk-26"
$JAVAFX = "C:\Program Files\Java\javafx"
$JAVAC  = "$JDK\bin\javac.exe"
$JAVA   = "$JDK\bin\java.exe"
$FXMODS = "javafx.controls,javafx.fxml"
$FXPATH = "$JAVAFX\lib"
$SRC    = "src"
$OUT    = "out"

Write-Host ""
Write-Host " =====================================================" -ForegroundColor Cyan
Write-Host "   ExamVaultPlus - PowerShell Build Script" -ForegroundColor Cyan
Write-Host " =====================================================" -ForegroundColor Cyan
Write-Host ""

# Create directories
New-Item -ItemType Directory -Force -Path $OUT | Out-Null
New-Item -ItemType Directory -Force -Path "$OUT\css" | Out-Null
New-Item -ItemType Directory -Force -Path "data" | Out-Null
New-Item -ItemType Directory -Force -Path "data\subject" | Out-Null

# Copy CSS
Write-Host " [1/3] Copying resources..." -ForegroundColor Yellow
Copy-Item "$SRC\css\style.css" "$OUT\css\style.css" -Force

# Collect all .java files
Write-Host " [2/3] Compiling sources..." -ForegroundColor Yellow
$sources = Get-ChildItem -Path $SRC -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }

& $JAVAC --module-path $FXPATH --add-modules $FXMODS -d $OUT @sources

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host " [ERROR] Compilation failed." -ForegroundColor Red
    Write-Host " JDK    : $JDK" -ForegroundColor Red
    Write-Host " JavaFX : $JAVAFX" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host " [3/3] Launching ExamVaultPlus..." -ForegroundColor Green
Write-Host ""

& $JAVA `
    --enable-native-access=javafx.graphics `
    --module-path $FXPATH `
    --add-modules $FXMODS `
    -cp $OUT `
    ExamVaultPlus
