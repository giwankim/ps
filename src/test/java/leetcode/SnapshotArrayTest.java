package leetcode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SnapshotArrayTest {
  private SnapshotArray sut;

  @BeforeEach
  void setUp() {
    sut = new SnapshotArray(3);
  }

  @Test
  void snap() {
    assertThat(sut.snap()).isZero();
    assertThat(sut.snap()).isOne();
  }

  @Test
  void get() {
    sut.set(0, 5);
    sut.snap();
    assertThat(sut.get(0, 0)).isEqualTo(5);
  }

  @Test
  void getAfterSnapshot() {
    sut.set(0, 5);
    sut.snap();
    sut.snap();
    assertThat(sut.get(0, 1)).isEqualTo(5);
  }

  @Test
  void getFromPreviousSnapshot() {
    sut.set(0, 5);
    sut.snap();
    sut.set(0, 6);
    assertThat(sut.get(0, 0)).isEqualTo(5);
  }
}
