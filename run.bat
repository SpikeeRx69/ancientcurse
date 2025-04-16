@echo off
echo Running Ancient Curse Mod in development environment...

:: Create gradle wrapper if it doesn't exist
if not exist gradlew.bat (
    echo Gradle wrapper not found. Installing...
    call gradle wrapper
)

:: Run the client
echo Starting Minecraft with mod...
call gradlew runClient

pause 