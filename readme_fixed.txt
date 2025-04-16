FIXED TESTING INSTRUCTIONS FOR ANCIENT CURSE MOD
=============================================

IMPORTANT: Use these FIXED scripts instead of the originals!

FOLLOW THESE SIMPLE STEPS:

1. RUN SETUP_FIXED.BAT FIRST
   - Double-click setup_fixed.bat
   - This will download and set up Gradle properly
   - Wait for it to complete

2. RUN TEST_FIXED.BAT SECOND
   - Double-click test_fixed.bat
   - This will build your mod and test it
   - Choose 'y' when asked to run the client
   - The first run may take several minutes to download dependencies

3. IN-GAME TESTING:
   - Create a new creative world
   - Type '/ra_staff' in the chat
   - Check the "Ancient Curse" tab in creative inventory
   - Test the staff animations by holding it

TROUBLESHOOTING:
----------------
* If you see "Java not found" - Install Java 17 (JDK) and set JAVA_HOME environment variable
* If downloading fails - Check your internet connection
* If build fails - Read the error messages for details

JAVA_HOME SETUP (if needed):
---------------------------
1. Install JDK 17 from https://adoptium.net/
2. Right-click "This PC" → Properties → Advanced system settings
3. Click "Environment Variables"
4. Under System variables, click "New"
5. Variable name: JAVA_HOME
6. Variable value: Path to your JDK (e.g., C:\Program Files\Eclipse Adoptium\jdk-17.0.8.7-hotspot)
7. Click OK and restart your command prompt

REQUIREMENTS:
------------
* Java 17 JDK (or newer)
* Internet connection
* 3GB+ free memory 