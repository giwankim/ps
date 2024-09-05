package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HitCounterTest {
  private HitCounter hitCounter;

  @BeforeEach
  void setUp() {
    hitCounter = new HitCounter();
  }

  @Test
  void hit() {
    hitCounter.hit(1);
    hitCounter.hit(2);
    hitCounter.hit(3);
    assertThat(hitCounter.getHits(4)).isEqualTo(3);
  }

  @Test
  void shouldGetRecentHits() {
    hitCounter.hit(1);
    hitCounter.hit(2);
    hitCounter.hit(3);
    assertThat(hitCounter.getHits(4)).isEqualTo(3);

    hitCounter.hit(300);
    assertThat(hitCounter.getHits(300)).isEqualTo(4);
    assertThat(hitCounter.getHits(301)).isEqualTo(3);
  }
}
