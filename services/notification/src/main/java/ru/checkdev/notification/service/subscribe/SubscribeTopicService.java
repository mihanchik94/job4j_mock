package ru.checkdev.notification.service.subscribe;

import lombok.AllArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.notification.domain.SubscribeTopic;
import ru.checkdev.notification.dto.TopicDTO;
import ru.checkdev.notification.repository.SubscribeTopicRepository;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscribeTopicService {
    private final SubscribeTopicRepository repository;
    private final SubscribeCategoryService categoryService;

    public List<SubscribeTopic> findAll() {
        return repository.findAll();
    }

    public SubscribeTopic save(SubscribeTopic subscribeTopic) {
        return repository.save(subscribeTopic);
    }

    public List<Integer> findTopicByUserId(int userId) {
        return repository.findByUserId(userId).stream()
                .map(subscribeTopic -> subscribeTopic.getTopicId())
                .collect(Collectors.toList());
    }

    public SubscribeTopic delete(SubscribeTopic subscribeTopic) {
        SubscribeTopic rsl = repository
                .findByUserIdAndTopicId(subscribeTopic.getUserId(), subscribeTopic.getTopicId());
        repository.delete(rsl);
        return subscribeTopic;
    }

    public boolean existByUserId(int userId) {
        return repository.existByUserId(userId);
    }

    @Transactional
    public void deleteByUserId(int userId) {
        repository.deleteByUserId(userId);
    }

    @Transactional
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 5, backoff = @Backoff(delay = 3000L, multiplier = 1.5))
    public void subscribe(int userId, List<TopicDTO> topics) {
        List<Integer> categoryIds = getCategoryIds(topics);
        List<SubscribeTopic> subscribeTopics = topics.stream()
                .map(topicDTO -> SubscribeTopic.builder()
                        .userId(userId)
                        .topicId(topicDTO.getId())
                        .build())
                .toList();
        repository.saveAll(subscribeTopics);
        categoryService.saveAll(userId, categoryIds);
    }

    @Transactional
    @Retryable(retryFor = PersistenceException.class, maxAttempts = 5, backoff = @Backoff(delay = 3000L, multiplier = 1.5))
    public void unsubscribe(int userId) {
        deleteByUserId(userId);
        categoryService.deleteByUserId(userId);
    }

    private List<Integer> getCategoryIds(List<TopicDTO> topics) {
        Set<Integer> categoryIds = new HashSet<>();
        topics.forEach(topicDTO -> categoryIds.add(topicDTO.getCategoryId()));
        return new ArrayList<>(categoryIds);
    }
}