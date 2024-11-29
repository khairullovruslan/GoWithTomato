package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractCrudDAO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.db.DaoException;

import java.util.Optional;

/**
 * Абстрактный интерфейс для работы с сущностями типа User
 */
public abstract class UserDAO extends AbstractCrudDAO<Long, User> {

    /**
     * Находит пользователя по логину.
     *
     * @param login Логин пользователя, который нужно найти
     * @return Объект Optional, содержащий найденного пользователя, или empty, если пользователь не найден
     * @throws DaoException если произошла ошибка при выполнении запроса к базе данных
     */
    public abstract Optional<User> findByLogin(String login) throws DaoException;

    public abstract Optional<String> getPasswordByLogin(String login);

    public abstract Optional<User> findByEmail(String email);
}
