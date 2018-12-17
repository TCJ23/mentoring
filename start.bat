git pull
call mvnw.cmd clean package -DskipTests
call java -jar target\devman-generator-ver-1.0.jar
pause