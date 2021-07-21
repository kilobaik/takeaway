package challenge.configuration;

import challenge.model.Message;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerConfiguration {

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  public Map<String, Object> producerProperties() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
    return properties;
  }

  @Bean
  public ProducerFactory<String, Message> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerProperties());
  }

  @Bean
  public KafkaTemplate<String, Message> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

}
