#!/usr/bin/env bash
docker pull sath89/oracle-xe-11g
docker run --name currencydb -d -p 8081:8080 -p 1521:1521 -e DEFAULT_SYS_PASS=currency sath89/oracle-xe-11g