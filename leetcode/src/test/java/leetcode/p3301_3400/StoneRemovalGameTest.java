package leetcode.p3301_3400;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class StoneRemovalGameTest {
  StoneRemovalGame sut = new StoneRemovalGame();

  // ===========================================================================================
  // The first move (Steps 1-4). Removals run 10, 9, 8, ... so the running totals are
  // 10, 19, 27, 34, 40, 45, 49 and the answer flips each time n reaches the next total.
  // ===========================================================================================

  // Step 1: the floor of the constraints, and LeetCode Example 2. Alice must remove exactly 10 and
  //         cannot, so she loses before the game starts. A solution that lets a player take
  //         "up to" the required count would have Alice take the single stone and win
  @Test
  void n1AliceCannotMakeTheFirstMove() {
    assertThat(sut.canAliceWin(1)).isFalse();
  }

  // Step 2: the last pile that is still too small for the opening move. One stone short of 10 is
  //         as lost as n = 1
  @Test
  void n9IsOneStoneShortOfTheOpeningMove() {
    assertThat(sut.canAliceWin(9)).isFalse();
  }

  // Step 3: exactly 10 stones is enough. Alice empties the pile and Bob, needing 9, cannot move.
  //         A loop guarded by "n > take" instead of "n >= take" answers false here
  @Test
  void n10AliceTakesTheWholePile() {
    assertThat(sut.canAliceWin(10)).isTrue();
  }

  // Step 4: LeetCode Example 1. Alice removes 10, leaving 2, and Bob cannot remove 9. Leftover
  //         stones do not help the player to move, who needs the exact count
  @Test
  void leetCodeExample1() {
    assertThat(sut.canAliceWin(12)).isTrue();
  }

  // ===========================================================================================
  // Bob's reply (Steps 5-7). The opponent removes 9, one fewer than Alice's 10.
  // ===========================================================================================

  // Step 5: 8 stones remain after Alice's move, one short of the 9 Bob needs, so Alice still wins.
  //         This is the top of the first winning band, 10 through 18
  @Test
  void n18BobIsOneStoneShortOfHisReply() {
    assertThat(sut.canAliceWin(18)).isTrue();
  }

  // Step 6: 10 + 9 = 19, so Bob empties the pile and Alice, needing 8, loses. Making the first
  //         move is not the same as winning: "return n >= 10" answers true, and so does a
  //         solution where every move removes 10 (it sees Bob stuck with 9 stones)
  @Test
  void n19BobAnswersAndAliceIsStuck() {
    assertThat(sut.canAliceWin(19)).isFalse();
  }

  // Step 7: the top of the losing band 19 through 26. After 10 and 9 there are 7 stones left and
  //         Alice needs 8. An even pile is not a winning pile, and "return n >= 10" still says
  //         true
  @Test
  void n26AliceIsOneStoneShortOfHerSecondMove() {
    assertThat(sut.canAliceWin(26)).isFalse();
  }

  // ===========================================================================================
  // The decrement is shared between the players (Steps 8-10).
  // ===========================================================================================

  // Step 8: "1 fewer than the previous opponent" means Alice's second move is 8, not 9. Here
  //         10 + 9 + 8 = 27 empties the pile and Bob cannot remove 7. A solution where each
  //         player counts down from their own previous move (10, 9, 9, 8, ...) leaves Alice
  //         needing 9 with 8 stones and answers false. So does the every-move-removes-10 solution
  @Test
  void n27AlicesSecondMoveRemovesEight() {
    assertThat(sut.canAliceWin(27)).isTrue();
  }

  // Step 9: the top of the second winning band, 27 through 33. After 10, 9, and 8 there are 6
  //         stones left and Bob needs 7
  @Test
  void n33BobIsOneStoneShortOfHisSecondMove() {
    assertThat(sut.canAliceWin(33)).isTrue();
  }

  // Step 10: 10 + 9 + 8 + 7 = 34, so Bob empties the pile on the fourth move and Alice, needing
  //          6, loses. The per-player countdown of Step 8 has Bob needing 8 with 7 stones and
  //          answers true
  @Test
  void n34BobsSecondMoveRemovesSeven() {
    assertThat(sut.canAliceWin(34)).isFalse();
  }

  // ===========================================================================================
  // The bands keep shrinking (Steps 11-13). Each band is one stone narrower than the last, so a
  // solution built on a fixed period drifts further out of step as n grows.
  // ===========================================================================================

  // Step 11: five moves, 10 + 9 + 8 + 7 + 6 = 40, and Alice makes the fifth. A period-10 rule
  //          such as "n / 10 is odd" answers false for every n from 40 through 44
  @Test
  void n40AliceMakesTheFifthMove() {
    assertThat(sut.canAliceWin(40)).isTrue();
  }

  // Step 12: six moves, the sum reaching 45, and Bob makes the sixth. Alice then needs 4 and the
  //          pile is empty
  @Test
  void n45BobMakesTheSixthMove() {
    assertThat(sut.canAliceWin(45)).isFalse();
  }

  // Step 13: the top of the last losing band, 45 through 48. Three stones remain and Alice needs
  //          4. "n / 10 is odd" happens to agree here, which is why Step 11 and Step 14 pin it
  //          from both sides
  @Test
  void n48AliceIsOneStoneShortOfTheSeventhMove() {
    assertThat(sut.canAliceWin(48)).isFalse();
  }

  // ===========================================================================================
  // The upper constraint bound (Steps 14-15). n <= 50, so at most seven moves ever happen and
  // the required count never falls below 3.
  // ===========================================================================================

  // Step 14: seven moves, 10 + 9 + 8 + 7 + 6 + 5 + 4 = 49, and Alice makes the seventh. Bob
  //          needs 3 and the pile is empty. "n / 10 is odd" answers false
  @Test
  void n49AliceMakesTheSeventhMove() {
    assertThat(sut.canAliceWin(49)).isTrue();
  }

  // Step 15: the maximum n. One stone is left after seven moves and Bob needs 3, so Alice wins.
  //          The eighth move would need the total to reach 52, which no legal input does
  @Test
  void n50MaximumPileStillEndsOnBobsTurn() {
    assertThat(sut.canAliceWin(50)).isTrue();
  }

  // ===========================================================================================
  // Hygiene (Step 16).
  // ===========================================================================================

  // Step 16: one instance answers several piles, deliberately out of order with the largest in
  //          the middle. A solution that keeps the pile, the next removal count, or whose turn it
  //          is in a field without resetting it per call carries state from one game into the next
  @Test
  void oneInstanceAnswersManyPilesIndependently() {
    assertThat(sut.canAliceWin(19)).isFalse();
    assertThat(sut.canAliceWin(10)).isTrue();
    assertThat(sut.canAliceWin(50)).isTrue();
    assertThat(sut.canAliceWin(1)).isFalse();
    assertThat(sut.canAliceWin(34)).isFalse();
    assertThat(sut.canAliceWin(27)).isTrue();
    assertThat(sut.canAliceWin(19)).isFalse();
  }
}
