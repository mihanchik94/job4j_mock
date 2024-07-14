package ru.checkdev.notification.exception;

public class UserAlreadySubscribedException extends RuntimeException {
    public UserAlreadySubscribedException(int userId) {
        super(String.format("Пользователь с id= %d уже подписан", userId));
    }
}