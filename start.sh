#!/bin/bash

export GEMINI_API_KEY=$1

if [ -z "${GEMINI_API_KEY}" ];
then
  echo "failed to bring up the system because gemini api key is empty"
  sleep 10
  echo "exiting"
  exit
else
  echo "starting services"
  docker compose up
fi