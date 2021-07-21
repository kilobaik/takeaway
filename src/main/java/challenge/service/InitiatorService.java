package challenge.service;

import challenge.messaging.Producer;
import challenge.model.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitiatorService {

  private final Producer producer;

  public void initiate(String playerName, Optional<Integer> number) {

    final Message message =
        Message.builder()
            .player(playerName)
            .number(number.orElse(Math.abs(new Random().nextInt())))
            .session(UUID.randomUUID().toString())
            .build();

    log.info(
        "[player={}; session={}] Starts the game with number={}",
        playerName,
        message.getSession(),
        message.getNumber());

    producer.produce(message);
  }
}
