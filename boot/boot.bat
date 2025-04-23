@ECHO OFF
SETLOCAL

SET DOWNLOAD_URL=https://github.com/inc0g-repoz/lix4j/releases/latest/download/lix4j.jar
SET LIX4J_PATH=%~dp0lix4j.jar

IF [%1]==[] (
    GOTO usage
)

IF NOT EXIST "%LIX4J_PATH%" (
    ECHO Downloading lix4j.jar...
    powershell -Command "(New-Object Net.WebClient).DownloadFile('%DOWNLOAD_URL%', '%LIX4J_PATH%')"

    IF EXIST "%LIX4J_PATH%" (
        ECHO Successfullly downloaded
    ) ELSE (
        ECHO Failed to download lix4j.jar
        EXIT /b 1
    )

    timeout /t 1 /nobreak>NUL
    CLS
)

java -jar "%~dp0lix4j.jar" %*
GOTO eof

:usage
ECHO Usage: %~n0 script_file [args...]

:eof
