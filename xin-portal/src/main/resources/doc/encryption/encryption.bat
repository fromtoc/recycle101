@echo off
set/p input=（Input）：
set/p password=（Salt）：
echo waiting......
java -cp jasypt-1.9.2.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI  ^
input=%input% password=%password% ^
algorithm=PBEWithMD5AndDES
pause