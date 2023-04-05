#!/bin/bash
# Script to propagate cert files and zoo.cfg to docker container, then restart.
# After running this the container should be properly configured for FIPS-
# compatible crypto. 
CONTAINER=${1:-zk}
set -x
docker stop ${CONTAINER}
echo $?
docker container rm ${CONTAINER}
echo $?
set -eu
docker run -d -p 2281:2281 \
 -e SERVER_JVMFLAGS="-Dfips.enabled=true -Dorg.bouncycastle.fips.approved_only=true -Djava.security.properties=/conf/fips.java.security" \
 --name ${CONTAINER} altinity/zookeeper-fips:3.7.1-1
sleep 1
docker cp ../certs ${CONTAINER}:/certs
docker cp zoo.cfg ${CONTAINER}:/conf
docker stop ${CONTAINER}
docker start --attach ${CONTAINER}
