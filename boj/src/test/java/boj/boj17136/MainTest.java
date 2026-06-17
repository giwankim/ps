package boj.boj17136;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

/**
 * BOJ 17136 색종이 붙이기 (Covering with Colored Paper) -- cover every 1 of a 10x10 grid using as few
 * square sheets as possible.
 *
 * <p>Input: ten lines, each ten space-separated values that are 0 or 1. Five kinds of square sheet
 * are available -- 1x1, 2x2, 3x3, 4x4, and 5x5 -- with exactly five sheets of each kind. Sheets
 * must be axis-aligned to the grid, may not extend past the border, may not overlap, and may not
 * cover any 0 cell. Output: the minimum number of sheets that covers all the 1 cells, or -1 when no
 * placement can cover them all (because some 1 cannot be reached, or the supply of a needed size
 * runs out).
 *
 * <p>Two facts drive every expected value below. (1) A 1 cell whose right, down, and down-right
 * neighbours are all 0 can only be covered by a 1x1 sheet -- any larger square rooted at it would
 * spill onto a 0. So a scatter of mutually isolated 1 cells forces one 1x1 each. (2) Each size has
 * only five sheets; once more than five cells demand the same forced size, the grid is impossible.
 * These two facts make the floors ({@link #emptyGridNeedsNoSheet},
 * {@link #singleIsolatedCellNeedsOneOneByOne}) and the supply ceiling
 * ({@link #sixIsolatedCellsExhaustTheOneByOneSupply}) provable by hand, independent of any solver.
 *
 * <p>Minimality is pinned from the other direction: a solid block must collapse to a single large
 * sheet rather than many small ones ({@link #solidTwoByTwoCollapsesToOneSheet},
 * {@link #solidFiveBySquareUsesOneSheet}, {@link #fullGridTiledByFourFiveSquares}), so a correct
 * solver cannot pass these by greedily spending 1x1 sheets.
 *
 * <p>The remaining cases are the statement's own worked examples -- scattered singletons, an
 * L-shape that cannot fold into a square, dense staircases, and multi-block layouts -- carried over
 * verbatim with their published answers (4, 5, -1, 7, 6, 5). They exercise the genuine optimisation
 * the floors and the supply cap alone do not.
 */
class MainTest {

  // --- Floors: nothing to cover, and the smallest non-empty grid. ---

