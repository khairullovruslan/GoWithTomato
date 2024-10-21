package org.tomato.gowithtomato.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.tomato.gowithtomato.util.PropertiesUtil;

@Slf4j
@WebListener
public class FlywayMigrationListener implements ServletContextListener {
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private final PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            String url = propertiesUtil.get(URL_KEY);
            String user = propertiesUtil.get(USERNAME_KEY);
            String password = propertiesUtil.get(PASSWORD_KEY);

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