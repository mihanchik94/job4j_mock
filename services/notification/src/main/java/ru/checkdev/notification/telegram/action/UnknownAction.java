package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@AllArgsConstructor
public class UnknownAction implements Action {
    private final List<String> actions;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        String chatId = message.getChatId().toString();
        String answer = "Команда не поддерживается!";
        String lineSeparator = System.lineSeparator();
        StringBuilder builder = new StringBuilder(answer);
        builder.append(lineSeparator)
                .append("Доступные команды:")
                .append(lineSeparator);
        actions.forEach(action -> builder.append(action).append(lineSeparator));
        return new SendMessage(chatId, builder.toString());
    }

    @Override
    public BotApiMethod<Message> callback(Message message) {
        return handle(message);
    }
}
