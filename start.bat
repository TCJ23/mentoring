git pull
call mvnw.cmd clean package -DskipTests
call java -jar target\devman-generator-1.0-SNAPSHOT.jar
pause