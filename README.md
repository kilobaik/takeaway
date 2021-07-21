# Takeaway Game

## Requirements
- [Docker](https://www.docker.com/products/docker-desktop)
- JDK 11 (Make sure you have `JAVA_HOME=$(path of JDK 11)`).
- Maven (Any compatible version).

To verify the requirements are satisfied run `mvn --version` command.
The output should be similar to the following output:

```shell
# MacOS

Apache Maven 3.8.1 (05c21c65bdfed0f71a2f2ada8b84da59348c4c5d)
Maven home: /usr/local/Cellar/maven/3.8.1/libexec
Java version: 11.0.11, vendor: AdoptOpenJDK, runtime: /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home
Default locale: en_DE, platform encoding: UTF-8
OS name: "mac os x", version: "11.4", arch: "x86_64", family: "mac"
```

## Kafka Cluster
### Starting up the cluster
The setup is done using `docker-compose` (Find the config file [here](docker-compose.yml)).
```shell
# Create and start containers
docker-compose up -d
```

### Configure the topics
Each client has a Kakfa topic as a receiving queue.

| Player     | Topic | Hint                         |
|------------|-------|------------------------------|
| `player-1` | `pot` | _Stand for 'PlayerOneTopic'_ |
| `player-2` | `ptt` | _Stand for 'PlayerTwoTopic'_ |

```shell
docker-compose exec kafka-1 kafka-topics --create \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1 \
    --topic pot

docker-compose exec kafka-1 kafka-topics --create \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1 \
    --topic ptt
```

## Player setup

Build the application using maven (JDK 11).
```shell
# JAVA_HOME environment variable MUST hav Java11

mvn clean package -Dlicense.skipAddThirdParty -DskipTests
```

### Player application properties

| Property                 | Possible values       | Explanation                                                           |
|--------------------------|-----------------------|-----------------------------------------------------------------------|
| `server.port`            | Any available port    | On which port the application is running.                             |
| `game.queues.downstream` | `pot`, `ptt`          | The kafka topic of the player. Where it receives the messages/events. |
| `game.queues.upstream`   | `pot`, `ptt`          | The kafka topic of the other player.                                  |
| `game.player.name`       | Any string            | Name of the player.                                                   |
| `game.initiator.mode`    | `automatic`, `manual` | The type of input:<br><br> - `automatic`: Both players will start automatically and bidirectionally.<br><br>- `manual`: The user should manually trigger on of the players to start the game by sending a `GET` request to `/initiate` endpoint of the player that is supposed to start (with an option request parameter `number`. However if the number is not provided a random `number` will be picked by the player implicitly). |


### Run the first player (Player-1)
```shell
java  -Dserver.port=8080 \
      -Dgame.queues.downstream='pot' \
      -Dgame.queues.upstream='ptt' \
      -Dgame.player.name='player-1' \
      -Dgame.initiator.mode='automatic' \ # Or 'manual'
      -jar target/challenge-0.0.1-SNAPSHOT.jar
```

### Run the second player (Player-2)
```shell
java  -Dserver.port=8081 \
      -Dgame.queues.downstream='ptt' \
      -Dgame.queues.upstream='pot' \
      -Dgame.player.name='player-2' \
      -Dgame.initiator.mode='automatic' \ # Or 'manual'
      -jar target/challenge-0.0.1-SNAPSHOT.jar
```