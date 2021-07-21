package challenge.messaging;

import challenge.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Producer {

  @Value("${game.queues.upstream}")
  private String upstreamQueue;

  private final KafkaTemplate<String, Message> kafkaTemplate;

  public void produce(Message message) {
    kafkaTemplate.send(
        MessageBuilder.withPayload(message).setHeader(KafkaHeaders.TOPIC, upstreamQueue).build());
  }
}
