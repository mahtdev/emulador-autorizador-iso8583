#!/bin/bash

java -Dlog4j.configurationFile=./config/log.xml \
     -Dport=5000 \
     -Dip=localhost \
     -Dtype=client \
     -DisoType="PROSA" \
     -jar emulador_trx-1.3.0.0.jar &
