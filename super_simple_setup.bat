@echo off
echo Setting up Gradle for Ancient Curse Mod...

:: Create directories
mkdir temp 2>nul
mkdir gradle\wrapper 2>nul

:: Download wrapper jar
echo Downloading Gradle wrapper JAR...
powershell -Command "& {Invoke-WebRequest -Uri 'https://github.com/gradle/gradle/raw/v8.1.1/gradle/wrapper/gradle-wrapper.jar' -OutFile 'gradle/wrapper/gradle-wrapper.jar'}"

:: Create wrapper properties file line by line
echo Creating gradle-wrapper.properties...
echo distributionBase=GRADLE_USER_HOME > gradle\wrapper\gradle-wrapper.properties
echo distributionPath=wrapper/dists >> gradle\wrapper\gradle-wrapper.properties
echo distributionUrl=https\://services.gradle.org/distributions/gradle-8.1.1-bin.zip >> gradle\wrapper\gradle-wrapper.properties
echo zipStoreBase=GRADLE_USER_HOME >> gradle\wrapper\gradle-wrapper.properties
echo zipStorePath=wrapper/dists >> gradle\wrapper\gradle-wrapper.properties

:: Download the complete gradlew.bat file
echo Downloading gradlew.bat...
powershell -Command "& {Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/gradle/gradle/v8.1.1/gradlew.bat' -OutFile 'gradlew.bat'}"

:: Download the gradlew script (useful for Git Bash or WSL users)
echo Downloading gradlew script...
powershell -Command "& {Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/gradle/gradle/v8.1.1/gradlew' -OutFile 'gradlew'}"

:: Make gradlew executable
attrib +x gradlew 2>nul

:: Check Java version
echo Checking Java version...
java -version 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo WARNING: Java not found in PATH. Make sure you have Java 17 installed.
    echo JAVA_HOME should be set to the Java installation directory.
) else (
    echo Java found. Proceeding...
)

echo.
echo Setup complete! Now run test_fixed.bat to build and test your mod.
pause 