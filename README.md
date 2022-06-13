# FunnyClient
Just a Funny Client
# Make maven wrapper
```
mvn -N io.takari:maven:wrapper
```


# Source
with git
```
git clone https://github.com/Dashstrom/FunnyClient.git
cd FunnyClient
```

# Build Window

as administrator (not obliged)
```
where java
# replace your Path by jdk-X or jre-X
setx JAVA_HOME 'Path'
```

in normal terminal
```
.\mvnw -T 2C clean package -Dmaven.test.skip -DskipTests
```

# Build Linux
```
.\mvnw -T 2C clean package -Dmaven.test.skip -DskipTests
```

# Run
```
java -jar target/funny-server-1.0-jar-with-dependencies.jar
```