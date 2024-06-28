package ru.checkdev.notification.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.checkdev.notification.domain.SubscribeCategory;

import java.util.List;

public interface SubscribeCategoryRepository extends CrudRepository<SubscribeCategory, Integer> {
    @Override
    List<SubscribeCategory> findAll();

    List<SubscribeCategory> findByUserId(int id);

    SubscribeCategory findByUserIdAndCategoryId(int userId, int categoryId);

    @Query(value = """
           select case when count(category_id) > 0 then true else false end
           from cd_subscribe_category
           where user_id = :userId
           """, nativeQuery = true)
    boolean existByUserId(int userId);

    void deleteByUserId(int userId);
}