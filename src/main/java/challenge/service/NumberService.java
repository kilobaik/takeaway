package challenge.service;

import lombok.Value;
import org.springframework.stereotype.Service;

@Service
public class NumberService {

  public Movement handle(int number) {
    final int addition = getAddition(number);
    return new Movement(addition, number, (addition + number) / 3);
  }

  private int getAddition(int number) {
    return (number % 3 == 0 ? 0 : number % 3 == 2 ? 1 : -1);
  }

  @Value
  public static class Movement {
    int addition;
    int origin;
    int result;

    @Override
    public String toString() {
      switch (addition) {
        case 1:
          return String.format("(%d + 1)/3 = %d", origin, result);
        case -1:
          return String.format("(%d -1)/3 = %d", origin, result);
        default:
          return String.format("%d/3 = %d", origin, result);
      }
    }
  }
}
