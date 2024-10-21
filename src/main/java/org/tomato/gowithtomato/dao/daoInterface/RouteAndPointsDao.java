package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.BaseDAO;
import org.tomato.gowithtomato.entity.Point;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RouteAndPointsDao extends BaseDAO {
    void saveAll(Connection connection, List<Point> pointList, Long routeId) throws SQLException;
    List<Point> findByRouteId(Connection connection, Long id);
}
