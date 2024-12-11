package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractBaseDAO;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.entity.User;
import org.tomato.gowithtomato.exception.db.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public abstract class RouteDAO extends AbstractBaseDAO<Route> {
    public abstract List<Route> findByUserWithPagination(User user, int pageNumber);

    public abstract long getCountPage(User user);

    public abstract Route save(Route route, Long userId);

    public abstract Optional<Route> findById(Long id);
    protected Long convertResultSetToCountPages(ResultSet result) throws SQLException {
        if (result.next()) {
            return result.getLong("count");
        }
        throw new DaoException("Ошибка при поиске количества значений");
    }
}
