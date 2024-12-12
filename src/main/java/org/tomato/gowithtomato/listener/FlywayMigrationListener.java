package org.tomato.gowithtomato.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;

@Slf4j
@WebListener
public class FlywayMigrationListener implements ServletContextListener {
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            String url = System.getenv(URL_KEY);
            String user = System.getenv(USERNAME_KEY);
            String password = System.getenv(PASSWORD_KEY);

            Flyway flyway = Flyway
                    .configure()
                    .dataSource(url, user, password)
                    .load();
            flyway.migrate();
            log.info("Миграции выполнены успешно!");
        } catch (Exception e) {
            log.error("Ошибка при выполнении миграций: ", e);
        }
    }


}