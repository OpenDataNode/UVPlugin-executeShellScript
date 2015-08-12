@echo off
for /f %%a in (%1) do (
	set param=%%a
)
echo arg1 %1
echo arg2 %2
set "arg3=%3\"
echo arg3 %arg3%
echo param %param%
echo inputpath %inputPath%
for /f %%i in (%2) do (
	echo Copying "%%i" to "%arg3%%param%%%~ni%%~xi"
    copy "%%i" "%arg3%%param%%%~ni%%~xi"
)