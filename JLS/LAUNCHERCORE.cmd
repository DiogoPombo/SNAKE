::----------------------------------------------------------------------------------------------------------------------------------------------------::
:: Customized JLauncher embedded!                                                                                                                     ::
:: This is a custom JLauncher for the project amd it will only work with this specific project and with the parameters defined by the project author. ::
::----------------------------------------------------------------------------------------------------------------------------------------------------::

::------------------------------------------::
:: Author: Diogo Santos Pombo - \Õ/ - @2026 ::
::------------------------------------------::

@echo off
setlocal enabledelayedexpansion

::MODIFICADO
set "APPNM=SNAKE"

for /f "tokens=* delims=" %%a in ('chcp') do (
    for %%b in (%%a) do set "original_cp=%%b"
)

chcp 65001 > nul

set MODE=%1
shift

goto :main

:pause_zero
if "%MODE%"=="-win11" (
    timeout /t 0 /nobreak >nul
) else (
    ping -n 1 127.0.0.1 >nul
)
goto :eof

:main
for /f "tokens=3" %%I in ('reg query "HKEY_CURRENT_USER\Control Panel\International" /v LocaleName') do set "LANGUAGE=%%I"
set "LANG_PREFIX=%LANGUAGE:~0,2%"
if /i "%LANG_PREFIX%"=="pt" (
    set "IDIOMA=PT"
) else (
    set "IDIOMA=EN"
)

if "%IDIOMA%"=="PT" (
    set "MSG_CANCELLED=Usuário cancelou a seleção."
    set "MSG_FILE_CHOSEN=Arquivo escolhido: "
    set "MSG_NO_URL=URL não fornecida."
    set "MSG_SKIP_URL=Pulando configuração de URL."
    set "MSG_NEEDS_URL=Essa aplicação precisa de uma URL para abrir no navegador?"
    set "MSG_NEEDS_URL_TITLE=Configuração"
    set "MSG_URL_PROMPT=Digite a URL:"
    set "MSG_URL_TITLE=URL"
    set "MSG_DEFAULT_URL=http://localhost:8080"
    set "MSG_ERROR_FILE_NOT_FOUND=[ERRO] Arquivo Java não encontrado: "
    set "MSG_LOG_RUNNING=[%date% %time%] Executando: "
    set "MSG_ANOTHER_LAUNCH=Deseja selecionar outro arquivo?"
    set "prompt=Insira uma URL"
    set "MSG_DELAY_PROMPT=Quantos segundos leva para a aplicação subir?"
    set "MSG_DELAY_TITLE=Tempo de Inicialização"
    set "MSG_DELAY_DEFAULT=10"
    set "MSG_INVALID_DELAY=Entrada inválida. Digite apenas um número inteiro."
    set "MSG_INVALID_DELAY_TITLE=Erro de Validação"
    set "MSG_WELCOME=Bem-vindo ao JLauncher, seu launcher simplificado para aplicações Java."
    set "MSG_PLS_SELECT=Por favor, selecione um arquivo .jar."
) else (
    set "MSG_CANCELLED=User canceled the selection."
    set "MSG_FILE_CHOSEN=File chosen: "
    set "MSG_NO_URL=No URL provided."
    set "MSG_SKIP_URL=Skipping URL configuration."
    set "MSG_NEEDS_URL=Does this application need a URL to open in the browser?"
    set "MSG_NEEDS_URL_TITLE=Configuration"
    set "MSG_URL_PROMPT=Type URL:"
    set "MSG_URL_TITLE=URL"
    set "MSG_DEFAULT_URL=http://localhost:8080"
    set "MSG_ERROR_FILE_NOT_FOUND=[ERROR] Java file not found: "
    set "MSG_LOG_RUNNING=[%date% %time%] Running: "
    set "MSG_ANOTHER_LAUNCH=Do you want to select another file?"
    set "prompt=Insert URL"
    set "MSG_DELAY_PROMPT=How many seconds does it take for the application to start?"
    set "MSG_DELAY_TITLE=Startup Time"
    set "MSG_DELAY_DEFAULT=10"
    set "MSG_INVALID_DELAY=Invalid input. Please enter an integer number only."
    set "MSG_INVALID_DELAY_TITLE=Validation Error"
    set "MSG_WELCOME=Welcome to JLauncher, your simplified launcher for Java applications."
    set "MSG_PLS_SELECT=Please select a .jar file."
)

