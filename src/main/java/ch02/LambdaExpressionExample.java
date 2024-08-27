package ch02;

public class LambdaExpressionExample {
  @FunctionalInterface
  interface MathInterface {
    double getPiValue();
  }

  public static void main(String[] args) {
    MathInterface math1 =
        new MathInterface() {
          @Override
          public double getPiValue() {
            return 3.141592653589793238;
          }
        };
    System.out.println("PI = " + math1.getPiValue());

    // ---

    MathInterface math2 = () -> 3.141592;
    System.out.println("PI = " + math2.getPiValue());
  }
}
