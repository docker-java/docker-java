@echo off
Setlocal EnableDelayedExpansion
echo %random% > %1

for /l %%i in (1,1,1000) do echo !random! >> %1

