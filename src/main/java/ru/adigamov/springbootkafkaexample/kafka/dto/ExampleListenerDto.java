package ru.adigamov.springbootkafkaexample.kafka.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExampleListenerDto {

    private Integer number;

    private LocalDateTime time;
}
