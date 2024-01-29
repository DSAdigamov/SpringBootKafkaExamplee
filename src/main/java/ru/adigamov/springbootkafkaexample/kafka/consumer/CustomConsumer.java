package ru.adigamov.springbootkafkaexample.kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.adigamov.springbootkafkaexample.exception.WrongNumberException;
import ru.adigamov.springbootkafkaexample.kafka.dto.ExampleListenerDto;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomConsumer {

    @KafkaListener(topics = "${kafka.example-topic}",
    groupId = "group1",
    filter = "deserializationRecordFilter",
    autoStartup = "${kafka.listener1.enabled}",
    properties = "${kafka.listener1.properties}",
    batch = "true")
    void listenMessages(List<ConsumerRecord<String, ExampleListenerDto>> messages) {
        log.info("Получено [{}] сообщений", messages.size());
        var values = messages.stream().map(ConsumerRecord::value).toList();
        System.out.println(values);

        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).getNumber() == 3) {
                throw new WrongNumberException("Ошибка. Цифра 3!");
            }
        }
    }
}