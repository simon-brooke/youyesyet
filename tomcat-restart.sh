#!/bin/bash

# There's a problem with YouYesYet which causes Tomcat to fall over
# from time to time; it needs fixed. This is a temporary stopgap
# simon, 20180918

wget -S https://www.projecthope.scot 2>&1 | grep 'HTTP/1.1 5'

if [ "$?" = "0" ]
then
    header=`wget -S https://www.projecthope.scot 2>&1 | grep 'HTTP/1.1 5'`
    timestamp=`date`
    echo "${timestamp} : ${header} : Restarting Tomcat"
    service tomcat8 restart
fi

