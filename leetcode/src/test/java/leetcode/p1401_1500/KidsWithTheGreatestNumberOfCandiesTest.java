package leetcode.p1401_1500;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KidsWithTheGreatestNumberOfCandiesTest {
  private KidsWithTheGreatestNumberOfCandies sut;

  @BeforeEach
  void setUp() {
    sut = new KidsWithTheGreatestNumberOfCandies();
  }

  @Test
  void mostKidsCanReachTheMax() {
    var candies = new int[] {2, 3, 5, 1, 3};

    var actual = sut.kidsWithCandies(candies, 3);

    assertThat(actual).containsExactly(true, true, true, false, true);
  }

  @Test
  void onlyTheLeaderCanReachTheMax() {
    var candies = new int[] {4, 2, 1, 1, 2};

    var actual = sut.kidsWithCandies(candies, 1);

    assertThat(actual).containsExactly(true, false, false, false, false);
  }

  @Test
  void tiedLeadersBothCount() {
    var candies = new int[] {12, 1, 12};

    var actual = sut.kidsWithCandies(candies, 10);

    assertThat(actual).containsExactly(true, false, true);
  }

  @Test
  void exactlyMatchingTheMaxCounts() {
    var candies = new int[] {5, 2};

    var actual = sut.kidsWithCandies(candies, 3);

    assertThat(actual).containsExactly(true, true);
  }

  @Test
  void oneShortOfTheMaxIsFalse() {
    var candies = new int[] {5, 1};

    var actual = sut.kidsWithCandies(candies, 3);

    assertThat(actual).containsExactly(true, false);
  }

  @Test
  void theLeaderIsAlwaysTrue() {
    var candies = new int[] {100, 1, 2, 3};

    var actual = sut.kidsWithCandies(candies, 1);

    assertThat(actual).containsExactly(true, false, false, false);
  }

  @Test
  void theMaxIsMeasuredBeforeHandingOutExtras() {
    var candies = new int[] {6, 5, 4};

    var actual = sut.kidsWithCandies(candies, 2);

    assertThat(actual).containsExactly(true, true, true);
  }

  @Test
  void everyKidEqualMakesEveryoneTrue() {
    var candies = new int[] {3, 3, 3};

    var actual = sut.kidsWithCandies(candies, 1);

    assertThat(actual).containsExactly(true, true, true);
  }

  @Test
  void twoKidsIsTheSmallestInput() {
    var candies = new int[] {1, 1};

    var actual = sut.kidsWithCandies(candies, 1);

    assertThat(actual).containsExactly(true, true);
  }

  @Test
  void maxExtrasLiftEveryoneWithinReach() {
    var candies = new int[] {100, 50, 51};

    var actual = sut.kidsWithCandies(candies, 50);

    assertThat(actual).containsExactly(true, true, true);
  }

  @Test
  void maxExtrasStillLeaveTheWidestGapShort() {
    var candies = new int[] {100, 49};

    var actual = sut.kidsWithCandies(candies, 50);

    assertThat(actual).containsExactly(true, false);
  }

  @Test
  void resultHasOneEntryPerKid() {
    var candies = new int[100];
    Arrays.fill(candies, 7);

    var actual = sut.kidsWithCandies(candies, 1);

    assertThat(actual).hasSize(100).containsOnly(true);
  }
}
