package challenge.scheduler;

import challenge.service.InitiatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@EnableScheduling
@RequiredArgsConstructor
@ConditionalOnProperty(value = "game.initiator.mode", havingValue = "automatic")
public class InitiatorScheduler {

  @Value("${game.player.name}")
  private String playerName;

  private final InitiatorService initiator;

  @Scheduled(fixedDelay = 100, initialDelay = 5000)
  public void initiate() {
    initiator.initiate(playerName, Optional.empty());
  }
}
