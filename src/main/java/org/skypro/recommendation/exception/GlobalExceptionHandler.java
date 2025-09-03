package org.skypro.recommendation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Глобальный обработчик исключений для всех контроллеров
 * Обрабатывает исключения и возвращает соответствующие HTTP-статусы
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обработка исключений некорректного формата ID пользователя
     *
     * @param ex исключение InvalidUserIdException
     * @return ResponseEntity с сообщением об ошибке и статусом 400
     */
    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<String> handleInvalidUserIdException(InvalidUserIdException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Обработка всех непредвиденных исключений
     *
     * @param ex исключение
     * @return ResponseEntity с сообщением об ошибке и статусом 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error: " + ex.getMessage());
    }
}