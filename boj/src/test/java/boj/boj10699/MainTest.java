package boj.boj10699;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class MainTest {
  private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

  @Test
  void formatsSingleDigitMonthAndDayWithLeadingZeroes() {
    assertThat(runToday(seoulClockAt(2024, 1, 2))).isEqualTo("2024-01-02");
  }

  @Test
  void usesSeoulDateWhenUtcDateIsDifferent() {
    Clock clock = Clock.fixed(Instant.parse("2024-01-01T15:30:00Z"), SEOUL);
    assertThat(runToday(clock)).isEqualTo("2024-01-02");
  }

  private static Clock seoulClockAt(int year, int month, int day) {
    return Clock.fixed(LocalDate.of(year, month, day).atStartOfDay(SEOUL).toInstant(), SEOUL);
  }

  private static String runToday(Clock clock) {
    StringWriter out = new StringWriter();
    Main.today(new PrintWriter(out), clock);
    return out.toString().trim();
  }
}
