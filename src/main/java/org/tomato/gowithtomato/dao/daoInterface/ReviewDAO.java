package org.tomato.gowithtomato.dao.daoInterface;

import org.tomato.gowithtomato.dao.daoInterface.base.AbstractCrudDAO;
import org.tomato.gowithtomato.dto.ReviewDTO;
import org.tomato.gowithtomato.entity.Review;

import java.util.List;
import java.util.Optional;

/**
 * Абстрактный интерфейс для работы с отзывами
 */
public abstract class ReviewDAO extends AbstractCrudDAO<Long, Review> {

    /**
     * Проверяет, оставил ли пользователь отзыв для определенной поездки
     *
     * @param tripId Идентификатор поездки
     * @param userId Идентификатор пользователя
     * @return true, если пользователь оставил отзыв, иначе false
     */
    public abstract boolean searchForUserInReviews(long tripId, long userId);

    /**
     * Находит отзыв пользователя по идентификатору поездки и пользователя
     *
     * @param userId Идентификатор пользователя
     * @param tripId Идентификатор поездки
     * @return Объект Optional, содержащий найденный отзыв, или empty, если отзыв не найден
     */
    public abstract Optional<Review> findByUserAndTripId(Long userId, Long tripId);

    public abstract List<Review> findByTripOwnerId(Long id);
}
