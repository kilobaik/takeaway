package challenge.configuration;

import challenge.model.Message;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConsumerConfiguration {

  @Value("${spring.kafka.group-id}")
  private String groupId;

  @Value("${spring.kafka.bootstrap-servers}")
  private String bootstrapServers;

  @Bean
  public Map<String, Object> consumerProperties() {
    final Map<String, Object> properties = new HashMap<>();
    properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    return properties;
  }

  @Bean
  public ConsumerFactory<String, Message> consumerFactory() {
    return new DefaultKafkaConsumerFactory<>(
        consumerProperties(), new StringDeserializer(), new JsonDeserializer<>(Message.class));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Message> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Message> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory());
    return factory;
  }
}
