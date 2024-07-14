package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Mono;
import ru.checkdev.notification.domain.PersonDTO;
import ru.checkdev.notification.exception.SubscribeNotFoundException;
import ru.checkdev.notification.service.subscribe.SubscribeTopicService;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import ru.checkdev.notification.validator.SubscribeValidator;

import java.util.Objects;

@AllArgsConstructor
@Slf4j
public class UnsubscribeAction implements Action {
    private static final String GET_PERSON_URL = "/person/check/";

    private final TgAuthCallWebClint authCallWebClint;
    private final SubscribeValidator subscribeValidator;
    private final SubscribeTopicService subscribeTopicService;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        String chatId = message.getChatId().toString();
        Mono<PersonDTO> personDTOMono = authCallWebClint.doGet(GET_PERSON_URL + chatId);
        Integer personId = Objects.requireNonNull(personDTOMono.block()).getId();
        if (personId == null) {
            return new SendMessage(chatId, "Пользователь не зарегистрирован в системе");
        }
        try {
            subscribeValidator.validateUnsubscribingParams(personId);
        } catch (SubscribeNotFoundException e) {
            log.error("пользователь не оформлял подписку на уведомления: ", e);
            return new SendMessage(chatId, "Уведомления в телеграме уже отключены");
        }
        subscribeTopicService.unsubscribe(personId);
        return new SendMessage(chatId, "Вы отписались от обновлений с сайта в телеграме");
    }

    @Override
    public BotApiMethod<Message> callback(Message message) {
        return handle(message);
    }
}