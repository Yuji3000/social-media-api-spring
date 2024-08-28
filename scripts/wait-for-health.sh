#!/bin/bash

TIMEOUT=60 # Timeout in seconds (e.g., 1 minute)
INTERVAL=5  # Interval between checks in seconds
START_TIME=$(date +%s)

while true; do
  CURRENT_TIME=$(date +%s)
  ELAPSED_TIME=$((CURRENT_TIME - START_TIME))

  if [ $ELAPSED_TIME -ge $TIMEOUT ]; then
    echo "Health check timed out after $TIMEOUT seconds."
    exit 1
  fi

  # Check the health endpoint
  HTTP_STATUS=$(curl -o /dev/null -s -w "%{http_code}\n" http://localhost:8080/actuator/health)
  if [ "$HTTP_STATUS" -eq 200 ]; then
    echo "Application is healthy."
    break
  else
    echo "Waiting for application to become healthy..."
  fi

  sleep $INTERVAL
done
