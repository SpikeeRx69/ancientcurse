@echo off
echo Building Ancient Curse Mod...

:: Create gradle wrapper if it doesn't exist
if not exist gradlew.bat (
    echo Gradle wrapper not found. Installing...
    call gradle wrapper
)

:: Generate Minecraft sources
echo Generating Minecraft sources...
call gradlew genSources

:: Build the mod
echo Building mod...
call gradlew build

:: Check if build was successful
if %ERRORLEVEL% == 0 (
    echo Build successful!
    echo JAR file created in build/libs/
) else (
    echo Build failed! Check the error messages above.
)

pause 