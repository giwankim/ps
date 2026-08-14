package algospot.packing;

import java.io.IOException;

/**
 * Runs the {@link PackingContract} ladder against {@link MainRecursive}, the memoized top-down
 * variant that recovers the chosen items by re-querying the memo rather than by walking a table.
 *
 * <p>Same contract, different shape of solution -- which is the point of sharing the ladder: the
 * two implementations differ in how they reconstruct the bag, and nothing in the contract may
 * depend on that. In particular this is where pinning an item order rather than an item set would
 * have shown up as a false failure.
 */
class PackingRecursiveTest extends PackingContract {

  @Override
  void solve() throws IOException {
    MainRecursive.main(new String[0]);
  }
}
