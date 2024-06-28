package ru.checkdev.notification.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.checkdev.notification.exception.SubscribeNotFoundException;
import ru.checkdev.notification.exception.UserAlreadySubscribedException;
import ru.checkdev.notification.service.subscribe.SubscribeCategoryService;
import ru.checkdev.notification.service.subscribe.SubscribeTopicService;


@Component
@AllArgsConstructor
public class SubscribeValidator {
    private final SubscribeCategoryService subscribeCategoryService;
    private final SubscribeTopicService subscribeTopicService;

    public void validateSubscribingParams(int userId) {
        if (subscribeCategoryService.existByUserId(userId)
                && subscribeTopicService.existByUserId(userId)) {
            throw new UserAlreadySubscribedException(userId);
        }
    }

    public void validateUnsubscribingParams(int userId) {
        if (!subscribeCategoryService.existByUserId(userId)
                && !subscribeTopicService.existByUserId(userId)) {
            throw new SubscribeNotFoundException(userId);
        }
    }
}