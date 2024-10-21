package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.CrudDao;
import org.tomato.gowithtomato.entity.User;

import java.util.Optional;

public interface UserDAO extends CrudDao<Long, User> {
    Optional<User> findByLogin(String login);
}
