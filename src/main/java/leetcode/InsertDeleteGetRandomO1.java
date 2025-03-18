package leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class InsertDeleteGetRandomO1 {
  private final Map<Integer, Integer> numberToIndex;
  private final List<Integer> numbers;
  private final Random random;

  public InsertDeleteGetRandomO1() {
    numberToIndex = new HashMap<>();
    numbers = new ArrayList<>();
    random = new Random();
  }

  public boolean insert(int val) {
    if (numberToIndex.containsKey(val)) {
      return false;
    }
    numberToIndex.put(val, numbers.size());
    numbers.add(val);
    return true;
  }

  public boolean remove(int val) {
    if (!numberToIndex.containsKey(val)) {
      return false;
    }
    // swap the number to delete with the last number
    int index = numberToIndex.get(val);
    numbers.set(index, numbers.getLast());
    numberToIndex.put(numbers.getLast(), index);
    // delete the last number
    numbers.removeLast();
    numberToIndex.remove(val);
    return true;
  }

  public int getRandom() {
    int index = random.nextInt(numbers.size());
    return numbers.get(index);
  }
}
