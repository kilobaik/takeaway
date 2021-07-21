package challenge.controller;

import challenge.service.InitiatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(value = "game.initiator.mode", havingValue = "manual")
public class InitiatorController {

  @Value("${game.player.name}")
  private String playerName;

  private final InitiatorService initiator;

  @GetMapping(path = "/initiate")
  @ResponseStatus(HttpStatus.OK)
  public void initiate(@RequestParam(required = false) Optional<Integer> number) {
    initiator.initiate(playerName, number);
  }
}
