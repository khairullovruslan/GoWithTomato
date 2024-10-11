package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.exception.DaoException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface PointDao {
    Point save(Connection connection, Point entity) throws DaoException, SQLException;

    Optional<Point> findByLatLng(Connection connection, Double lat, Double lng) throws SQLException;
}
