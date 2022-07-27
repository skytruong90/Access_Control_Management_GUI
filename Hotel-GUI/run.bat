@echo off
call mvn clean install && call java -jar target\access-control-system-1.0.0-SNAPSHOT.jar
echo.
set /p DUMMY=Press ENTER to exit...
