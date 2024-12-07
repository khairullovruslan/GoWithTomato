FROM maven:3.8.6-openjdk-17 AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем pom.xml и файл с исходным кодом
COPY pom.xml .
COPY src ./src

# Сборка проекта (создание файла WAR)
RUN mvn clean package -DskipTests

# Этап 2: Запуск на Tomcat
FROM tomcat:latest

# Копируем собранный файл WAR
COPY --from=build /app/target/GoWithTomato-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Открываем порт Tomcat
EXPOSE 8080