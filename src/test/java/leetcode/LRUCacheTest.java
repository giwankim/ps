package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LRUCacheTest {
  LRUCache cache;

  @BeforeEach
  void setUp() {
    cache = new LRUCache(2);
  }

  @Test
  void get() {
    cache.put(1, 1);
    assertThat(cache.get(1)).isEqualTo(1);
  }

  @Test
  void getDoesNotExist() {
    cache.put(1, 1);
    assertThat(cache.get(2)).isEqualTo(-1);
  }

  @Test
  void getEvicted() {
    cache.put(1, 1);
    cache.put(2, 2);
    cache.put(3, 3);
    assertThat(cache.get(1)).isEqualTo(-1);
  }

  @Test
  void put() {}

  @Test
  void lruCache() {
    SoftAssertions.assertSoftly(
        softly -> {
          cache.put(1, 1); // {1: 1}
          cache.put(2, 2); // {1: 1, 2: 2}
          softly.assertThat(cache.get(1)).isEqualTo(1);

          cache.put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
          softly.assertThat(cache.get(2)).isEqualTo(-1);

          cache.put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3}
          softly.assertThat(cache.get(1)).isEqualTo(-1); // return -1 (not found)

          softly.assertThat(cache.get(3)).isEqualTo(3);
          softly.assertThat(cache.get(4)).isEqualTo(4);
        });
  }
}
