#!/bin/bash

[ -d tmp ] || mkdir -v tmp

cp -v ../../Development/ptd/ptd-server/ptd-web-scheduler/target/ptd-web-scheduler.war ./tmp/
cp -v ../../Development/ptd/ptd-server/ptd-ws-rest/target/ptd-ws-rest.war ./tmp/
cp -v ../../Deployment/Tls/keystore ./tmp/
