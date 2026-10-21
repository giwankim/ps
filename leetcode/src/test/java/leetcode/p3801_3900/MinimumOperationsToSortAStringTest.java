package leetcode.p3801_3900;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MinimumOperationsToSortAStringTest {
  MinimumOperationsToSortAString sut = new MinimumOperationsToSortAString();

  // ===========================================================================================
  // Legal input floor and a single proper-substring sort (Steps 1-7).
  // ===========================================================================================

  // Step 1: one character is the legal floor and is already sorted. Having no nonempty proper
  //         substring does not make an already sorted input impossible
  @Test
  void singleCharacterNeedsNoOperation() {
    assertThat(sut.minOperations("a")).isZero();
  }

  // Step 2: non-descending order permits equal neighbors. A strict-increase check incorrectly
  //         rejects this sorted pair, whose only proper substrings are single characters
  @Test
  void equalPairIsAlreadySorted() {
    assertThat(sut.minOperations("aa")).isZero();
  }

  // Step 3: a pair spanning both alphabet endpoints is also sorted. Checking length two before
  //         checking the existing order incorrectly returns the impossibility sentinel
  @Test
  void orderedPairNeedsNoOperation() {
    assertThat(sut.minOperations("az")).isZero();
  }

  // Step 4: sorting the entire string is forbidden. Neither single-character substring changes
  //         this reversed pair, so a solver that allows the whole string incorrectly returns 1
  @Test
  void reversedPairCannotBeSorted() {
    assertThat(sut.minOperations("za")).isEqualTo(-1);
  }

  // Step 5: the initial minimum can stay in place while the suffix cb becomes bc. Restricting
  //         operations to prefixes misses this one-operation repair
  @Test
  void sortingProperSuffixCanFinishInOneOperation() {
    assertThat(sut.minOperations("acb")).isEqualTo(1);
  }

  // Step 6: the mirror repair keeps the final maximum fixed and sorts ba to ab. Looking only
  //         for a minimum at the beginning misses this equally valid one-operation case
  @Test
  void sortingProperPrefixCanFinishInOneOperation() {
    assertThat(sut.minOperations("bac")).isEqualTo(1);
  }

  // Step 7: both endpoints can already be correct while the middle is inverted. Looking only
  //         at those endpoints mistakes acbd for an already sorted string
  @Test
  void correctEndpointsDoNotMakeAnInvertedMiddleSorted() {
    assertThat(sut.minOperations("acbd")).isEqualTo(1);
  }

  // ===========================================================================================
  // Moving misplaced extremes and distinguishing two operations from three (Steps 8-17).
  // ===========================================================================================

  // Step 8: cab becomes acb and then abc. One proper substring cannot repair both endpoints,
  //         but treating every string that starts with its maximum as a three-step case overcounts
  @Test
  void interiorMinimumAllowsTwoOperationsWithMaximumFirst() {
    assertThat(sut.minOperations("cab")).isEqualTo(2);
  }

  // Step 9: bca becomes bac and then abc. The interior maximum is the mirror opportunity, so
  //         checking only where the minimum appears misses this two-operation repair
  @Test
  void interiorMaximumAllowsTwoOperationsWithMinimumLast() {
    assertThat(sut.minOperations("bca")).isEqualTo(2);
  }

  // Step 10: cba needs three operations. Sorting the noncontiguous endpoints would take one,
  //          but a substring is contiguous and the entire string cannot be selected
  @Test
  void reversedTripleNeedsThreeProperSubstringSorts() {
    assertThat(sut.minOperations("cba")).isEqualTo(3);
  }

  // Step 11: repeating the middle letter leaves the unique minimum and maximum at opposite
  //          wrong ends. Treating any duplicate as enough to reduce the answer to two is wrong
  @Test
  void repeatedInteriorLetterDoesNotRemoveTheThirdOperation() {
    assertThat(sut.minOperations("cbba")).isEqualTo(3);
  }

  // Step 12: an extra minimum before the last position reduces the answer to two. Remembering
  //          only the final occurrence of a misses the earlier copy that makes this possible
  @Test
  void repeatedMinimumBeforeTheLastPositionAllowsTwoOperations() {
    assertThat(sut.minOperations("cbaa")).isEqualTo(2);
  }

  // Step 13: an extra maximum after the first position is the mirror case. Remembering only
  //          the first occurrence of c incorrectly leaves this in the three-operation category
  @Test
  void repeatedMaximumAfterTheFirstPositionAllowsTwoOperations() {
    assertThat(sut.minOperations("ccba")).isEqualTo(2);
  }

  // Step 14: both extremes repeat internally, but neither endpoint is already correct. The
  //          duplicate copies allow two operations without making a single operation sufficient
  @Test
  void repeatingBothExtremesStillRequiresTwoOperations() {
    assertThat(sut.minOperations("ccbaa")).isEqualTo(2);
  }

  // Step 15: both extremes can start strictly inside the string. A solver that recognizes only
  //          the maximum-first or minimum-last patterns overlooks this two-operation case
  @Test
  void interiorMinimumAndMaximumCanBothBeMovedInTwoOperations() {
    assertThat(sut.minOperations("bdac")).isEqualTo(2);
  }

  // Step 16: the first minimum stays fixed even when more copies occur later, including at the
  //          end. Requiring that anchored minimum to be unique incorrectly rejects this shortcut
  @Test
  void repeatedMinimumAtTheBeginningStillAllowsOneOperation() {
    assertThat(sut.minOperations("abaca")).isEqualTo(1);
  }

  // Step 17: the last maximum stays fixed even when another copy appears earlier. Counting
  //          extreme occurrences before checking the usable endpoint can overcount this repair
  @Test
  void repeatedMaximumAtTheEndStillAllowsOneOperation() {
    assertThat(sut.minOperations("bcaac")).isEqualTo(1);
  }

  // ===========================================================================================
  // Official examples, including the proper-substring impossibility (Steps 18-20).
  // ===========================================================================================

  // Step 18: the official explanation sorts only og into go, leaving d fixed. Requiring the
  //          selected substring to start at index zero misses this one-operation answer
  @Test
  void leetCodeExample1() {
    assertThat(sut.minOperations("dog")).isEqualTo(1);
  }

  // Step 19: sorting car gives acrd, then sorting rd gives acdr. A single sort of the entire
  //          string would produce the same result but is forbidden, so the minimum is two
  @Test
  void leetCodeExample2() {
    assertThat(sut.minOperations("card")).isEqualTo(2);
  }

  // Step 20: both legal substrings have length one and change nothing. Repeated no-op sorts
  //          cannot rescue gf, so failure must use -1 rather than a positive operation count
  @Test
  void leetCodeExample3() {
    assertThat(sut.minOperations("gf")).isEqualTo(-1);
  }

  // ===========================================================================================
  // Maximum length: 100,000 characters in every case (Steps 21-27).
  //
  // Linear scans or an O(n log n) sort fit these timeouts. Enumerating all proper substrings
  // needs about five billion candidates, so a quadratic search cannot finish in time.
  // ===========================================================================================

  // Step 21: the full-length sorted input spans the lowest and highest lowercase letters.
  //          An endpoint shortcut must not charge an operation before checking the existing order
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthSortedStringNeedsNoOperation() {
    String s = "a".repeat(50_000) + "z".repeat(50_000);

    assertThat(sut.minOperations(s)).isZero();
  }

  // Step 22: all equal letters are non-descending even at the maximum length. Treating equality
  //          as an inversion or assuming the minimum and maximum differ rejects this input
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthUniformStringNeedsNoOperation() {
    assertThat(sut.minOperations("z".repeat(100_000))).isZero();
  }

  // Step 23: the inversion appears only at the final pair, so the whole input must be examined.
  //          Sorting the long suffix is one operation regardless of how many swaps it replaces
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthSuffixCanBeSortedInOneOperation() {
    String s = "a" + "z".repeat(99_998) + "b";

    assertThat(sut.minOperations(s)).isEqualTo(1);
  }

  // Step 24: the mirror input keeps the final maximum and repairs a long prefix. A strategy
  //          that only uses a fixed minimum at the beginning misses this one-operation repair
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthPrefixCanBeSortedInOneOperation() {
    String s = "y" + "a".repeat(99_998) + "z";

    assertThat(sut.minOperations(s)).isEqualTo(1);
  }

  // Step 25: the second copy of the minimum appears just before the last position. Endpoint
  //          checks or a scan that skips the penultimate character incorrectly require three
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthRepeatedMinimumAllowsTwoOperations() {
    String s = "z" + "m".repeat(99_997) + "aa";

    assertThat(sut.minOperations(s)).isEqualTo(2);
  }

  // Step 26: the unique minimum and maximum remain at opposite wrong ends despite the long
  //          equal middle. Capping every sortable input at two operations undercounts this case
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthUniqueExtremesNeedThreeOperations() {
    String s = "z" + "m".repeat(99_998) + "a";

    assertThat(sut.minOperations(s)).isEqualTo(3);
  }

  // Step 27: every lowercase letter repeats in descending cycles. Counting inversions or
  //          separately sorting each descending run overcounts a string that needs only two sorts
  @Test
  @Timeout(value = 5, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
  void maximumLengthDescendingAlphabetCyclesNeedTwoOperations() {
    char[] letters = new char[100_000];
    for (int i = 0; i < letters.length; i++) {
      letters[i] = (char) ('z' - i % 26);
    }

    assertThat(sut.minOperations(new String(letters))).isEqualTo(2);
  }

  // ===========================================================================================
  // Reusing one solution instance (Step 28).
  // ===========================================================================================

  // Step 28: the largest call sits between smaller inputs with every possible answer. Cached
  //          extrema, sorted flags, and the previous operation count must reset for each call
  @Test
  void oneInstanceAnswersDifferentSizesWithoutLeakingState() {
    assertThat(sut.minOperations("za")).isEqualTo(-1);
    assertThat(sut.minOperations("acb")).isEqualTo(1);
    assertThat(sut.minOperations("z" + "m".repeat(2046) + "a")).isEqualTo(3);
    assertThat(sut.minOperations("z")).isZero();
    assertThat(sut.minOperations("cbaa")).isEqualTo(2);
    assertThat(sut.minOperations("aa")).isZero();
  }
}
