package ru.checkdev.notification.service.subscribe;

import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.notification.domain.SubscribeCategory;
import ru.checkdev.notification.repository.SubscribeCategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SubscribeCategoryService {
    private final SubscribeCategoryRepository repository;

    public List<SubscribeCategory> findAll() {
        return repository.findAll();
    }

    @KafkaListener(topics = "add-subscribeCategoryTopic")
    public SubscribeCategory save(SubscribeCategory subscribeCategory) {
        return repository.save(subscribeCategory);
    }

    public List<Integer> findCategoriesByUserId(int userId) {
        List<Integer> rsl = new ArrayList<>();
        List<SubscribeCategory> list = repository.findByUserId(userId);
        for (SubscribeCategory subscribeCategory : list) {
            rsl.add(subscribeCategory.getCategoryId());
        }
        return rsl;
    }

    @KafkaListener(topics = "delete-subscribeCategoryTopic")
    public SubscribeCategory delete(SubscribeCategory subscribeCategory) {
        SubscribeCategory subscribeCategoryRsl = repository
                .findByUserIdAndCategoryId(subscribeCategory.getUserId(), subscribeCategory.getCategoryId());
        repository.delete(subscribeCategoryRsl);
        return subscribeCategory;
    }

    public boolean existByUserId(int userId) {
        return repository.existByUserId(userId);
    }

    @Transactional
    public void deleteByUserId(int userId) {
        repository.deleteByUserId(userId);
    }

    public void saveAll(int userId, List<Integer> categoryIds) {
        List<SubscribeCategory> subscribeCategories = categoryIds.stream()
                .map(categoryId -> SubscribeCategory.builder()
                        .userId(userId)
                        .categoryId(categoryId)
                        .build())
                .toList();
        repository.saveAll(subscribeCategories);
    }
}