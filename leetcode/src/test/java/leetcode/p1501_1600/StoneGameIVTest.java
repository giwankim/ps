package leetcode.p1501_1600;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StoneGameIVTest {
  StoneGameIV sut = new StoneGameIV();

  // Step 1: smallest valid input (1 <= n) and LeetCode Example 1 — Alice removes the single
  //         stone, Bob has no move left and loses
  @Test
  void oneStoneIsTakenWholeAndWins() {
    assertThat(sut.winnerSquareGame(1)).isTrue();
  }

  // Step 2: LeetCode Example 2 — the first losing pile. Alice's only legal move is 1, and the
  //         single stone she leaves behind is exactly what Bob needs to win
  @Test
  void twoStonesLoseBecauseTheOnlyMoveHandsBobAWin() {
    assertThat(sut.winnerSquareGame(2)).isFalse();
  }

  // Step 3: the mirror of Step 2 — Alice takes one stone precisely so that Bob inherits the
  //         losing pile of two
  @Test
  void threeStonesWinByLeavingBobTheLosingPileOfTwo() {
    assertThat(sut.winnerSquareGame(3)).isTrue();
  }

  // Step 4: LeetCode Example 3 — the whole pile is a legal move when n itself is a square, so a
  //         move loop bounded by i * i < n answers false here. It also rules out "always take one
  //         stone", since 4 - 1 = 3 is a win for Bob by Step 3
  @Test
  void fourStonesAreClearedInASingleMove() {
    assertThat(sut.winnerSquareGame(4)).isTrue();
  }

  // Step 5: squares are available and both of them still lose — 5 - 1 = 4 and 5 - 4 = 1 are wins
  //         for Bob by Steps 4 and 1. Being able to move is not the same as having a good move,
  //         so a solution that returns true whenever some square fits fails here
  @Test
  void fiveStonesLoseAlthoughBothSquareMovesAreLegal() {
    assertThat(sut.winnerSquareGame(5)).isFalse();
  }

  // Step 6: one stone off the losing pile of five, so taking one wins
  @Test
  void sixStonesWinByLeavingBobTheLosingPileOfFive() {
    assertThat(sut.winnerSquareGame(6)).isTrue();
  }

  // Step 7: the second losing pile — 7 - 1 = 6 and 7 - 4 = 3 are both wins for Bob
  @Test
  void sevenStonesLoseWhicheverSquareAliceRemoves() {
    assertThat(sut.winnerSquareGame(7)).isFalse();
  }

  // Step 8: the first pile where taking the largest square is a mistake. Removing 4 leaves 4,
  //         which Bob clears outright; the only winning move is the smallest one, leaving the
  //         losing pile of seven. A greedy "remove the biggest square that fits" answers false
  @Test
  void eightStonesWinOnlyByTakingOneStoneRatherThanTheLargestSquare() {
    assertThat(sut.winnerSquareGame(8)).isTrue();
  }

  // Step 9: the opposite greedy failure at the very next square. Taking one stone leaves 8, a win
  //         for Bob by Step 8, so Alice must remove 4 or clear all 9. Steps 8 and 9 together leave
  //         no fixed choice of move that survives both
  @Test
  void nineStonesWinOnlyByTakingFourOrAllNine() {
    assertThat(sut.winnerSquareGame(9)).isTrue();
  }

  // Step 10: the losing piles below 25 fall into a tidy rhythm — 2, 5, 7, 10, 12, 15, 17, 20, 22,
  //          which is every n that is 0 or 2 modulo 5. The next two steps exist because that
  //          rhythm is a coincidence, and any solution that latches onto it passes everything here
  @Test
  void losingPilesBelowTwentyFiveArriveEveryFiveStones() {
    assertThat(sut.winnerSquareGame(10)).isFalse();
    assertThat(sut.winnerSquareGame(12)).isFalse();
    assertThat(sut.winnerSquareGame(15)).isFalse();
    assertThat(sut.winnerSquareGame(17)).isFalse();
    assertThat(sut.winnerSquareGame(20)).isFalse();
    assertThat(sut.winnerSquareGame(22)).isFalse();
  }

  // Step 11: the rhythm of Step 10 breaks here. 25 is 0 modulo 5 yet Alice wins, and clearing the
  //          pile is her only winning move — every smaller square leaves Bob a win
  @Test
  void twentyFiveStonesBreakTheRhythmByBeingClearedOutright() {
    assertThat(sut.winnerSquareGame(25)).isTrue();
  }

  // Step 12: the break in the other direction — 34 is 4 modulo 5, which the Step 10 rhythm calls
  //          a win, and all five square moves from it lose. Steps 11 and 12 are the pair that a
  //          modular shortcut cannot satisfy at the same time
  @Test
  void thirtyFourStonesLoseDespiteSittingOutsideTheRhythm() {
    assertThat(sut.winnerSquareGame(34)).isFalse();
  }

  // Step 13: the winning move is neither the smallest nor the largest square. From 28 only 16
  //          works, and from 29 only 9 does, while the largest square that fits either pile is 25.
  //          The move has to be searched for, not picked from an end of the range
  @Test
  void theOnlyWinningMoveCanBeASquareFromTheMiddleOfTheRange() {
    assertThat(sut.winnerSquareGame(28)).isTrue();
    assertThat(sut.winnerSquareGame(29)).isTrue();
  }

  // Step 14: a square pile is always won by clearing it, whatever else is true of the position
  @Test
  void everySquarePileIsWon() {
    assertThat(sut.winnerSquareGame(16)).isTrue();
    assertThat(sut.winnerSquareGame(36)).isTrue();
    assertThat(sut.winnerSquareGame(49)).isTrue();
    assertThat(sut.winnerSquareGame(64)).isTrue();
    assertThat(sut.winnerSquareGame(81)).isTrue();
  }

  // Step 15: past 25 the losing piles are irregular — 44 and 52 sit five apart, but 39 to 44 is a
  //          gap of five and 44 to 52 a gap of eight, so no fixed stride reproduces them
  @Test
  void losingPilesPastTwentyFiveAreIrregularlySpaced() {
    assertThat(sut.winnerSquareGame(39)).isFalse();
    assertThat(sut.winnerSquareGame(44)).isFalse();
    assertThat(sut.winnerSquareGame(52)).isFalse();
    assertThat(sut.winnerSquareGame(57)).isFalse();
    assertThat(sut.winnerSquareGame(62)).isFalse();
  }

  // Step 16: a three-digit pile whose only winning move is to clear it — all nine smaller squares
  //          leave Bob a win. The search has to reach the top of the square range, not sample it
  @Test
  void oneHundredStonesWinOnlyByClearingThePile() {
    assertThat(sut.winnerSquareGame(100)).isTrue();
  }

  // Step 17: one stone more, and clearing is no longer possible — Alice wins instead through one
  //          of the four smaller squares that strand Bob
  @Test
  void oneHundredOneStonesWinThroughASmallerSquare() {
    assertThat(sut.winnerSquareGame(101)).isTrue();
  }

  // ===========================================================================================
  // Upper end of the constraint (1 <= n <= 10^5).
  //
  // These pin down the property the small piles cannot: every pile from 1 to n has to be settled
  // once, not re-derived per move. There are 316 squares at this size, so a search without
  // memoization does not finish, and the answers below are not reachable by any pattern shorter
  // than the full table.
  //
  // What they deliberately do not pin down is recursion depth. ps.test-conventions runs tests with
  // -Xss64m, so a top-down solution recursing from n down to 0 one stone at a time clears 10^5
  // frames here even though a 1MB stack gives out far earlier. Passing this section is therefore
  // not evidence that a recursive solution survives a judge; only a bottom-up loop makes the depth
  // question moot.
  // ===========================================================================================

  // Step 18: the maximum pile — Alice wins, and none of her six winning moves is the largest
  //          square that fits
  @Test
  void maximumPileIsWon() {
    assertThat(sut.winnerSquareGame(100000)).isTrue();
  }

  // Step 19: the largest losing pile in range. All 316 square moves lead to piles Bob wins, so a
  //          single wrong entry anywhere in the table below it turns this answer into true
  @Test
  void largestLosingPileInRangeIsLost() {
    assertThat(sut.winnerSquareGame(99919)).isFalse();
  }

  // Step 20: the counterpart of Step 19 — 99955 has exactly one winning move out of 316, removing
  //          36 stones to leave Bob the losing pile of 99919. A solution that scores the position
  //          by anything coarser than the exact table cannot find that one move
  @Test
  void pileWithASingleWinningMoveNearTheMaximumIsWon() {
    assertThat(sut.winnerSquareGame(99955)).isTrue();
  }

  // Step 21: one stone under the maximum, still won, and by a different set of moves than Step 18
  @Test
  void oneStoneUnderTheMaximumIsWon() {
    assertThat(sut.winnerSquareGame(99999)).isTrue();
  }

  // Step 22: several piles answered by one instance, largest last. A solution that memoizes into
  //          a table sized to the first n it is asked about answers the later, larger piles from
  //          a table that is too small
  @Test
  void oneInstanceAnswersPilesOfAnySizeInAnyOrder() {
    assertThat(sut.winnerSquareGame(1)).isTrue();
    assertThat(sut.winnerSquareGame(99919)).isFalse();
    assertThat(sut.winnerSquareGame(2)).isFalse();
    assertThat(sut.winnerSquareGame(100000)).isTrue();
    assertThat(sut.winnerSquareGame(4)).isTrue();
  }
}
