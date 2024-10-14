package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.entity.User;

import java.util.List;

public interface RouteDAO extends CrudDao<Long, Route> {
    List<Route> findByUser(User id);
}
