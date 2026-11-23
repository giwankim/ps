package leetcode.p1301_1400;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import leetcode.p1301_1400.ProductOfTheLastKNumbers.ProductOfNumbers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class ProductOfTheLastKNumbersTest {
  ProductOfNumbers sut = new ProductOfNumbers();

  // ===========================================================================================
  // The window is the tail of the stream (Steps 1-7).
  // ===========================================================================================

  // Step 1: the floor. 1 <= k and the stream always holds at least k numbers, so the smallest legal
  //         sequence is one add followed by getProduct(1). There is no query on an empty stream
  @Test
  void singleNumberIsItsOwnProduct() {
    sut.add(7);

    assertThat(sut.getProduct(1)).isEqualTo(7);
  }

  // Step 2: "last" means most recently added. A solution that reads from the front of the stream
  //         answers 2
  @Test
  void k1ReturnsTheNewestNumber() {
    sut.add(2);
    sut.add(5);

    assertThat(sut.getProduct(1)).isEqualTo(5);
  }

  // Step 3: k may equal the stream size, in which case the window is everything. 2 * 3 * 4 = 24.
  //         An off-by-one that stops one short of the oldest number answers 12
  @Test
  void kEqualToStreamSizeMultipliesEverything() {
    sut.add(2);
    sut.add(3);
    sut.add(4);

    assertThat(sut.getProduct(3)).isEqualTo(24);
  }

  // Step 4: a shorter k takes the tail, not the head. stream = [2, 3, 4, 5]: the last two are
  //         4 * 5 = 20 and the last three are 3 * 4 * 5 = 60. Taking the first k answers 6 and 24
  @Test
  void kSmallerThanStreamSizeTakesTheTail() {
    sut.add(2);
    sut.add(3);
    sut.add(4);
    sut.add(5);

    assertThat(sut.getProduct(2)).isEqualTo(20);
    assertThat(sut.getProduct(3)).isEqualTo(60);
  }

  // Step 5: getProduct is a read. Asking twice gives the same answer, and a narrower query after a
  //         wider one still sees the stream intact. A solution that pops what it multiplies fails
  //         the second call
  @Test
  void getProductDoesNotConsumeTheStream() {
    sut.add(2);
    sut.add(3);

    assertThat(sut.getProduct(2)).isEqualTo(6);
    assertThat(sut.getProduct(2)).isEqualTo(6);
    assertThat(sut.getProduct(1)).isEqualTo(3);
  }

  // Step 6: adds and queries interleave, and each add slides the window. [2, 3] -> 6, then
  //         [2, 3, 4] -> last two are 12 and all three are 24. A cached answer stays at 6
  @Test
  void addAfterQuerySlidesTheWindow() {
    sut.add(2);
    sut.add(3);
    assertThat(sut.getProduct(2)).isEqualTo(6);

    sut.add(4);

    assertThat(sut.getProduct(2)).isEqualTo(12);
    assertThat(sut.getProduct(3)).isEqualTo(24);
  }

  // Step 7: 1 is a real number in the stream even though it leaves the product unchanged. It still
  //         occupies a slot in the window. stream = [1, 1, 1, 5, 1]: the last one is 1, the last
  //         two are 5, all five are 5. A solution that skips ones to save space miscounts k
  @Test
  void onesOccupyWindowSlots() {
    sut.add(1);
    sut.add(1);
    sut.add(1);
    assertThat(sut.getProduct(3)).isEqualTo(1);

    sut.add(5);
    sut.add(1);

    assertThat(sut.getProduct(1)).isEqualTo(1);
    assertThat(sut.getProduct(2)).isEqualTo(5);
    assertThat(sut.getProduct(5)).isEqualTo(5);
  }

  // ===========================================================================================
  // Zero (Steps 8-15). 0 <= num, and one zero is what breaks a plain prefix-product array.
  // ===========================================================================================

  // Step 8: zero is a legal first number, and the product of a window holding only that zero is 0.
  //         A solution that resets its prefix products on zero and then reports the product of
  //         the now-empty run answers 1
  @Test
  void zeroAsTheOnlyNumber() {
    sut.add(0);

    assertThat(sut.getProduct(1)).isZero();
  }

  // Step 9: a zero anywhere inside the window makes the product 0. stream = [3, 0, 2]: the last
  //         three and the last two both contain the zero
  @Test
  void zeroInsideTheWindowMakesTheProductZero() {
    sut.add(3);
    sut.add(0);
    sut.add(2);

    assertThat(sut.getProduct(3)).isZero();
    assertThat(sut.getProduct(2)).isZero();
  }

  // Step 10: a zero outside the window does not touch the answer. stream = [3, 0, 2, 5]: the last
  //          two are 2 * 5 = 10. Prefix products kept across the zero are [3, 0, 0, 0], so the
  //          division for this window is 0 / 0 and throws ArithmeticException
  @Test
  void zeroOutsideTheWindowIsIgnored() {
    sut.add(3);
    sut.add(0);
    sut.add(2);
    sut.add(5);

    assertThat(sut.getProduct(2)).isEqualTo(10);
    assertThat(sut.getProduct(1)).isEqualTo(5);
  }

  // Step 11: the boundary of Step 10. Same stream, one step wider: k = 3 reaches back exactly to
  //          the zero, so the answer flips from 10 to 0. Two numbers follow the zero, so the test
  //          is "k greater than 2", and a solution that compares with the wrong strictness is off
  //          by one right here
  @Test
  void windowReachingExactlyBackToTheZero() {
    sut.add(3);
    sut.add(0);
    sut.add(2);
    sut.add(5);

    assertThat(sut.getProduct(3)).isZero();
    assertThat(sut.getProduct(4)).isZero();
  }

  // Step 12: when the zero is the newest number, every window contains it, k = 1 included
  @Test
  void zeroAsTheNewestNumberZeroesEveryWindow() {
    sut.add(2);
    sut.add(5);
    sut.add(0);

    assertThat(sut.getProduct(1)).isZero();
    assertThat(sut.getProduct(2)).isZero();
    assertThat(sut.getProduct(3)).isZero();
  }

  // Step 13: a zero is not permanent. Numbers added after it start a fresh run: [2, 0, 3] -> last
  //          one is 3, then [2, 0, 3, 4] -> last two are 12 while the last three still hit the
  //          zero. A solution that latches a "seen zero" flag answers 0 forever
  @Test
  void streamRecoversAfterAZero() {
    sut.add(2);
    sut.add(0);
    assertThat(sut.getProduct(1)).isZero();

    sut.add(3);
    assertThat(sut.getProduct(1)).isEqualTo(3);

    sut.add(4);
    assertThat(sut.getProduct(2)).isEqualTo(12);
    assertThat(sut.getProduct(3)).isZero();
  }

  // Step 14: back-to-back zeros. stream = [5, 0, 0, 6, 7]: the last two are 42, and every wider
  //          window contains at least one zero. The second zero resets a run that is already empty
  @Test
  void consecutiveZeros() {
    sut.add(5);
    sut.add(0);
    sut.add(0);
    sut.add(6);
    sut.add(7);

    assertThat(sut.getProduct(2)).isEqualTo(42);
    assertThat(sut.getProduct(3)).isZero();
    assertThat(sut.getProduct(4)).isZero();
    assertThat(sut.getProduct(5)).isZero();
  }

  // Step 15: only the latest zero matters. stream = [2, 0, 3, 0, 4, 5]: the last two are 20, and
  //          the last three reach the second zero. A solution that remembers the position of the
  //          first zero sees it outside the last three and answers 20 for k = 3
  @Test
  void latestZeroDecidesNotTheFirst() {
    sut.add(2);
    sut.add(0);
    sut.add(3);
    sut.add(0);
    sut.add(4);
    sut.add(5);

    assertThat(sut.getProduct(2)).isEqualTo(20);
    assertThat(sut.getProduct(3)).isZero();
    assertThat(sut.getProduct(5)).isZero();
  }

  // ===========================================================================================
  // The official example (Step 16).
  // ===========================================================================================

  // Step 16: stream = [3, 0, 2, 5, 4]. The three queries walk k across the zero: 5 * 4 = 20, then
  //          2 * 5 * 4 = 40 with the zero just outside, then 0 once k = 4 reaches it. The add of 8
  //          afterwards shows the window sliding: 4 * 8 = 32
  @Test
  void leetCodeExample1() {
    sut.add(3);
    sut.add(0);
    sut.add(2);
    sut.add(5);
    sut.add(4);
    assertThat(sut.getProduct(2)).isEqualTo(20);
    assertThat(sut.getProduct(3)).isEqualTo(40);
    assertThat(sut.getProduct(4)).isZero();

    sut.add(8);

    assertThat(sut.getProduct(2)).isEqualTo(32);
  }

  // ===========================================================================================
  // The value range (Steps 17-19). Every contiguous product is guaranteed to fit in an int, and
  // nothing wider is promised.
  // ===========================================================================================

  // Step 17: the largest num. Four 100s are 100,000,000 and a leading 21 brings all five to
  //          2,100,000,000, which sits just under Integer.MAX_VALUE = 2,147,483,647
  @Test
  void largestNumbersNearTheIntCeiling() {
    sut.add(21);
    sut.add(100);
    sut.add(100);
    sut.add(100);
    sut.add(100);

    assertThat(sut.getProduct(4)).isEqualTo(100_000_000);
    assertThat(sut.getProduct(5)).isEqualTo(2_100_000_000);
  }

  // Step 18: the longest run of numbers above 1 that still fits. Thirty 2s are 2^30 =
  //          1,073,741,824, and one more would overflow, so the statement's guarantee stops here
  @Test
  void thirtyTwosReachTwoToTheThirtieth() {
    addRepeated(sut, 2, 30);

    assertThat(sut.getProduct(30)).isEqualTo(1_073_741_824);
    assertThat(sut.getProduct(29)).isEqualTo(536_870_912);
  }

  // Step 19: the guarantee covers contiguous windows, and a window with a zero in it is 0, so runs
  //          on either side of a zero may each be as large as an int. Three runs of thirty 2s
  //          separated by zeros are legal. A solution that treats zero as 1 and carries one
  //          running product across it holds 2^60 after two runs and 2^90 after three: the first
  //          wraps an int to 0 and the second wraps a long to 0, and dividing by that throws
  @Test
  void fullSizeRunsOnBothSidesOfAZero() {
    addRepeated(sut, 2, 30);
    sut.add(0);
    addRepeated(sut, 2, 30);
    sut.add(0);
    addRepeated(sut, 2, 30);

    assertThat(sut.getProduct(30)).isEqualTo(1_073_741_824);
    assertThat(sut.getProduct(31)).isZero();
    assertThat(sut.getProduct(92)).isZero();
  }

  // ===========================================================================================
  // The call budget (Steps 20-22). At most 4 * 10^4 calls are made to add and getProduct combined.
  // That is too few for a timeout to tell the O(1) follow-up from an O(k) loop: the worst an O(k)
  // getProduct can cost is 20,000 queries of width 20,000, about 4 * 10^8 multiplications, which
  // finishes well inside a second. These steps pin correctness at full size, and the timeouts
  // only stop a runaway solution from hanging the build.
  // ===========================================================================================

  // Step 20: the widest window the budget allows. 39,998 adds leave room for two queries, so
  //          k = 4 * 10^4 itself is never reachable. stream = [0, 1, 1, ..., 1]: the 39,997 ones
  //          are 1, and one step wider reaches the zero at the very front
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void widestWindowTheCallBudgetAllows() {
    sut.add(0);
    addRepeated(sut, 1, 39_997);

    assertThat(sut.getProduct(39_997)).isEqualTo(1);
    assertThat(sut.getProduct(39_998)).isZero();
  }

  // Step 21: the worst case for an O(k) loop. 20,000 adds, then 20,000 queries that each span the
  //          whole stream. The stream is 19,970 ones followed by thirty 2s, so every answer is
  //          2^30 = 1,073,741,824
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void twentyThousandFullWidthQueries() {
    addRepeated(sut, 1, 19_970);
    addRepeated(sut, 2, 30);

    for (int i = 0; i < 20_000; i++) {
      assertThat(sut.getProduct(20_000)).isEqualTo(1_073_741_824);
    }
  }

  // Step 22: 420 resets under interleaved queries. The stream cycles through a zero followed by
  //          thirty 2s. After each add, p numbers follow the latest zero, where p is the position
  //          in the cycle: the window of width p + 1 reaches that zero and is 0, and the window of
  //          width p stops just short of it and is 2^p. 13,000 adds make 38,580 calls in total
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void repeatedResetsUnderInterleavedQueries() {
    for (int i = 0; i < 13_000; i++) {
      int p = i % 31;
      sut.add(p == 0 ? 0 : 2);

      assertThat(sut.getProduct(p + 1)).isZero();
      if (p >= 1) {
        assertThat(sut.getProduct(p)).isEqualTo(1 << p);
      }
    }
  }

  // ===========================================================================================
  // Hygiene (Steps 23-24).
  // ===========================================================================================

  // Step 23: each object owns its stream. Two instances fed in alternation must not see each
  //          other's numbers, and a zero added to one must not reset the other. A solution that
  //          keeps its prefix products in a static field mixes the two streams
  @Test
  void twoInstancesKeepSeparateStreams() {
    ProductOfNumbers other = new ProductOfNumbers();

    sut.add(2);
    other.add(5);
    sut.add(3);
    assertThat(sut.getProduct(2)).isEqualTo(6);
    assertThat(other.getProduct(1)).isEqualTo(5);

    other.add(0);
    sut.add(4);

    assertThat(sut.getProduct(3)).isEqualTo(24);
    assertThat(other.getProduct(2)).isZero();
  }

  // Step 24: one long mixed sequence, with the stream after each group of adds shown on the right.
  //          It crosses a single zero, a pair of zeros, and a run of ones, querying on both sides
  //          of each
  @Test
  void longMixedSequence() {
    sut.add(4); // [4]
    assertThat(sut.getProduct(1)).isEqualTo(4);

    sut.add(5);
    sut.add(1); // [4, 5, 1]
    assertThat(sut.getProduct(3)).isEqualTo(20);

    sut.add(0); // [4, 5, 1, 0]
    assertThat(sut.getProduct(1)).isZero();
    assertThat(sut.getProduct(4)).isZero();

    sut.add(7); // [4, 5, 1, 0, 7]
    assertThat(sut.getProduct(1)).isEqualTo(7);
    assertThat(sut.getProduct(2)).isZero();

    sut.add(1);
    sut.add(3); // [4, 5, 1, 0, 7, 1, 3]
    assertThat(sut.getProduct(3)).isEqualTo(21);
    assertThat(sut.getProduct(4)).isZero();

    sut.add(0);
    sut.add(0);
    sut.add(9); // [4, 5, 1, 0, 7, 1, 3, 0, 0, 9]
    assertThat(sut.getProduct(1)).isEqualTo(9);
    assertThat(sut.getProduct(2)).isZero();

    sut.add(10);
    sut.add(10); // [4, 5, 1, 0, 7, 1, 3, 0, 0, 9, 10, 10]
    assertThat(sut.getProduct(2)).isEqualTo(100);
    assertThat(sut.getProduct(3)).isEqualTo(900);
    assertThat(sut.getProduct(12)).isZero();
  }

  private static void addRepeated(ProductOfNumbers stream, int num, int times) {
    for (int i = 0; i < times; i++) {
      stream.add(num);
    }
  }
}
