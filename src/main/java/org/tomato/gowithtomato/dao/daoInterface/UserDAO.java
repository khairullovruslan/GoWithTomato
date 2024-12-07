package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractCrudDAO;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.db.DaoException;

import java.util.Optional;

public abstract class UserDAO extends AbstractCrudDAO<Long, User> {
    public abstract Optional<User> findByLogin(String login) throws DaoException;

    public abstract Optional<String> getPasswordByLogin(String login);

    public abstract Optional<User> findByEmail(String email);
}
