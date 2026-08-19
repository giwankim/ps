package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CinemaSeatAllocationTest {
  CinemaSeatAllocation sut = new CinemaSeatAllocation();

  // Step 1: seat 1 belongs to no block, so a row holding only that reservation is effectively
  // untouched and seats both outer blocks {2,3,4,5} and {6,7,8,9}
  @Test
  void aisleSeatOneBlocksNothing() {
    assertThat(sut.maxNumberOfFamilies(1, new int[][] {{1, 1}})).isEqualTo(2);
  }

  // Step 2: seat 10 is the other dead seat; pinning it separately stops an implementation from
  // excluding only one end of the row
  @Test
  void aisleSeatTenBlocksNothing() {
    assertThat(sut.maxNumberOfFamilies(1, new int[][] {{1, 10}})).isEqualTo(2);
  }

  // Step 3: two is the ceiling for any row — the middle block {4,5,6,7} overlaps both outer blocks,
  // so a wide-open row must answer 2 rather than counting all three usable blocks
  @Test
  void openRowSeatsTwoGroupsNotThree() {
    assertThat(sut.maxNumberOfFamilies(1, new int[][] {{1, 1}, {1, 10}})).isEqualTo(2);
  }

  // Step 4: reserving seat 2 kills the left block; middle and right both survive but share seats
  // 6 and 7, so only one of them can be used
  @Test
  void leftBlockBrokenLeavesOneGroup() {
    assertThat(sut.maxNumberOfFamilies(1, new int[][] {{1, 2}})).isEqualTo(1);
  }

  // Step 5: the mirror of step 4 — seat 9 kills the right block while left and middle survive but
  // collide on seats 4 and 5, so counting both would wrongly report 2
  @Test
  void rightBlockBrokenLeavesOneGroup() {
    assertThat(sut.maxNumberOfFamilies(1, new int[][] {{1, 9}})).isEqualTo(1);
  }

  // Step 6: both outer blocks are broken yet seats 4 through 7 are clear, so the middle block is
  // the only way to seat anyone — an implementation that checks just the outer blocks answers 0
  @Test
  void middleBlockRescuesRowWithBothOuterBlocksBroken() {
    assertThat(sut.maxNumberOfFamilies(1, new int[][] {{1, 2}, {1, 9}})).isEqualTo(1);
  }

  // Step 7: seat 5 sits in both the left and middle blocks, so one reservation kills two blocks at
  // once and only the right block is left
  @Test
  void seatFiveKillsLeftAndMiddleBlocks() {
    assertThat(sut.maxNumberOfFamilies(1, new int[][] {{1, 5}})).isEqualTo(1);
  }

  // Step 8: seat 6 is the mirror hinge — it kills the middle and right blocks, leaving only the
  // left block
  @Test
  void seatSixKillsMiddleAndRightBlocks() {
    assertThat(sut.maxNumberOfFamilies(1, new int[][] {{1, 6}})).isEqualTo(1);
  }

  // Step 9: seats 4 and 7 together touch all three blocks, so a row can legitimately seat nobody
  @Test
  void rowWithEveryBlockBrokenSeatsNobody() {
    assertThat(sut.maxNumberOfFamilies(1, new int[][] {{1, 4}, {1, 7}})).isEqualTo(0);
  }

  // Step 10: seats 5 and 6 each leave one block alive on their own (steps 7 and 8), but together
  // they leave none — reservations must accumulate per row rather than be judged one at a time
  @Test
  void reservationsInTheSameRowAccumulate() {
    assertThat(sut.maxNumberOfFamilies(1, new int[][] {{1, 5}, {1, 6}})).isEqualTo(0);
  }

  // Step 11: rows absent from the input are never enumerated in the array yet still seat two groups
  // each, so the untouched rows have to be counted arithmetically
  @Test
  void rowsAbsentFromInputEachSeatTwoGroups() {
    assertThat(sut.maxNumberOfFamilies(3, new int[][] {{2, 4}, {2, 7}})).isEqualTo(4);
  }

  // Step 12: reservations arrive interleaved and out of row order, so grouping cannot rely on the
  // input already being sorted by row
  @Test
  void reservationsMayArriveInAnyRowOrder() {
    assertThat(sut.maxNumberOfFamilies(3, new int[][] {{3, 4}, {1, 7}, {3, 7}, {1, 4}}))
        .isEqualTo(2);
  }

  // Step 13: LeetCode Example 1 — row 1 keeps only its middle block, row 2 keeps only its left
  // block, and row 3 is blocked solely on the dead aisle seats, so 1 + 1 + 2 = 4
  @Test
  void exampleOneMixesMiddleOnlyAndFullRows() {
    assertThat(sut.maxNumberOfFamilies(
            3, new int[][] {{1, 2}, {1, 3}, {1, 8}, {2, 6}, {3, 1}, {3, 10}}))
        .isEqualTo(4);
  }

  // Step 14: LeetCode Example 2 — every row appears in the input and each seats exactly one group,
  // so no untouched-row shortcut contributes here
  @Test
  void exampleTwoHasNoUntouchedRows() {
    assertThat(sut.maxNumberOfFamilies(2, new int[][] {{2, 1}, {1, 8}, {2, 6}})).isEqualTo(2);
  }

  // Step 15: LeetCode Example 3 — the two reserved rows seat nobody while rows 2 and 3 never appear
  // in the input, so the whole answer comes from rows the array never mentions
  @Test
  void exampleThreeDrawsItsAnswerFromUntouchedRows() {
    assertThat(sut.maxNumberOfFamilies(4, new int[][] {{4, 3}, {1, 4}, {4, 6}, {1, 7}}))
        .isEqualTo(4);
  }

  // Step 16: n at the 10^9 ceiling with every row open gives 2 * 10^9, which fits an int with only
  // 147 million to spare — and is only reachable if untouched rows are counted, never iterated
  @Test
  void rowCeilingProducesTwoBillionGroups() {
    assertThat(sut.maxNumberOfFamilies(1_000_000_000, new int[][] {{1, 1}}))
        .isEqualTo(2_000_000_000);
  }

  // Step 17: the same ceiling with one fully blocked row deep in the middle — the answer must drop
  // by exactly two, catching an off-by-one in how reserved rows are discounted from the total
  @Test
  void oneBlockedRowAtCeilingCostsExactlyTwoGroups() {
    assertThat(sut.maxNumberOfFamilies(
            1_000_000_000, new int[][] {{500_000_000, 4}, {500_000_000, 7}}))
        .isEqualTo(1_999_999_998);
  }
}
