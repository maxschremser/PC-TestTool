@echo on
setlocal
set COPYCMD=/Y

set CLASSPATH=K:\RUNTIME\JavaDev3rdParty\apache-ivy-2.0.0\ivy-2.0.0.jar;K:\RUNTIME\JavaDev3rdParty\apache-ivy-2.0.0\jsch-0.1.31.jar
set JAVA_HOME=K:\JavaRT2\3rdparty\jdk\win32\1.6.0_14
set ANT_HOME=K:\RUNTIME\JavaDev3rdParty\apache-ant-1.7.0
set TOMCAT_HOME=K:\RUNTIME\JavaDev3rdParty\apache-tomcat-6.0.16

set TOMCAT_BASE=j:\dist6

set WEB_HOME=j:\projects\TestTool\web

if (%1) == () set RDEBUG= -Xnoagent -Xdebug -Xrunjdwp:transport=dt_socket,address=20000,server=y,suspend=n

xcopy j:\projects\TestTool\webproj\tomcat6_config.xml %TOMCAT_BASE%\conf\server.xml

%ANT_HOME%\bin\ant.bat -DRDEBUG="%RDEBUG%" -DOPTS="%OPTS%" -DJAVA_HOME=%JAVA_HOME% -DANT_HOME=%ANT_HOME% -DTOMCAT_HOME=%TOMCAT_HOME% -DTOMCAT_BASE=%TOMCAT_BASE% run_tomcat6
