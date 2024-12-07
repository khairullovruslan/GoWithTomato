FROM maven:3.8.3-openjdk-17 as build

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM tomcat:9.0.65-jdk17-corretto

WORKDIR /usr/local/tomcat
COPY --from=build /home/app/target/GoWithTomato-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
