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

/**
 * Реализация интерфейса RouteDAO для управления маршрутами.
 */
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

    /**
     * Получает экземпляр синглтона RouteDAOImpl.
     *
     * @return экземпляр синглтона
     */
    public static RouteDAOImpl getInstance() {
        return INSTANCE;
    }

    /**
     * Находит маршрут по заданному ID.
     *
     * @param id ID маршрута
     * @return объект маршрут, если найдено, иначе пустой Optional
     * @throws DaoException если произошла ошибка при поиске маршрута
     */
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

    /**
     * Сохраняет маршрут.
     *
     * @param route объект маршрута
     * @return сохранённый объект маршрута
     * @throws DaoException если произошла ошибка при сохранении маршрута
     */
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

    /**
     * Находит маршруты пользователя с пагинацией.
     *
     * @param user объект пользователя
     * @param page номер страницы
     * @return список маршрутов пользователя
     * @throws DaoException если произошла ошибка при поиске маршрутов пользователя
     */
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

    /**
     * Получает количество страниц маршрутов для указанного пользователя.
     *
     * @param user объект пользователя
     * @return количество страниц
     * @throws DaoException если произошла ошибка при подсчете страниц
     */
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
    public Route update(Route entity) {
        return null;
    }

    @Override
    public void delete(Long id) {
    }

    /**
     * Получение ID обекъта, сохранение, если его нет в бд
     *
     * @param point Объект для сохранения
     * @return Возвращает ID для данного point
     * @throws SQLException если объект уже есть в бд
     */
    private Long saveOrFindPoint(Point point) throws SQLException {
        try {
            return pointDAO.save(point).getId();
        } catch (UniqueSqlException e) {
            Optional<Point> existingPoint = pointDAO.findByLatLng(point.getLat(), point.getLng());
            return existingPoint.orElseThrow(() -> new DaoException("Ошибка: точка не найдена и не может быть сохранена.")).getId();
        }
    }


    /**
     * Сохранение маршрута
     *
     * @param startPointId  ID начальной точки
     * @param finishPointId ID конечной точки
     * @param ownerId       ID владельца
     * @param distance      Дистанция маршрута
     * @return ID созданного маршрута
     * @throws SQLException если произошла ошибка в бд
     */
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

    /**
     * Конвертирование ResultSet в общее количество страниц
     *
     * @return количество страниц
     * @throws SQLException если возникнет ошибка в бд
     */
    private Long convertResultSetToCountPages(ResultSet result) throws SQLException {
        if (result.next()) {
            return result.getLong("count");
        }
        throw new DaoException("Ошибка при поиске количества значений");
    }
}
