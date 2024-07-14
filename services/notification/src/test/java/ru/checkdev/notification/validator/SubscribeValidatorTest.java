package ru.checkdev.notification.validator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.checkdev.notification.exception.SubscribeNotFoundException;
import ru.checkdev.notification.exception.UserAlreadySubscribedException;
import ru.checkdev.notification.service.subscribe.SubscribeCategoryService;
import ru.checkdev.notification.service.subscribe.SubscribeTopicService;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubscribeValidatorTest {
    @Mock
    private SubscribeCategoryService subscribeCategoryService;

    @Mock
    private SubscribeTopicService subscribeTopicService;

    @InjectMocks
    private SubscribeValidator subscribeValidator;


    @Test
    public void whenValidateSubscribingParamsAndUserAlreadySubscribedThenThrowsUserAlreadySubscribedException() {
        int userId = 1;
        when(subscribeCategoryService.existByUserId(userId)).thenReturn(true);
        when(subscribeTopicService.existByUserId(userId)).thenReturn(true);
        assertThrows(UserAlreadySubscribedException.class, () -> {
            subscribeValidator.validateSubscribingParams(userId);
        });
    }

    @Test
    public void whenValidateSubscribingParamsAndUserNotSubscribed() {
        int userId = 1;
        when(subscribeCategoryService.existByUserId(userId)).thenReturn(false);
        when(subscribeTopicService.existByUserId(userId)).thenReturn(false);
        assertThrows(SubscribeNotFoundException.class, () -> {
            subscribeValidator.validateUnsubscribingParams(userId);
        });
    }
}