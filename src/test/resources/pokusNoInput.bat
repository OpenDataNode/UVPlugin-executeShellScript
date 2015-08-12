@echo off
set conf=%1
echo conf %conf%
echo outDir %2
type %conf%
for /f "tokens=1,*" %%a in (%conf%) do (
	echo Creating file "%2\%%a"
	copy NUL "%2\%%a"
	set zvysok=%%b
)
:loop
for /f "tokens=1,*" %%c in ("%zvysok%") do (
	echo Creating file "%2\%%c"
	copy NUL "%2\%%c"
	set zvysok=%%d
	if defined zvysok (
	if "!zvysok!"=="" GOTO END
		GOTO loop
	) else (
		GOTO END
	)
)
:END
