#!/bin/bash
# Run docker build with current directory as context. 
echo "run 'mvn clean install' before running this command."
set -x
docker build -t altinity/zookeeper-fips:3.7.1-1 .
