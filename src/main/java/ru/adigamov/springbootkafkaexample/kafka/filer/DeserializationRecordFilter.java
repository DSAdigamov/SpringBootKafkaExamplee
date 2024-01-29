package ru.adigamov.springbootkafkaexample.kafka.filer;

import org.apache.commons.logging.LogFactory;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.core.log.LogAccessor;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.kafka.support.serializer.DeserializationException;
import org.springframework.kafka.support.serializer.SerializationUtils;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class DeserializationRecordFilter<K, V> implements RecordFilterStrategy<K, V> {

    private final LogAccessor logger = new LogAccessor(LogFactory.getLog(DeserializationRecordFilter.class));

    @Override
    public boolean filter(ConsumerRecord<K, V> consumerRecord) {
        if (consumerRecord.value() == null) {
            logDeserializationException(consumerRecord);
            return false;
        }

        return true;
    }

    @Override
    public List<ConsumerRecord<K, V>> filterBatch(List<ConsumerRecord<K, V>> records) {
        return records.stream()
                .filter(this::filter)
                .toList();
    }

    private void logDeserializationException(ConsumerRecord<K, V> consumerRecord) {
        DeserializationException ex = SerializationUtils.byteArrayToDeserializationException(logger, consumerRecord.headers().lastHeader(SerializationUtils.VALUE_DESERIALIZER_EXCEPTION_HEADER));

        logger.error(String.format("Record[%s] at offset [%d] could not been deserialized. Exception: [%s {Message: %s}]",
                new String(ex.getData(), UTF_8), consumerRecord.offset(), ex.getCause(), ex.getCause().getMessage()));
    }
}
