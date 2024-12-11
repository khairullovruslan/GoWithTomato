package org.tomato.gowithtomato.dao.impl;

import org.tomato.gowithtomato.dao.daoInterface.TripDAO;
import org.tomato.gowithtomato.dto.FilterQueriesDTO;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.entity.TripStatus;
import org.tomato.gowithtomato.exception.db.DaoException;
import org.tomato.gowithtomato.mapper.TripMapper;
import org.tomato.gowithtomato.util.FilterTripDaoUtil;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.tomato.gowithtomato.dao.query.TripQueries.*;

public class TripDAOImpl extends TripDAO {
    private static final TripDAOImpl INSTANCE = new TripDAOImpl();
    private final FilterTripDaoUtil filterUtil;

    private TripDAOImpl() {
        filterUtil = FilterTripDaoUtil.getInstance();
        mapper = TripMapper.getInstance();
    }


    public static TripDAOImpl getInstance() {
        return INSTANCE;
    }


    @Override
    public Trip saveWithRouteId(Trip trip, Long id, Long userId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, userId);
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
            throw new DaoException("Ошибка при поиске поездки по ID: %s".formatted(id), e);
        }
    }

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
            throw new DaoException("Ошибка при поиске поездки по ID: %s".formatted(id), e);
        }
    }

    @Override
    public List<Trip> findAllByFilter(Map<String, String> filter) {
        try (Connection connection = getConnection()) {
            FilterQueriesDTO filterQueriesDTO = filterUtil.getQueryByFilter(filter);
            System.out.println("sql query : " + filterQueriesDTO.findByFilterSql());
            PreparedStatement statement = connection.prepareStatement(filterQueriesDTO.findByFilterSql());
            filterUtil.insertValueFromFilterIntoPreparedStatement(filter, statement, filterQueriesDTO);
            return getTripByResultSet(statement.executeQuery());
        } catch (SQLException e) {
            throw new DaoException("Ошибка при поиске с фильтром", e);
        }
    }

    @Override
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

    @Override
    public void cancelTrip(Long id) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(CANCEL_TRIP_SQL)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при отмене поездки", e);
        }
    }


    @Override
    public void checkTheRelevanceOfTheTripStatus() {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_STATUS_FOR_COMPLETED_TRIPS_SQL)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Ошибка при обновлении статуса поездки", e);
        }
    }
}
