#!/bin/bash

SMTPPORT=${SMTPPORT:=25}
HTTPPORT=${HTTPPORT:=80}

java -jar /authenticator.jar \
    --port=${SMTPNAME} \
    --http.server.port=${HTTPPORT}

