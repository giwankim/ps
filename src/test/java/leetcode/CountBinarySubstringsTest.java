package leetcode;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.Test;

class CountBinarySubstringsTest {
  private final CountBinarySubstrings sut = new CountBinarySubstrings();

  @Test
  void ZeroesAndOnesConsecutive() {
    assertSoftly(
        softly -> {
          softly.assertThat(sut.countBinarySubstrings("000111")).isEqualTo(3);
          softly.assertThat(sut.countBinarySubstrings("11100")).isEqualTo(2);
          softly.assertThat(sut.countBinarySubstrings("00011100")).isEqualTo(5);
        });
  }

  @Test
  void countBinarySubstrings() {
    assertSoftly(
        softly -> {
          softly.assertThat(sut.countBinarySubstrings("00110011")).isEqualTo(6);
          softly.assertThat(sut.countBinarySubstrings("10101")).isEqualTo(4);
        });
  }
}
