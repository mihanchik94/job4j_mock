package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

import java.util.Objects;

@AllArgsConstructor
public class CheckAction implements Action {
    private static final String URL_CHECK_USERNAME_AND_EMAIL = "/person/check/";
    private final TgAuthCallWebClint authCallWebClint;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        Long chatId = message.getChatId();
        var personDTO = authCallWebClint.doGet(URL_CHECK_USERNAME_AND_EMAIL + chatId);
        String email = Objects.requireNonNull(personDTO.block()).getEmail();
        String username = Objects.requireNonNull(personDTO.block()).getUsername();
        if (email == null || username == null) {
            return new SendMessage(chatId.toString(), "Пользователь не зарегистрирован в системе");
        }
        String lineSeparator = System.lineSeparator();
        String answer = "Имя: " + lineSeparator + username + lineSeparator + "Email: " + lineSeparator + email;
        return new SendMessage(chatId.toString(), answer);
    }

    @Override
    public BotApiMethod<Message> callback(Message message) {
        return handle(message);
    }
}
