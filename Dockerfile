# Используем Maven с Amazon Corretto (OpenJDK 21)
FROM maven:3.9.9-amazoncorretto-21 as build

# Копируем исходные файлы и POM в каталог приложения
COPY src /home/app/src
COPY pom.xml /home/app/

# Выполняем сборку проекта
RUN mvn -f /home/app/pom.xml clean package

# Используем Tomcat 10 с JRE 21 (вместо jdk21-corretto)
FROM tomcat:10.1.33-jdk21-temurin

# Устанавливаем рабочий каталог для Tomcat
WORKDIR /usr/local/tomcat

# Копируем собранный .war файл в каталог веб-приложений Tomcat
COPY --from=build /home/app/target/GoWithTomato-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Открываем порт 8080
EXPOSE 8080

# Запускаем Tomcat
CMD ["catalina.sh", "run"]
