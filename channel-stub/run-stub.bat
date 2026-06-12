@echo off
echo Compiling ChannelStubServer...
javac ChannelStubServer.java
if %ERRORLEVEL% NEQ 0 (
    echo Failed to compile.
    exit /b %ERRORLEVEL%
)
echo Starting ChannelStubServer...
java ChannelStubServer
