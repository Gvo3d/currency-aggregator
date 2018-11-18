#!/usr/bin/env bash
docker pull sath89/oracle-xe-11g
docker run --name currency -p 5433:5432 -e POSTGRES_USER=currency -e POSTGRES_PASSWORD=root -d postgres:9.6