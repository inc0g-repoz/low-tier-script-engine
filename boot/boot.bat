@ECHO OFF
SETLOCAL

:: Check if lix4j.jar exists in script's directory
IF NOT EXIST "%~dp0lix4j.jar" (
    ECHO Downloading lix4j.jar...
    powershell -Command "(New-Object Net.WebClient).DownloadFile('https://github.com/inc0g-repoz/lix4j/releases/latest/download/lix4j.jar', '%~dp0lix4j.jar')"

    :: Validating
    IF NOT EXIST "%~dp0lix4j.jar" (
        ECHO Failed to download lix4j.jar
        EXIT /b 1
    )
)

java -jar "%~dp0lix4j.jar" %*
