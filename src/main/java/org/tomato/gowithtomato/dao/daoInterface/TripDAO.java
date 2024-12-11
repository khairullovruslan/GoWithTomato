package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractBaseDAO;
import org.tomato.gowithtomato.entity.Trip;
import org.tomato.gowithtomato.exception.db.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class TripDAO extends AbstractBaseDAO<Trip> {
    public abstract Trip saveWithRouteId(Trip trip, Long id, Long userId);

    public abstract long getCountByUserId(Long id);

    public abstract List<Trip> findAllByFilter(Map<String, String> filter);

    public abstract Long getCountPage(Map<String, String> filter);

    public abstract void cancelTrip(Long id);

    public abstract Optional<Trip> findById(Long id);

    public abstract void checkTheRelevanceOfTheTripStatus();

    protected Long convertResultSetToCountPages(ResultSet result) throws SQLException {
        if (result.next()) {
            return result.getLong("count");
        }
        throw new DaoException("Ошибка при поиске количества значений");
    }

    protected List<Trip> getTripByResultSet(ResultSet resultSet) throws SQLException {
        List<Trip> trips = new ArrayList<>();
        while (resultSet.next()) {
            trips.add(mapper.mapRow(resultSet));
        }
        return trips;
    }

    protected long convertResultSetToCountByUserId(ResultSet result) throws SQLException {
        if (result.next()) {
            return result.getLong("count");
        }
        throw new DaoException("Ошибка при поиске количества поездок");
    }
}
