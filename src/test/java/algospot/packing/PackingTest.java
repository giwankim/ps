package algospot.packing;

import static algospot.packing.Main.main;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class PackingTest {

  @Test
  @StdIo({
    """
      2
      6 10
      laptop 4 7
      camera 2 10
      xbox 6 6
      grinder 4 7
      dumbell 2 5
      encyclopedia 10 4
      6 17
      laptop 4 7
      camera 2 10
      xbox 6 6
      grinder 4 7
      dumbell 2 5
      encyclopedia 10 4
      """
  })
  void packingTest(StdOut out) throws IOException {
    main(new String[0]);

    assertThat(out.capturedLines())
        .containsExactly(
            "24 3", "laptop", "camera", "grinder", "30 4", "laptop", "camera", "xbox", "grinder");
  }
}
