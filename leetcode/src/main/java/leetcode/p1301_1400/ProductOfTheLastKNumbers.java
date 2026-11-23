package leetcode.p1301_1400;

import java.util.ArrayList;
import java.util.List;

/**
 * <a href="https://leetcode.com/problems/product-of-the-last-k-numbers/">1352. Product of the Last
 * K Numbers</a>
 *
 * @implNote Time {@code O(1)} for {@code getProduct} and amortized {@code O(1)} for {@code add},
 *     space {@code O(n)}, where {@code n} is the count of numbers added since the last zero.
 */
public class ProductOfTheLastKNumbers {
  private ProductOfTheLastKNumbers() {
    /* This utility class should not be instantiated */
  }

  public static class ProductOfNumbers {
    private int size;
    private final List<Integer> prods = new ArrayList<>();

    public ProductOfNumbers() {
      initialize();
    }

    public void add(int num) {
      if (num == 0) {
        initialize();
        return;
      }
      prods.add(prods.getLast() * num);
      size++;
    }

    public int getProduct(int k) {
      if (k > size) {
        return 0;
      }
      return prods.getLast() / prods.get(prods.size() - 1 - k);
    }

    private void initialize() {
      size = 0;
      prods.clear();
      prods.add(1);
    }
  }
}
