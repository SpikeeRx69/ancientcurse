@echo off
echo ==========================================================
echo ANCIENT CURSE MOD - FINAL FIX SCRIPT
echo ==========================================================
echo.

:: Check if Java is installed
echo Checking for Java...
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java not found! Please install Java 17 JDK from adoptium.net
    echo After installing Java, run this script again.
    pause
    exit /b 1
) else (
    echo Java found! Continuing...
)

:: Create directories
mkdir gradle\wrapper 2>nul

:: Download wrapper jar
echo Downloading Gradle wrapper JAR...
powershell -Command "& {Invoke-WebRequest -Uri 'https://github.com/gradle/gradle/raw/v8.1.1/gradle/wrapper/gradle-wrapper.jar' -OutFile 'gradle/wrapper/gradle-wrapper.jar'}"

:: Create wrapper properties file with CORRECT URL FORMAT
echo Creating gradle-wrapper.properties with fixed URL...
(
echo distributionBase=GRADLE_USER_HOME
echo distributionPath=wrapper/dists
echo distributionUrl=https://services.gradle.org/distributions/gradle-8.1.1-bin.zip
echo zipStoreBase=GRADLE_USER_HOME
echo zipStorePath=wrapper/dists
) > gradle\wrapper\gradle-wrapper.properties

:: Download the complete gradlew.bat file
echo Downloading gradlew.bat...
powershell -Command "& {Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/gradle/gradle/v8.1.1/gradlew.bat' -OutFile 'gradlew.bat'}"

:: Download the gradlew script
echo Downloading gradlew script...
powershell -Command "& {Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/gradle/gradle/v8.1.1/gradlew' -OutFile 'gradlew'}"

:: Set up increased memory for Gradle
echo Setting up Gradle memory...
(
    echo # Memory settings for Gradle
    echo org.gradle.jvmargs=-Xmx3G
    echo org.gradle.daemon=false
    echo org.gradle.parallel=true
) > gradle.properties

echo Gradle setup complete!

echo.
echo STEP 2: Building the mod...
echo ==========================================================
echo This will take several minutes for the first run...
echo.

:: Generate sources
call gradlew genSources --no-daemon
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Failed to generate sources! See errors above.
    pause
    exit /b %ERRORLEVEL%
)

:: Build the mod
echo.
echo Building the mod...
call gradlew build --no-daemon
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Failed to build mod! See errors above.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo BUILD SUCCESSFUL!
echo Mod JAR file is located in: build\libs\
echo.

echo STEP 3: Run Minecraft with your mod
echo ==========================================================
echo.
echo Would you like to run Minecraft now to test your mod?
set /p RUNCLIENT="Enter y to launch Minecraft or n to exit (y/n): "
if /i "%RUNCLIENT%"=="y" (
    echo.
    echo Starting Minecraft with your mod...
    echo After Minecraft loads, create a creative world and type /ra_staff
    echo.
    call gradlew runClient --no-daemon
)

echo.
echo ==========================================================
echo TESTING COMPLETE!
echo ==========================================================
echo.
echo Remember:
echo - To test the Staff of Ra in-game, type '/ra_staff' in the chat
echo - To rebuild your mod after changes, run this batch file again
echo.
pause 