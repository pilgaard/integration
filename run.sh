cd ./Peer
mvn clean
mvn package
docker build -t peer .
cd ..
docker-compose up --build
