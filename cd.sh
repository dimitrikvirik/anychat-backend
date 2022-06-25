#!/bin/bash
git checkout master
git pull origin master
docker-compose -f docker-services/docker-compose.yml stop anychat
mvn spring-boot:build-image
docker-compose -f docker-services/docker-compose.yml up -d anychat