package ru.adigamov.springbootkafkaexample.kafka.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.support.serializer.DeserializationException;
import ru.adigamov.springbootkafkaexample.config.ObjectMapperConfig;
import ru.adigamov.springbootkafkaexample.kafka.dto.ExampleListenerDto;
import ru.adigamov.springbootkafkaexample.kafka.dto.ExampleProducerDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
public class CustomDeserializer implements Deserializer<ExampleListenerDto> {

    private final ObjectMapper customObjectMapper;

    public CustomDeserializer() {
        this.customObjectMapper = new ObjectMapperConfig().customObjectMapper();
    }

    public CustomDeserializer(ObjectMapper customObjectMapper) {
        this.customObjectMapper = customObjectMapper;
    }


    @Override
    public ExampleListenerDto deserialize(String s, byte[] bytes) {
        return null;
    }

    @Override
    public ExampleListenerDto deserialize(String topic, Headers headers, byte[] data) {
        try {
            if (data == null) {
                log.error("data is null");
                return null;
            }

            // какие-то манипуляции с сообщением/проверки
            String numberStr = new String(data, UTF_8);

            return new ExampleListenerDto(Integer.valueOf(numberStr), LocalDateTime.now());
        } catch (Exception e) {
            throw new DeserializationException(e.getMessage(), data, false, e);
        }
    }
}
