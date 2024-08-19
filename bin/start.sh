#!/bin/sh

cd /app/emulador

java -Dlog4j.configurationFile=/app/emulador/config/log4j2.xml \
     -Dport=5000 \
     -Dip=localhost \
     -Dtype=client \
     -DisoType="PROSA" \
     -jar emulador_trx-1.3.0.0.jar
