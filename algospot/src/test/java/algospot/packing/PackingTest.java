package algospot.packing;

import java.io.IOException;

/**
 * Runs the {@link PackingContract} ladder against {@link Main}, the bottom-up tabulation that fills
 * a {@code [item][capacity]} table and then walks it backwards to recover the chosen items.
 */
class PackingTest extends PackingContract {

  @Override
  void solve() throws IOException {
    Main.main(new String[0]);
  }
}
