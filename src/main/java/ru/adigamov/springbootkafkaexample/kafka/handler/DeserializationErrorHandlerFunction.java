package ru.adigamov.springbootkafkaexample.kafka.handler;

import org.springframework.kafka.support.serializer.FailedDeserializationInfo;

import java.util.function.Function;

public class DeserializationErrorHandlerFunction implements Function<FailedDeserializationInfo, FailedDeserializationInfo> {
    @Override
    public FailedDeserializationInfo apply(FailedDeserializationInfo failedDeserializationInfo) {
        // Костыль для работы батчей
        return null;
    }
}
