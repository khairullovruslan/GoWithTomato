package org.tomato.gowithtomato.dao.daoInterface.m2m;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractBaseDAO;
import org.tomato.gowithtomato.entity.Point;

import java.sql.SQLException;
import java.util.List;

public abstract class RouteAndPointsDao extends AbstractBaseDAO {
    public abstract void saveAll(List<Point> pointList, Long routeId) throws SQLException;

    public abstract List<Point> findByRouteId(Long id) throws SQLException;
}
