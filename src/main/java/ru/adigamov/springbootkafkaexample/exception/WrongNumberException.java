package ru.adigamov.springbootkafkaexample.exception;

public class WrongNumberException extends RuntimeException {

    public WrongNumberException(String message) {
        super(message);
    }
}
