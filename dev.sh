set -e

echo "Starting docker-compose for dev..."
docker-compose -f docker-compose.yaml up -d

echo "Starting Quarkus in dev mode..."
./mvnw quarkus:dev
