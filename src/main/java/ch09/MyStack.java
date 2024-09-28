package ch09;

public class MyStack {
  MyNode top;

  public MyStack() {
    top = null;
  }

  public void push(int value) {
    top = new MyNode(value, top);
  }

  public int pop() {
    int value = top.value;
    top = top.next;
    return value;
  }

  public static class MyNode {
    int value;
    MyNode next;

    public MyNode(int value, MyNode next) {
      this.value = value;
      this.next = next;
    }
  }

  public static void main(String[] args) {
    MyStack stack = new MyStack();
    stack.push(1);
    stack.push(2);
    stack.push(3);
    stack.push(4);
    stack.push(5);

    for (int i = 0; i < 5; i++) {
      System.out.println(stack.pop());
    }
  }
}
