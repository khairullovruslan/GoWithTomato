FROM maven:3.8.3-openjdk-21 as build

# Копируем исходные файлы и POM в каталог приложения
COPY src /home/app/src
COPY pom.xml /home/app/  # Добавлен слеш для правильного пути

# Выполняем сборку проекта
RUN mvn -f /home/app/pom.xml clean package

FROM tomcat:9.0.65-jdk21-corretto

# Устанавливаем рабочий каталог для Tomcat
WORKDIR /usr/local/tomcat

# Копируем собранный .war файл в каталог веб-приложений Tomcat
COPY --from=build /home/app/target/GoWithTomato-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Открываем порт 8080
EXPOSE 8080

# Запускаем Tomcat
CMD ["catalina.sh", "run"]
