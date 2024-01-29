package ru.adigamov.springbootkafkaexample.kafka.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;

@Slf4j
public class CustomRecordRecoverer implements ConsumerRecordRecoverer {
    @Override
    public void accept(ConsumerRecord<?, ?> record, Exception e) {
        if (record.value() == null) {
            log.debug("Record value is null. Handled exception: {} with message {}", e.getClass(), e.getMessage());
        }

        log.error("Handled exception: {} with message {}", e.getClass(), e.getMessage());
    }
}
