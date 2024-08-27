package ch04;

public class JavaPerf {
  public static void main(String[] args) {
    long startTime;
    int index;

    // int[] 1억 개 삽입
    startTime = System.currentTimeMillis();
    int[] intElements = new int[1_000_000];
    for (int i = 0; i < intElements.length - 1; i++) {
      intElements[i] = 1;
    }
    intElements[999_999] = 2;
    System.out.printf("int[] 1억 개 삽입: %s밀리초\n", System.currentTimeMillis() - startTime);

    // int[] 1억 개 중 찾기
    startTime = System.currentTimeMillis();
    index = 0;
    while (2 != intElements[index]) {
      index++;
    }
    System.out.printf("int[] 1억 개 중 찾기: %s밀리초\n", System.currentTimeMillis() - startTime);

    // ---

    // Integer[] 1억 개 삽입
    startTime = System.currentTimeMillis();
    Integer[] integerElements = new Integer[1_000_000];
    for (int i = 0; i < integerElements.length - 1; i++) {
      integerElements[i] = 1;
    }
    integerElements[999_999] = 2;
    System.out.printf("Integer[] 1억 개 삽입: %s밀리초\n", System.currentTimeMillis() - startTime);

    // Integer[] 1억 개 중 찾기
    startTime = System.currentTimeMillis();
    index = 0;
    while (2 != integerElements[index]) {
      index++;
    }
    System.out.printf("Integer[] 1억 개 중 찾기: %s밀리초\n", System.currentTimeMillis() - startTime);
  }
}
