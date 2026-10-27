package org.example;

/**
 * Власне (перевірюване) виключення, яке сигналізує про некоректні вхідні дані.
 */
public class InvalidInputException extends Exception {

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
}
