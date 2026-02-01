package support;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

public class ResourceUtils {
  private ResourceUtils() {}

  public static Path getPath(String name) throws URISyntaxException {
    return Path.of(
        Objects.requireNonNull(ResourceUtils.class.getClassLoader().getResource(name)).toURI());
  }
}
