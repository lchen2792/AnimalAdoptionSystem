#!/bin/bash

export GEMINI_API_KEY=$1

if [ -z "${GEMINI_API_KEY}" ];
then
  echo "failed to bring up the system because gemini api key is empty"
  sleep 10
  echo "exiting"
  exit
else
  cd $(dirname "${BASH_SOURCE[0]}")
  for path in *-service common;
  do
    echo "packaging $path"
    cd "$path" || exit
    mvn clean install -DskipTests || exit
    cd ../
  done
  echo "starting services"
  docker compose up
fi