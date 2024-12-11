package org.tomato.gowithtomato.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;

@Slf4j
public class ConnectionManager {
    private static final ConnectionManager INSTANCE = new ConnectionManager();
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";
    private static final int DEFAULT_POOL_SIZE = 10;
    private static final String POOL_SIZE_KEY = "db.pool.size";
    private static ArrayBlockingQueue<Connection> pool;
    private final PropertiesUtil propertiesUtil;

    private ConnectionManager() {
        propertiesUtil = PropertiesUtil.getInstance();
        try {
            initConnectionPool();
        } catch (Exception e) {

            log.error("Error connection");
        }
    }

    private void initConnectionPool() throws ClassNotFoundException {
        String poolSize = propertiesUtil.get(POOL_SIZE_KEY);
        int size = poolSize == null ? DEFAULT_POOL_SIZE : Integer.parseInt(poolSize);
        Class.forName("org.postgresql.Driver");
        pool = new ArrayBlockingQueue<>(size);
        log.info("connection added in pool...");
        for (int i = 0; i < size; i++) {
            Connection connection = open();
            ClassLoader connectionClassLoader = connection.getClass().getClassLoader();
            var proxyConnection = Proxy.newProxyInstance(connectionClassLoader,
                    new Class[]{Connection.class},
                    ((proxy, method, args) ->
                            method.getName().equals("close") ? pool.add((Connection) proxy) : method.invoke(connection, args
                            )));
            pool.offer((Connection) proxyConnection);
        }

    }

    private Connection open() {
        try {
            return DriverManager.getConnection(propertiesUtil.get(URL_KEY), propertiesUtil.get(USERNAME_KEY), propertiesUtil.get(PASSWORD_KEY));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection get() {
        try {
            return pool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int size() {
        return pool.size();
    }

    public static ConnectionManager getInstance() {
        return INSTANCE;
    }


}
