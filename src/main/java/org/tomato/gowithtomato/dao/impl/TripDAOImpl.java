package org.tomato.gowithtomato.dao.impl;

import lombok.SneakyThrows;
import org.tomato.gowithtomato.dao.daoInterface.TripDAO;
import org.tomato.gowithtomato.dto.FilterQueriesDTO;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.entity.TripStatus;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.mapper.TripMapper;
import org.tomato.gowithtomato.util.FilterTripDaoUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.tomato.gowithtomato.dao.query.TripQueries.*;

/**
 * Реализация интерфейса TripDAO для управления поездками.
 */
public class TripDAOImpl extends TripDAO {
    private static final TripDAOImpl INSTANCE = new TripDAOImpl();
    private final FilterTripDaoUtil filterUtil;

    private TripDAOImpl() {
        filterUtil = FilterTripDaoUtil.getInstance();
        mapper = TripMapper.getInstance();
    }

    /**
     * Получает экземпляр синглтона TripDAOImpl.
     *
     * @return экземпляр синглтона
     */
    public static TripDAOImpl getInstance() {
        return INSTANCE;
    }

    /**
     * Сохраняет поездку с заданным ID маршрута.
     *
     * @param trip объект поездки
     * @param id   ID маршрута
     * @return сохранённый объект поездки с присвоенным ID
     * @throws DaoException если произошла ошибка при сохранении поездки
     */
    public Trip saveWithRouteId(Trip trip, Long id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, trip.getOwner().getId());
            statement.setLong(2, id);
            statement.setObject(3, trip.getTripDateTime());
            statement.setInt(4, trip.getAvailableSeats());
            statement.setBigDecimal(5, trip.getPrice());
            statement.setObject(6, TripStatus.available, Types.OTHER);
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                trip.setId(keys.getLong("id"));
                return trip;
            }
            throw new DaoException("Не удалось получить ID для новой поездки.");
        } catch (SQLException e) {
            throw new DaoException("Ошибка при сохранении поездки.", e);
        }
    }

    @Override
    public long getCountByUserId(Long id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(COUNT_BY_OWNER_ID_SQL)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            return convertResultSetToCountByUserId(resultSet);
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске поездки по ID: " + id, e);
        }
    }

    /**
     * Находит поездку по заданному ID.
     *
     * @param id ID поездки
     * @return объект поездки, если найдено, иначе пустой Optional
     * @throws DaoException если произошла ошибка при поиске поездки
     */
    @Override
    public Optional<Trip> findById(Long id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.ofNullable(mapper.mapRow(resultSet));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске поездки по ID: " + id, e);
        }
    }

    /**
     * Находит все поездки.
     *
     * @return список всех поездок
     * @throws DaoException если произошла ошибка при получении поездок
     */
    public List<Trip> findAll() {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL)) {

            return getTripByResultSet(statement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException("Ошибка при получении всех поездок", e);
        }
    }

    @Override
    public Trip save(Trip entity) throws DaoException {
        return null;
    }

    @Override
    public Trip update(Trip entity) {
        return null;
    }

    @Override
    public void delete(Long id) {
        // Реализация удаления поездки может быть добавлена здесь
    }

    /**
     * Находит все поездки по заданным фильтрам.
     *
     * @param filter карта с фильтрами
     * @return список поездок, соответствующих фильтру
     * @throws DaoException если произошла ошибка при применении фильтра
     */
    @SneakyThrows
    public List<Trip> findAllByFilter(Map<String, String> filter) {
        try (Connection connection = getConnection()) {
            FilterQueriesDTO filterQueriesDTO = filterUtil.getQueryByFilter(filter);
            PreparedStatement statement = connection.prepareStatement(filterQueriesDTO.findByFilterSql());

            filterUtil.insertValueFromFilterIntoPreparedStatement(filter, statement, filterQueriesDTO);
            return getTripByResultSet(statement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске с фильтром", e);
        }
    }

    /**
     * Получает количество страниц на основе фильтров.
     *
     * @param filter карта с фильтрами
     * @return количество страниц
     * @throws DaoException если произошла ошибка при подсчете страниц
     */
    public Long getCountPage(Map<String, String> filter) {
        try (Connection connection = getConnection()) {
            FilterQueriesDTO filterQueriesDTO = filterUtil.getQueryByFilter(filter);
            PreparedStatement statement = connection.prepareStatement(filterQueriesDTO.countFilterSql());
            filterUtil.insertValueFromFilterIntoPreparedStatement(filter, statement, filterQueriesDTO);
            Long count = convertResultSetToCountPages(statement.executeQuery());
            return (long) Math.ceil(count * 1.0 / LIMIT);
        } catch (SQLException e) {
            throw new DaoException("Ошибка при подсчете всех страниц", e);
        }
    }


    /**
     * Отменяет поездку по заданному ID.
     *
     * @param id ID поездки
     * @throws DaoException если произошла ошибка при отмене поездки
     */
    public void cancelTrip(Long id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CANCEL_TRIP_SQL)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при отмене поездки", e);
        }
    }

    /**
     * Проверяет актуальность статуса поездки и обновляет его при необходимости.
     *
     * @throws DaoException если произошла ошибка при обновлении статуса
     */
    public void checkTheRelevanceOfTheTripStatus() {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_STATUS_FOR_COMPLETED_TRIPS_SQL)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при обновлении статуса поездки", e);
        }
    }


    /**
     * Получает общее количество страниц для пагинации.
     *
     * @param result ResultSet, откуда берется данные
     * @return Количество страниц
     * @throws SQLException если произошла ошибка
     */
    private Long convertResultSetToCountPages(ResultSet result) throws SQLException {
        if (result.next()) {
            return result.getLong("count");
        }
        throw new DaoException("Ошибка при поиске количества значений");
    }

    /**
     * Получаем список поездок из ResultSet
     *
     * @param resultSet ResultSet, откуда берется данные
     * @return Список поездок
     * @throws SQLException если произошла ошибка
     */
    private List<Trip> getTripByResultSet(ResultSet resultSet) throws SQLException {
        List<Trip> trips = new ArrayList<>();
        while (resultSet.next()) {
            trips.add(mapper.mapRow(resultSet));
        }
        return trips;
    }

    private long convertResultSetToCountByUserId(ResultSet result) throws SQLException {
        if (result.next()) {
            return result.getLong("count");
        }
        throw new DaoException("Ошибка при поиске количества поездок");
    }
}
