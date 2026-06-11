package support;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;

/**
 * Test harness for CSES solutions: runs a solution's {@code main} on inline stdin ({@link #run}) or
 * on every official {@code N.in}/{@code N.out} data pair under {@code src/test/resources/<slug>/}
 * ({@link #tests}).
 */
public final class Cses {
  private Cses() {}

  /** The entry-point shape every CSES solution submits: a main method. */
  @FunctionalInterface
  public interface Solver {
    void main(String[] args) throws IOException;
  }

  /**
   * Runs the solution's main with the given stdin content and returns its stdout with trailing
   * whitespace stripped, so expectations are written without a trailing newline. Leading whitespace
   * is preserved: a solution that emits it should fail. The real streams are restored even when the
   * solution throws.
   */
  public static String run(Solver solver, String input) throws IOException {
    InputStream originalIn = System.in;
    PrintStream originalOut = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
      System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
      solver.main(new String[0]);
      return out.toString(StandardCharsets.UTF_8).stripTrailing();
    } finally {
      System.setIn(originalIn);
      System.setOut(originalOut);
    }
  }

  /**
   * Creates one test per {@code N.in}/{@code N.out} pair under {@code src/test/resources/<slug>/},
   * in numeric order, comparing trailing-whitespace-stripped output. Validation is eager: a missing
   * or empty folder, an input without a matching output, or a non-numeric input file name throws
   * from this call rather than producing a silently empty (and passing) stream.
   */
  public static Stream<DynamicTest> tests(String slug, Solver solver) {
    List<Path> inputs = discoverInputs(slug);
    return inputs.stream()
        .map(in -> DynamicTest.dynamicTest(stem(in), () -> {
          String expected = Files.readString(expectedOutputFor(in)).stripTrailing();
          assertThat(run(solver, Files.readString(in))).isEqualTo(expected);
        }));
  }

  private static List<Path> discoverInputs(String slug) {
    URL url = Cses.class.getClassLoader().getResource(slug);
    if (url == null) {
      throw new IllegalStateException("no test data folder on the test classpath for: " + slug);
    }
    List<Path> inputs;
    try (Stream<Path> files = Files.list(Path.of(url.toURI()))) {
      inputs = files
          .filter(p -> p.getFileName().toString().endsWith(".in"))
          .map(in -> Map.entry(number(in), in))
          .sorted(Map.Entry.comparingByKey())
          .map(Map.Entry::getValue)
          .toList();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (URISyntaxException e) {
      throw new IllegalStateException("test data folder is not on the file system: " + slug, e);
    }
    if (inputs.isEmpty()) {
      throw new IllegalStateException("no .in files in test data folder: " + slug);
    }
    for (Path in : inputs) {
      if (!Files.isRegularFile(expectedOutputFor(in))) {
        throw new IllegalStateException("no matching output file: " + expectedOutputFor(in));
      }
    }
    return inputs;
  }

  private static int number(Path in) {
    try {
      return Integer.parseInt(stem(in));
    } catch (NumberFormatException e) {
      throw new IllegalStateException("input file name is not a number: " + in.getFileName(), e);
    }
  }

  private static String stem(Path in) {
    String name = in.getFileName().toString();
    return name.substring(0, name.length() - ".in".length());
  }

  private static Path expectedOutputFor(Path in) {
    return in.resolveSibling(stem(in) + ".out");
  }
}
