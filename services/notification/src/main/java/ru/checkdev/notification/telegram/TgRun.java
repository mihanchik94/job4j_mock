package ru.checkdev.notification.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.checkdev.notification.service.subscribe.SubscribeCategoryService;
import ru.checkdev.notification.service.subscribe.SubscribeTopicService;
import ru.checkdev.notification.telegram.action.*;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import ru.checkdev.notification.telegram.service.TgDeskCallWebClint;
import ru.checkdev.notification.validator.SubscribeValidator;

import java.util.List;
import java.util.Map;

/**
 * 3. Мидл
 * Инициализация телеграм бот,
 * username = берем из properties
 * token = берем из properties
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.09.2023
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TgRun {
    private final TgAuthCallWebClint tgAuthCallWebClint;
    private final TgDeskCallWebClint tgDeskCallWebClint;
    private final SubscribeValidator subscribeValidator;
    private final SubscribeTopicService subscribeTopicService;

    @Value("${tg.username}")
    private String username;
    @Value("${tg.token}")
    private String token;
    @Value("${server.site.url.login}")
    private String urlSiteAuth;

    @Bean
    public void initTg() {
        List<String> commandsForActions = getCommandsForActions();
        Map<String, Action> actionMap = Map.of(
                "/start", new InfoAction(List.of(
                        "/start", "/new", "/check", "/forget", "/subscribe", "/unsubscribe")),
                "/new", new RegAction(tgAuthCallWebClint, urlSiteAuth),
                "/check", new CheckAction(tgAuthCallWebClint),
                "/forget", new ForgetAction(tgAuthCallWebClint),
                "/subscribe", new SubscribeAction(tgAuthCallWebClint,
                        tgDeskCallWebClint, subscribeValidator, subscribeTopicService),
                "/unsubscribe", new UnsubscribeAction(tgAuthCallWebClint, subscribeValidator, subscribeTopicService),
                "unknown", new UnknownAction(commandsForActions)
        );
        try {
            BotMenu menu = new BotMenu(actionMap, username, token);

            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(menu);
        } catch (TelegramApiException e) {
            log.error("Telegram bot: {}, ERROR {}", username, e.getMessage());
        }
    }

    private List<String> getCommandsForActions() {
        return List.of("/start", "/new", "/check", "/forget");
    }
}
