# Ball Painting

문제 ID: `BALLPAINTING`

## 문제

#### 문제

There are 2N white balls on a table in two rows, making a nice 2-by-N rectangle. Jon has a big paint bucket full of black paint. (Don’t ask why.) He wants to paint all the balls black, but he would like to have some math fun while doing it. (Again, don’t ask why.) First, he computed the number of diﬀerent ways to paint all the balls black. In no time, he ﬁgured out that the answer is (2N)! and thought it was too easy. So, he introduced some rules to make the problem more interesting.

* The ﬁrst ball that Jon paints can be any one of the 2N balls.
* After that, each subsequent ball he paints must be adjacent to some black ball (that was already painted). Two balls are assumed to be adjacent if they are next to each other horizontally, vertically, or diagonally.

Jon was quite satisﬁed with the rules, so he started counting the number of ways to paint all the balls according to them. Can you write a program to ﬁnd the answer faster than Jon?

## 입력

#### 입력

The input consists of multiple test cases. Each test case consists of a single line containing an integer N, where 1 ≤ N ≤ 1,000.

## 출력

#### 출력

For each test case, print out a single line that contains the number of possible ways that Jon can paint all the 2N balls according to his rules. The number can become very big, so print out the number modulo 1,000,000,007.

## 노트

#### 노트
