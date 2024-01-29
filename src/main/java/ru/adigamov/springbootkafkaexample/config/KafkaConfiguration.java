package ru.adigamov.springbootkafkaexample.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;
import ru.adigamov.springbootkafkaexample.kafka.dto.ExampleProducerDto;
import ru.adigamov.springbootkafkaexample.kafka.handler.CustomRecordRecoverer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfiguration {

    @Value("${kafka.retry.retry-interval-ms}")
    private long retryIntervalMs;

    @Value("${kafka.retry.retry-max-attempts}")
    private long retryMaxAttempts;

    @Value(value = "${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapAddress;


    @Bean
    DefaultErrorHandler defaultErrorHandler() {
        FixedBackOff fixedBackOff = new FixedBackOff(retryIntervalMs, retryMaxAttempts);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(new CustomRecordRecoverer(), fixedBackOff);
        errorHandler.setLogLevel(KafkaException.Level.TRACE); //Чтобы убрать огромный стэктрейс при ошибке

        //errorHandler.addNotRetryableExceptions();
        errorHandler.addRetryableExceptions(RuntimeException.class);

        return errorHandler;
    }

    // TODO вынести в проперти
    @Bean
    public ProducerFactory<String, ExampleProducerDto> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, ExampleProducerDto> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topic1() {
        return new NewTopic("testTopic", 1, (short) 1);
    }
}
