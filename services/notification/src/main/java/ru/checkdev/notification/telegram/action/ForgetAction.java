package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

@AllArgsConstructor
public class ForgetAction implements Action {
    private static final String URL_GENERATE_NEW_PASSWORD = "/person/forget";
    private final TgAuthCallWebClint authCallWebClint;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        Long chatId = message.getChatId();
        String password = authCallWebClint.doPut(URL_GENERATE_NEW_PASSWORD, chatId).block();
        if (password == null || password.isEmpty()) {
            return new SendMessage(chatId.toString(), "Пользователь не зарегистрирован в системе");
        }
        String lineSeparator = System.lineSeparator();
        String answer = "Ваш новый пароль:" + lineSeparator + password;
        return new SendMessage(chatId.toString(), answer);
    }

    @Override
    public BotApiMethod<Message> callback(Message message) {
        return handle(message);
    }
}