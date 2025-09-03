package org.skypro.recommendation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение для некорректного формата ID пользователя
 * Автоматически возвращает статус 400 Bad Request
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidUserIdException extends RuntimeException {

    /**
     * Конструктор исключения
     *
     * @param message сообщение об ошибке
     */
    public InvalidUserIdException(String message) {
        super(message);
    }
}