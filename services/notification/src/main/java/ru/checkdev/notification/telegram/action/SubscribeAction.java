package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.checkdev.notification.domain.PersonDTO;
import ru.checkdev.notification.dto.TopicDTO;
import ru.checkdev.notification.exception.UserAlreadySubscribedException;
import ru.checkdev.notification.service.subscribe.SubscribeTopicService;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import ru.checkdev.notification.telegram.service.TgDeskCallWebClint;
import ru.checkdev.notification.validator.SubscribeValidator;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Slf4j
public class SubscribeAction implements Action {
    private static final String GET_PERSON_URL = "/person/check/";
    private static final String GET_TOPICS_URL = "/topic/";

    private final TgAuthCallWebClint authCallWebClint;
    private final TgDeskCallWebClint deskCallWebClient;
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
            subscribeValidator.validateSubscribingParams(personId);
        } catch (UserAlreadySubscribedException e) {
            log.error("пользователь уже оформил подписку");
            return new SendMessage(chatId, "Уведомления в телеграме уже подключены");
        }
        List<TopicDTO> topics = getTopics();
        subscribeTopicService.subscribe(personId, topics);
        return new SendMessage(chatId, "Подписка оформлена!");
    }

    @Override
    public BotApiMethod<Message> callback(Message message) {
        return handle(message);
    }

    private List<TopicDTO> getTopics() {
        Flux<TopicDTO> topics = deskCallWebClient.getAllTopics(GET_TOPICS_URL);
        return topics.collectList().block();
    }
}