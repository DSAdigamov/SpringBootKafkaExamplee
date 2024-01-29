package ru.adigamov.springbootkafkaexample.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.adigamov.springbootkafkaexample.kafka.dto.ExampleProducerDto;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ProducerController {

    @Value("${kafka.example-topic}")
    private String topic;

    private final KafkaTemplate<String, ExampleProducerDto> kafkaTemplate;

    @GetMapping("/send")
    public void sendMessage() {
        for (int i = 0; i < 10; i++) {

            kafkaTemplate.send(topic, new ExampleProducerDto(i));
        }
    }
}
