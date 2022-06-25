git pull
docker-compose -f docker-services/docker-compose.yml stop -d anychat
mvn spring-boot:build-image
docker-compose -f docker-services/docker-compose.yml up -d anychat