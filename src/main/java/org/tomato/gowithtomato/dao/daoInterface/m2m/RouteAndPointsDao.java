package org.tomato.gowithtomato.dao.daoInterface.m2m;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractBaseDAO;
import org.tomato.gowithtomato.entity.Point;
import org.tomato.gowithtomato.entity.Route;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Абстрактный класс для работы с маршрутами и точками, имеющими связь многие-ко-многим m2m
 */
public abstract class RouteAndPointsDao extends AbstractBaseDAO {

    /**
     * Сохраняет список точек, связанных с определённым маршрутом
     *
     * @param pointList  Список точек для сохранения
     * @param routeId    Идентификатор маршрута
     * @throws SQLException если произошла ошибка доступа к базе данных
     */
    public abstract void saveAll(List<Point> pointList, Long routeId) throws SQLException;

    /**
     * Находит точки по идентификатору маршрута
     *
     * @param id         Идентификатор маршрута
     * @return Список точек, связанных с указанным маршрутом
     * Возвращает пустой список, если точки не найдены
     * @throws SQLException если произошла ошибка доступа к базе данных
     */
    public abstract List<Point> findByRouteId(Long id) throws SQLException;
}
