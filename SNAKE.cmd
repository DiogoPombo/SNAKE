::----------------------------------------------------------------------------------------------------------------------------------------------------::
:: Customized JLauncher embedded!                                                                                                                     ::
:: This is a custom JLauncher for the project amd it will only work with this specific project and with the parameters defined by the project author. ::
::----------------------------------------------------------------------------------------------------------------------------------------------------::

::------------------------------------------::
:: Author: Diogo Santos Pombo - \Õ/ - @2026 ::
::------------------------------------------::

@echo off

setlocal enabledelayedexpansion

java -version >nul 2>&1
if not %errorlevel%==0 (
    COLOR 0C
    echo ============================================================
    echo [ERROR] Java not found.
    echo Please install Java before running JLauncher.
    echo ============================================================
    timeout /t 5 /nobreak >nul
    goto end
)

set "BASESCRIPTFILE=%~dp0\JLS\"

if not exist "%BASESCRIPTFILE%" (
    echo [ERROR] Folder JLS not found: %BASESCRIPTFILE%
    timeout /t 5 /nobreak >nul
    goto end
)

cd /d "%BASESCRIPTFILE%"

set "LAUNCHERCORE=%BASESCRIPTFILE%LAUNCHERCORE.cmd"

if not exist "%LAUNCHERCORE%" (
    echo [ERROR] LAUNCHERCORE.cmd not found at: %LAUNCHERCORE%
    timeout /t 5 /nobreak >nul
    goto end
)

for /f "tokens=2 delims==" %%i in ('wmic os get BuildNumber /value 2^>nul ^| findstr /b /c:"BuildNumber="') do set "BUILD=%%i"
set "BUILD=%BUILD: =%"
if "%BUILD%"=="" set "BUILD=0"
set "WINDOWS_VERSION=Win10"

if %BUILD% GEQ 22000 (
    set "WINDOWS_VERSION=Win11+"
    set "VERSION_PARAM=-win11"
) else (
    set "WINDOWS_VERSION=Win10-"
    set "VERSION_PARAM=-win10"
)

if "%1"=="-s" (
    call "%LAUNCHERCORE%" %VERSION_PARAM% -s
) else if "%1"=="-S" (
    call "%LAUNCHERCORE%" %VERSION_PARAM% -S
) else (
    call "%LAUNCHERCORE%" %VERSION_PARAM%
)

:end
endlocal

exit /b