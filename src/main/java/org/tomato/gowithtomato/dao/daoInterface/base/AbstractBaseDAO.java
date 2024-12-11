package org.tomato.gowithtomato.dao.daoInterface.base;

import lombok.extern.slf4j.Slf4j;
import org.tomato.gowithtomato.exception.db.NullConnectionException;
import org.tomato.gowithtomato.mapper.RowMapper;
import org.tomato.gowithtomato.util.ConnectionManager;

import java.sql.Connection;

/*
Базовый абстрактный класс для всех DAO
 */
@Slf4j
public abstract class AbstractBaseDAO<T> {
    protected RowMapper<T> mapper;

    public Connection getConnection() {
        Connection connection = ConnectionManager.getInstance().get();
        if (connection == null) {
            log.error("Не удалось установить соединение с базой данных.");
            throw new NullConnectionException("Соединение не может быть установлено.");
        }
        return connection;
    }
}
