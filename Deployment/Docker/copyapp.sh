#!/bin/bash

[ -d tmp ] || mkdir -v tmp

cp -v ../../Development/tfc/tfc-server/tfc-web-scheduler/target/tfc-web-scheduler.war ./tmp/
cp -v ../../Development/tfc/tfc-server/tfc-ws-rest/target/tfc-ws-rest.war ./tmp/
