package boj.boj1417;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

class MainTest {
  @Test
  @StdIo({"3", "5", "7", "7"})
  void sampleTwoLeadersNeedTwoBribes(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("2");
  }

  @Test
  @StdIo({"4", "10", "10", "10", "10"})
  void sampleFourWayTieNeedsOneBribe(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"1", "1"})
  void sampleSingleCandidateNeedsNoBribes(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"5", "5", "10", "7", "3", "8"})
  void sampleSeveralCandidatesRequireRepeatedBuying(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("4");
  }

  @Test
  @StdIo({"3", "10", "9", "9"})
  void alreadyStrictlyAheadNeedsNoBribes(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("0");
  }

  @Test
  @StdIo({"2", "1", "1"})
  void tieWithOnlyOpponentStillNeedsStrictWin(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"2", "1", "2"})
  void oneVoteBehindOnlyOpponentNeedsOneBribe(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("1");
  }

  @Test
  @StdIo({"2", "1", "100"})
  void oneOpponentAtVoteCeilingRequiresHalfTheGapRoundedUp(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("50");
  }

  @Test
  @StdIo({"3", "1", "15", "14"})
  void bribesRotateBetweenCloseLeadersToStayMinimal(StdOut out) throws IOException {
    Main.main(new String[0]);
    assertThat(out.capturedString().trim()).isEqualTo("10");
  }

  @Test
  void maximumCandidateCountWithAllOpponentsAtVoteCeiling() throws IOException {
    StringBuilder input = new StringBuilder();
    input.append(50).append('\n');
    input.append(1).append('\n');
    for (int i = 0; i < 49; i++) {
      input.append(100).append('\n');
    }

    assertThat(runMain(input.toString())).isEqualTo("98");
  }

  private static String runMain(String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

      Main.main(new String[0]);

      return new String(out.toByteArray(), StandardCharsets.UTF_8).trim();
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }
}
