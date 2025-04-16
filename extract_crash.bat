@echo off
setlocal enabledelayedexpansion

echo Extracting crash information...
echo.

set "crashdir=run\crash-reports"
for /f "tokens=*" %%a in ('dir /b /o-d "%crashdir%\crash-*.txt"') do (
    set "latest=%%a"
    goto found
)

:found
echo Latest crash report: %latest%
echo.

echo === CRASH DESCRIPTION ===
findstr /C:"Description:" "%crashdir%\%latest%"
echo.

echo === CAUSED BY ===
findstr /C:"Caused by:" "%crashdir%\%latest%"
echo.

echo === MIXIN ERRORS ===
findstr /C:"Mixin" "%crashdir%\%latest%"
echo.

echo === FULL STACK TRACE ===
type "%crashdir%\%latest%"
echo.

echo Done.
