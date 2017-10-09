#!/bin/bash

SMTPPORT=${SMTPPORT:=25}
HTTPPORT=${HTTPPORT:=80}

java -jar /fakeSMTP.jar \
    --port=${SMTPPORT} \
    --http.server.port=${HTTPPORT}

