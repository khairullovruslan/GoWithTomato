package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractCrudDAO;
import org.tomato.gowithtomato.entity.Route;
import org.tomato.gowithtomato.entity.User;

import java.util.List;

/**
 * Абстрактный интерфейс для работы с маршрутами Route
 */
public abstract class RouteDAO extends AbstractCrudDAO<Long, Route> {

    /**
     * Находит маршруты пользователя с пагинацией
     *
     * @param user       Пользователь, для которого ищутся маршруты
     * @param pageNumber Номер страницы для пагинации
     * @return Список маршрутов пользователя на указанной странице
     */
    public abstract List<Route> findByUserWithPagination(User user, int pageNumber);

    /**
     * Получает общее количество страниц маршрутов для пользователя
     *
     * @param user Пользователь, для которого рассчитывается количество страниц
     * @return Общее количество страниц маршрутов
     */
    public abstract long getCountPage(User user);
}