  @Test
  @StdIo({
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0"
  })
  void emptyGridNeedsNoSheet(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Official example 1: there are no 1 cells, so the empty placement already covers everything
    // and
    // the minimum is zero sheets.
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({
    "1 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0"
  })
  void singleIsolatedCellNeedsOneOneByOne(StdOut out) throws IOException {
    Main.main(new String[0]);
    // A lone 1 at (0,0) has only 0 neighbours, so a 2x2 or larger rooted there would cover a 0. The
    // single 1x1 sheet is the only legal cover -- one sheet.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Minimality: a solid block must collapse to one large sheet, not many small ones. ---

  @Test
  @StdIo({
    "1 1 0 0 0 0 0 0 0 0",
    "1 1 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0"
  })
  void solidTwoByTwoCollapsesToOneSheet(StdOut out) throws IOException {
    Main.main(new String[0]);
    // The four 1 cells form a solid 2x2, so a single 2x2 sheet covers them. Spending four 1x1
    // sheets
    // would also cover them but uses four sheets, so the minimum is one.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({
    "1 1 1 1 1 0 0 0 0 0",
    "1 1 1 1 1 0 0 0 0 0",
    "1 1 1 1 1 0 0 0 0 0",
    "1 1 1 1 1 0 0 0 0 0",
    "1 1 1 1 1 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0"
  })
  void solidFiveBySquareUsesOneSheet(StdOut out) throws IOException {
    Main.main(new String[0]);
    // A solid 5x5 block is exactly the largest sheet, so one 5x5 sheet covers all 25 cells -- the
    // minimum, since at least one sheet is always needed.
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  // --- Supply ceiling: only five sheets of each size exist. ---

  @Test
  @StdIo({
    "1 0 1 0 1 0 1 0 1 0",
    "0 0 0 0 0 0 0 0 0 0",
    "1 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0"
  })
  void sixIsolatedCellsExhaustTheOneByOneSupply(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Six mutually isolated 1 cells -- five across row 0 at columns 0,2,4,6,8 and one at (2,0) --
    // can
    // each be covered only by a 1x1 sheet (every larger square would touch a 0). Six 1x1 sheets are
    // required but only five exist, so the grid is impossible: -1.
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  // --- The statement's worked examples, verbatim with their published answers. ---

  @Test
  @StdIo({
    "0 0 0 0 0 0 0 0 0 0",
    "0 1 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 1 0 0 0 0 0",
    "0 0 0 0 0 1 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 1 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0"
  })
  void fourScatteredCellsEachNeedTheirOwnSheet(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Official example 2: four isolated 1 cells, none adjacent to another, so each forces its own
    // 1x1
    // sheet -- four sheets in total.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({
    "0 0 0 0 0 0 0 0 0 0",
    "0 1 1 0 0 0 0 0 0 0",
    "0 0 1 0 0 0 0 0 0 0",
    "0 0 0 0 1 1 0 0 0 0",
    "0 0 0 0 1 1 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 1 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0"
  })
  void lShapeBlockAndSingletonNeedFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Official example 3: the L-shape (1,1),(1,2),(2,2) cannot fold into a 2x2 (its (2,1) corner is
    // 0), so it costs three 1x1 sheets; the solid 2x2 at rows 3-4, cols 4-5 takes one 2x2 sheet;
    // the
    // lone (6,2) takes one 1x1. Three plus one plus one is five.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }

  @Test
  @StdIo({
    "0 0 0 0 0 0 0 0 0 0",
    "0 1 1 0 0 0 0 0 0 0",
    "0 0 1 0 0 0 0 0 0 0",
    "0 0 0 0 1 1 0 0 0 0",
    "0 0 0 0 0 1 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 1 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0"
  })
  void twoLShapesAndSingletonAreImpossible(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Official example 4: two L-shapes -- (1,1),(1,2),(2,2) and (3,4),(3,5),(4,5) -- plus the lone
    // (6,2). Neither L folds into a square, so all seven cells force 1x1 sheets, but only five
    // exist.
    // Seven 1x1 sheets cannot be supplied, so the answer is -1.
    assertThat(out.capturedString().trim()).isEqualTo("-1");
  }

  @Test
  @StdIo({
    "0 0 0 0 0 0 0 0 0 0",
    "0 1 1 0 0 0 0 0 0 0",
    "0 1 1 1 0 0 0 0 0 0",
    "0 0 1 1 1 1 1 0 0 0",
    "0 0 0 1 1 1 1 0 0 0",
    "0 0 0 0 1 1 1 0 0 0",
    "0 0 1 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0",
    "0 0 0 0 0 0 0 0 0 0"
  })
  void denseStaircaseNeedsSeven(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Official example 5: a connected staircase of 1 cells whose minimum cover, mixing sheet sizes
    // under the no-overlap and no-0 rules, is seven sheets.
    assertThat(out.capturedString().trim()).isEqualTo("7");
  }

  @Test
  @StdIo({
    "1 1 1 1 1 1 1 1 1 1",
    "1 1 1 1 1 1 1 1 1 1",
    "1 1 1 1 1 1 1 1 1 1",
    "1 1 1 1 1 1 1 1 1 1",
    "1 1 1 1 1 1 1 1 1 1",
    "1 1 1 1 1 1 1 1 1 1",
    "1 1 1 1 1 1 1 1 1 1",
    "1 1 1 1 1 1 1 1 1 1",
    "1 1 1 1 1 1 1 1 1 1",
    "1 1 1 1 1 1 1 1 1 1"
  })
  void fullGridTiledByFourFiveSquares(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Official example 6: all 100 cells are 1. No sheet covers more than 25 cells, so at least four
    // are needed, and four 5x5 sheets (we own five) tile the 10x10 exactly -- the minimum is four.
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({
    "0 0 0 0 0 0 0 0 0 0",
    "0 1 1 1 1 1 0 0 0 0",
    "0 1 1 1 1 1 0 0 0 0",
    "0 0 1 1 1 1 0 0 0 0",
    "0 0 1 1 1 1 0 0 0 0",
    "0 1 1 1 1 1 1 1 1 1",
    "0 1 1 1 0 1 1 1 1 1",
    "0 1 1 1 0 1 1 1 1 1",
    "0 0 0 0 0 1 1 1 1 1",
    "0 0 0 0 0 1 1 1 1 1"
  })
  void twoLargeClustersNeedSix(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Official example 7: two large irregular clusters of 1 cells whose optimal cover, fitting the
    // largest sheets that stay clear of the 0 cells, is six sheets.
    assertThat(out.capturedString().trim()).isEqualTo("6");
  }

  @Test
  @StdIo({
    "0 0 0 0 0 0 0 0 0 0",
    "1 1 1 1 1 0 0 0 0 0",
    "1 1 1 1 1 0 1 1 1 1",
    "1 1 1 1 1 0 1 1 1 1",
    "1 1 1 1 1 0 1 1 1 1",
    "1 1 1 1 1 0 1 1 1 1",
    "0 0 0 0 0 0 0 0 0 0",
    "0 1 1 1 0 1 1 0 0 0",
    "0 1 1 1 0 1 1 0 0 0",
    "0 1 1 1 0 0 0 0 0 1"
  })
  void mixedBlocksAndScatterNeedFive(StdOut out) throws IOException {
    Main.main(new String[0]);
    // Official example 8: several rectangular blocks of differing sizes plus a lone (9,9) cell. The
    // optimal cover across the mix of block shapes and the supply limits is five sheets.
    assertThat(out.capturedString().trim()).isEqualTo("5");
  }
}
