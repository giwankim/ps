package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class StoneGameIITest {
  StoneGameII sut = new StoneGameII();

  // Step 1: smallest valid input (1 <= piles.length) — Alice's opening move can take
  // 1..2M = 1..2 piles, so the lone pile is hers and the game ends immediately
  @Test
  void singlePileGoesEntirelyToAlice() {
    assertThat(sut.stoneGameII(new int[] {5})).isEqualTo(5);
  }

  // Step 2: with M = 1 the opening move already reaches 2 piles, so Bob never gets a turn
  @Test
  void twoPilesAreBothTakenOnTheOpeningMove() {
    assertThat(sut.stoneGameII(new int[] {1, 2})).isEqualTo(3);
  }

  // Step 3: the first position with a real choice. Taking 1 leaves 2 piles, which Bob can
  // sweep at M = 1; taking 2 leaves only 1 pile for Bob. Alice takes 2 -> 1 + 2 = 3.
  @Test
  void threePilesAliceTakesTwoToDenyBobTheRest() {
    assertThat(sut.stoneGameII(new int[] {1, 2, 3})).isEqualTo(3);
  }

  // Step 4: the first position where taking *fewer* wins. Taking 2 raises M to 2, letting
  // Bob reach the 100. Taking 1 keeps Bob capped at 2 piles, so he settles for 2 + 3 = 5
  // and the 100 falls to Alice: 1 + 100 = 101.
  @Test
  void aliceTakesOnePileToKeepBobAwayFromTheJackpot() {
    assertThat(sut.stoneGameII(new int[] {1, 2, 3, 100})).isEqualTo(101);
  }

  // Step 5: LeetCode Example 1 — Alice takes 1, Bob takes 2, Alice takes 2: 2 + 4 + 4 = 10.
  // Opening with 2 piles would concede all three remaining piles, leaving her only 9.
  @Test
  void leetCodeExample1() {
    assertThat(sut.stoneGameII(new int[] {2, 7, 9, 4, 4})).isEqualTo(10);
  }

  // Step 6: LeetCode Example 2 — four single-pile moves hold M at 1 until Bob is forced to
  // stop short of the 100, which Alice then claims: 1 + 3 + 100 = 104.
  @Test
  void leetCodeExample2() {
    assertThat(sut.stoneGameII(new int[] {1, 2, 3, 4, 5, 100})).isEqualTo(104);
  }

  // Step 7: with identical piles there is nothing to fight over — Alice takes 1, Bob takes 2,
  // Alice takes the last one, splitting 4 stones 2-2
  @Test
  void fourEqualPilesSplitEvenly() {
    assertThat(sut.stoneGameII(new int[] {1, 1, 1, 1})).isEqualTo(2);
  }

  // Step 8: pins M = max(M, X) rather than M = X. Alice takes 2 (M -> 2), Bob takes only 1,
  // and because M stays at 2 Alice may still take 4, sweeping piles 3..6 before Bob is left
  // the trailing 3: Alice = 2 + 4 = 6. Had Bob's small take reset M to 1, Alice's next cap
  // would be 2 and she would finish with 5.
  @Test
  void takeCapNeverShrinksAfterASmallMove() {
    assertThat(sut.stoneGameII(new int[] {1, 1, 1, 1, 1, 1, 1, 3})).isEqualTo(6);
  }

  // Step 9: moving first is not an advantage here. Alice's opening cap of 2 cannot reach the
  // 100 at index 2, and either opening lets Bob take it, so she nets 2 + 1 + 1 + 1 + 1 = 6
  // out of 108 stones.
  @Test
  void aliceCanBeShutOutOfTheLargestPile() {
    assertThat(sut.stoneGameII(new int[] {2, 2, 100, 1, 1, 1, 1})).isEqualTo(6);
  }

  // Step 10: the mirror of step 9 — the jackpot sits in front, so Alice banks it on move one
  // and the trailing ones alternate to give her 3 more: 103 of 106
  @Test
  void leadingJackpotIsTakenImmediately() {
    assertThat(sut.stoneGameII(new int[] {100, 1, 1, 1, 1, 1, 1})).isEqualTo(103);
  }

  // Step 11: upper constraint bounds (100 piles of 10^4). With every pile equal there is no
  // positional edge, so the 10^6 stones split exactly in half — and the total stays far
  // inside int range.
  @Test
  void maximumUniformInputSplitsEvenly() {
    int[] piles = new int[100];
    Arrays.fill(piles, 10_000);
    assertThat(sut.stoneGameII(piles)).isEqualTo(500_000);
  }

  // Step 12: full-size input with distinct values (100, 200, ..., 10000). Ascending piles make
  // every move a real decision, so this exercises the whole (i, m) state space and would time
  // out on an unmemoized search.
  @Test
  void maximumAscendingInputIsSolvedWithoutExponentialSearch() {
    int[] piles = new int[100];
    for (int i = 0; i < piles.length; i++) {
      piles[i] = (i + 1) * 100;
    }
    assertThat(sut.stoneGameII(piles)).isEqualTo(252_600);
  }
}
