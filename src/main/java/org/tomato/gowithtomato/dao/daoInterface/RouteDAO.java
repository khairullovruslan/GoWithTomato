package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractCrudDAO;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.entity.User;

import java.util.List;

public abstract class RouteDAO extends AbstractCrudDAO<Long, Route> {
    public abstract List<Route> findByUserWithPagination(User user, int pageNumber);

    public abstract long getCountPage(User user);
}
