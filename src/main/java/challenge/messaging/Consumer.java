package challenge.messaging;

import challenge.model.Message;
import challenge.service.NumberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Consumer {

  @Value("${game.player.name}")
  private String playerName;

  private final NumberService numberService;
  private final Producer producer;

  @KafkaListener(topics = "${game.queues.downstream}", groupId = "${spring.kafka.group-id}")
  public void consume(@Payload Message message, @Headers MessageHeaders headers) {

    final NumberService.Movement movement = numberService.handle(message.getNumber());

    if (movement.getResult() == 1) {
      log.info("[player={}; session={}] {} WIN !!", playerName, message.getSession(), movement);
    } else {
      producer.produce(
          Message.builder()
              .player(playerName)
              .number(movement.getResult())
              .session(message.getSession())
              .build());

      log.info(
          "[player={}; session={}] Received {} and sent {}",
          playerName,
          message.getSession(),
          message.getNumber(),
          movement);
    }
  }
}
