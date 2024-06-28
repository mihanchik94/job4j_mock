package ru.checkdev.notification.exception;

public class SubscribeNotFoundException extends RuntimeException {
    public SubscribeNotFoundException(int userId) {
        super(String.format("У пользователя с id= %d подписка отсутствует", userId));
    }
}