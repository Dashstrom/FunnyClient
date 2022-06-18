# FunnyClient

Just a Funny Client for upload and download files from a Funny Server.

## Make maven wrapper

```sh
mvn -N io.takari:maven:wrapper
```

## Get source

```sh
git clone https://github.com/Dashstrom/FunnyClient.git
cd FunnyClient
```

## Setup java for window

as administrator (not obliged)

```sh
where java
# replace your Path by jdk-X or jre-X
setx JAVA_HOME 'Path'
```

## Build Linux

```bash
.\mvnw -T 2C clean package
```

## Run

```sh
java -jar target/funnyclient-1.0-jar-with-dependencies.jar
```
