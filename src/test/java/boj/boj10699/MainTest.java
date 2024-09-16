package boj.boj10699;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.Test;

class MainTest {
  @Test
  void today() {
    BufferedReader r = new BufferedReader(new StringReader(""));
    StringWriter out = new StringWriter();
    PrintWriter pw = new PrintWriter(out);
    ZonedDateTime dateTime = ZonedDateTime.of(2024, 9, 16, 10, 10, 10, 0, ZoneId.of("Asia/Seoul"));
    Clock clock = Clock.fixed(dateTime.toInstant(), ZoneId.of("Asia/Seoul"));

    Main.today(r, pw, clock);

    assertThat(out).hasToString("2024-09-16\n");
  }
}
