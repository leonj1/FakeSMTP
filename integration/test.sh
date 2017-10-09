#!/bin/bash

export CORE_NETWORK=core_net
export PROJECT=fakesmtp
export container=${PROJECT}; docker stop $container; docker rm $container

docker network create ${CORE_NETWORK}


docker run -d --name ${PROJECT} \
-p 8025:25 \
-p 9025:80 \
--net ${CORE_NETWORK} \
dapidi/${PROJECT}:latest

echo "Waiting for SMTP to come online"
while ! curl http://localhost:9025/health | grep -q 'OK'; do
  sleep 5
done


