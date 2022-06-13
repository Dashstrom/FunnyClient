# FunnyClient

Just a Funny Client

## Make maven wrapper

```
mvn -N io.takari:maven:wrapper
```

## Get source

```
git clone https://github.com/Dashstrom/FunnyClient.git
cd FunnyClient
```

## Setup java for window

as administrator (not obliged)
```
where java
# replace your Path by jdk-X or jre-X
setx JAVA_HOME 'Path'
```

# Build Linux

```
.\mvnw -T 2C clean package
```

# Run

```
java -jar target/funny-server-1.0-jar-with-dependencies.jar
```