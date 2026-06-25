package leetcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DesignHashMapTest {
  DesignHashMap hashMap;

  @BeforeEach
  void setUp() {
    hashMap = new DesignHashMap();
    hashMap.put(1, 1);
  }

  @Test
  void put() {
    hashMap.put(2, 2);
    assertThat(hashMap.get(2)).isEqualTo(2);
  }

  @Test
  void get() {
    assertThat(hashMap.get(1)).isEqualTo(1);
  }

  @Test
  void getKeyDoesNotExist() {
    assertThat(hashMap.get(3)).isEqualTo(-1);
  }

  @Test
  void remove() {
    hashMap.remove(1);
    assertThat(hashMap.get(1)).isEqualTo(-1);
  }

  @Test
  void hashMap() {
    hashMap.put(2, 2);
    assertThat(hashMap.get(1)).isEqualTo(1);
    assertThat(hashMap.get(3)).isEqualTo(-1);

    hashMap.put(2, 1);
    assertThat(hashMap.get(2)).isEqualTo(1);

    hashMap.remove(2);
    assertThat(hashMap.get(2)).isEqualTo(-1);
  }
}