:start
@title %APPNM%

set "DELAY_SECONDS=10"
set "BASE=%~dp0"
set "SOM=%BASE%play.vbs"
set "SOM2=%BASE%play2.vbs"

set "APP_URL="
set "SPACE= "

::MODIFICADO
set "JAVA_FILE_PATH=%BASE%..\target\Snake-0.0.1-SNAPSHOT.jar"

if "%1"=="-s" (
    goto silent) else if "%1"=="-S" (
    goto silent)

@title %APPNM%
wscript.exe "%SOM%"

set colors2=F7 F8 F0
set colors=00 80 70 F0

::MODIFICADO
timeout /t 2 /nobreak >nul
for %%c in (%colors%) do (
    color %%c
    call :pause_zero
)

:: Language-independent parsing for console dimensions using PowerShell
for /f %%a in ('powershell -command "$Host.UI.RawUI.WindowSize.Width"') do set COLS=%%a
for /f %%a in ('powershell -command "$Host.UI.RawUI.WindowSize.Height"') do set LINS=%%a

set /a LEN=75
set /a ALT=5

set /a PAD=(COLS - LEN) / 2
set "SPACES="
for /l %%i in (1,1,%PAD%) do set "SPACES=!SPACES! "

set /a PADV=(LINS - ALT) / 2
if "%MODE%"=="-win10" if %PADV% GTR 20 set /a PADV=20

set "L1=*****************************************************************************"
set "L2=*                                                                           *"

set "APPNAME=%APPNM%"

set /a NAMELEN=0
for /l %%i in (0,1,200) do (
    if "!APPNAME:~%%i,1!"=="" (
        set /a NAMELEN=%%i
        goto :lenFoundName
    )
)
:lenFoundName

set /a INNER=LEN-2
set /a PADNAME=(INNER - NAMELEN) / 2

set "SPACESNAME="
for /l %%i in (1,1,%PADNAME%) do set "SPACESNAME=!SPACESNAME! "

set "L3=*  %SPACESNAME%%APPNAME%"
set /a REMSP=INNER - (PADNAME + NAMELEN)
set "SPACESRIGHT="
for /l %%i in (1,1,%REMSP%) do set "SPACESRIGHT=!SPACESRIGHT! "
set "L3=!L3!!SPACESRIGHT!*"

set "L4=*                                                                           *"

call :pause_zero
for %%c in (%colors2%) do (
    color %%c
    cls

    for /l %%i in (1,1,%PADV%) do echo.

    echo !SPACES!!L1!
    echo !SPACES!!L2!
    echo !SPACES!!L3!
    echo !SPACES!!L4!
    echo !SPACES!!L1!

    call :pause_zero
)

::MODIFICADO
if "%MODE%"=="-win11" (
    timeout /t 5 /nobreak >nul
) else (
    timeout /t 6 /nobreak >nul
)

call :pause_zero
call :pause_zero
call :pause_zero
call :pause_zero
::MODIFICADO
::wscript.exe "%SOM2%"
color 0A
call :pause_zero
color 0B
call :pause_zero
color 0F
cls
call :pause_zero
call :pause_zero
call :pause_zero
::MODIFICADO
color 0B
echo.

set "LINE=**************************%SPACE%%APPNM%%SPACE%**************************"
set "LENLINE=0"
for /l %%i in (0,1,200) do (
    if "!LINE:~%%i,1!"=="" (
        set /a LENLINE=%%i
        goto :lenFound2
    )
)
:lenFound2

set /a PAD2=(COLS - LENLINE) / 2
set "SPACES2="
for /l %%i in (1,1,%PAD2%) do set "SPACES2=!SPACES2! "

echo !SPACES2!!LINE!
:silent
@title %APPNM%
chcp %original_cp% >nul
start "" /b java -jar "%JAVA_FILE_PATH%"
timeout /t %DELAY_SECONDS% /nobreak >nul

if not "!APP_URL!"=="" (
    start "%APPNM%" "msedge" --app="!APP_URL!"
        if not %errorlevel%==0 (
            start "" "!APP_URL!"
    )
)

:end
echo.
endlocal

exit