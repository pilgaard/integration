mvn clean
mvn package
docker build -t peer .
docker run peer
