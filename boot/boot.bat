@ECHO OFF
SETLOCAL

IF NOT EXIST "%~dp0lix4j.jar" (
    ECHO Downloading lix4j.jar...
    powershell -Command "(New-Object Net.WebClient).DownloadFile('https://github.com/inc0g-repoz/lix4j/releases/latest/download/lix4j.jar', '%~dp0lix4j.jar')"

    IF EXIST "%~dp0lix4j.jar" (
        ECHO Successfullly downloaded
    ) ELSE (
        ECHO Failed to download lix4j.jar
        EXIT /b 1
    )

    timeout /t 1 /nobreak>NUL
    CLS
)

java -jar "%~dp0lix4j.jar" %*
