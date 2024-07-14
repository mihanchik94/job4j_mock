package ru.checkdev.notification.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.checkdev.notification.domain.SubscribeTopic;
import java.util.List;

public interface SubscribeTopicRepository extends CrudRepository<SubscribeTopic, Integer> {
    @Override
    List<SubscribeTopic> findAll();

    List<SubscribeTopic> findByUserId(int id);

    SubscribeTopic findByUserIdAndTopicId(int userId, int topicId);

    @Query(value = """
           select case when count(topic_id) > 0 then true else false end
           from cd_subscribe_topic
           where user_id = :userId
           """, nativeQuery = true)
    boolean existByUserId(int userId);

    void deleteByUserId(int userId);
}