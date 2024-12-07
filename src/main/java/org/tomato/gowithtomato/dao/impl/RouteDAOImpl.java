package org.tomato.gowithtomato.dao.impl;

import org.tomato.gowithtomato.dao.daoInterface.RouteDAO;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.exception.db.UniqueSqlException;
import org.tomato.gowithtomato.mapper.RouteMapper;
import org.tomato.gowithtomato.mapper.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.tomato.gowithtomato.dao.query.RouteQueries.*;

public class RouteDAOImpl extends RouteDAO {
    private static final RouteDAOImpl INSTANCE = new RouteDAOImpl();
    private final PointDAOImpl pointDAO;
    private final RouteAndPointsDaoImpl routeAndPointsDao;
    private final RowMapper<Route> rowMapper;

    private RouteDAOImpl() {
        pointDAO = PointDAOImpl.getInstance();
        routeAndPointsDao = RouteAndPointsDaoImpl.getInstance();
        rowMapper = RouteMapper.getInstance();
    }
    public static RouteDAOImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Route> findById(Long id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(rowMapper.mapRow(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске маршрута по id.", e);
        }
    }


    @Override
    public Route save(Route route) {
        try {
            Long startPointId = saveOrFindPoint(route.getDeparturePoint());
            Long finishPointId = saveOrFindPoint(route.getDestinationPoint());

            long routeId = insertRoute(startPointId, finishPointId, route.getOwner().getId(), Math.round(route.getDistance()));
            route.setId(routeId);

            if (route.getOther() != null) {
                routeAndPointsDao.saveAll(route.getOther(), route.getId());
            }
            return route;
        } catch (SQLException e) {
            throw new DaoException("Ошибка при сохранении маршрута.", e);
        }
    }

    public List<Route> findByUserWithPagination(User user, int page) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     FIND_BY_USER_ID_SQL + String.format(" LIMIT %d OFFSET %d", LIMIT, LIMIT * (page - 1)))) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            List<Route> routes = new ArrayList<>();
            while (resultSet.next()) {
                routes.add(rowMapper.mapRow(resultSet));
            }
            return routes;
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске списка маршрутов пользователя", e);
        }
    }


    public long getCountPage(User user) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_SQL)) {
            statement.setLong(1, user.getId());
            Long count = convertResultSetToCountPages(statement.executeQuery());
            return (long) Math.ceil(count * 1.0 / LIMIT);
        } catch (SQLException e) {
            throw new DaoException("Ошибка при подсчете total_page", e);
        }
    }

    @Override
    public void update(Route entity) {
    }

    @Override
    public void delete(Long id) {
    }


    private Long saveOrFindPoint(Point point) throws SQLException {
        try {
            return pointDAO.save(point).getId();
        } catch (UniqueSqlException e) {
            Optional<Point> existingPoint = pointDAO.findByLatLng(point.getLat(), point.getLng());
            return existingPoint.orElseThrow(() -> new DaoException("Ошибка: точка не найдена и не может быть сохранена.")).getId();
        }
    }



    private long insertRoute(long startPointId, long finishPointId, long ownerId, long distance) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, startPointId);
            statement.setLong(2, finishPointId);
            statement.setDouble(3, distance);
            statement.setLong(4, ownerId);
            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                return keys.getLong("id");
            } else {
                throw new SQLException("Не удалось получить ID для маршрута.");
            }
        }
    }


    private Long convertResultSetToCountPages(ResultSet result) throws SQLException {
        if (result.next()) {
            return result.getLong("count");
        }
        throw new DaoException("Ошибка при поиске количества значений");
    }
}
