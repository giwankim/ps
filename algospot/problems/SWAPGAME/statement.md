# Swap Game

문제 ID: `SWAPGAME`

## 문제

#### 문제

StarMask is another superheroine that you’ve never heard of. As she is a heroine anyway, it is always your pleasure to play a game with her. This time, you are playing a game to sort a permutation of $N$ numbers. In a permutation, each number from 1 to $N$ appears exactly once and these are denoted as $P[1], …, P[N]$. The goal of this game is to sort a given permutation in increasing order using only one exchange rule. The rule is to follow the instructions below sequentially.

1. Choose two numbers $i$ and $j$ such that $1 \le i<j \le N$.
2. Find the maximum value and minimum value among $P[i], P[i + 1], …, P[j - 1], P[j]$.
3. Exchange these two values.

StarMask knows $N$ but does not know the permutation. Meanwhile, you know the exact permutation. At the beginning of the game, you announce the inversion of the permutation. The inversion is defined as the number of pairs of $(i, j)$ such that $1 \le i < j \le N$ and $P[i] > P[j]$. For example, a permutation “2 3 1” has the inversion of 2. As the game goes on, StarMask uses the exchange rule explained above, and you announce the inversion after each exchange rule is executed. Of course, the optimal strategy will be depend on your announcement.  
Unfortunately, StarMask’s super power is about physical strength not intellectual brightness and she already made her mind to use which exchange rules even before the game has started. Now, it doesn’t have to be responsive that you are going to make a program to announce the inversion instead of you. That way, you can have fun playing other games with StarMask. But be careful. She will be very mad if your announcement is wrong.

## 입력

#### 입력

The first line of the input is $T$ which shows the number of the test cases. The first line of a test case has two numbers $N (1 \le N \le 100,000)$ and $M (0 \le M \le 100,000)$. These show the size of permutation and the number of exchanges that StarMask ordered, respectably. The second line of a test case has $N$ numbers showing $P[1]$ to $P[N]$. Then, from the third line, a pair of two integers $i$ and $j$ ($1 \le i<j \le N$) are given $M$ times showing the orders of StarMask.

## 출력

#### 출력

For each test case, your program should print a integer: sum of $M+1$ inversions that you were supposed to announce.

## 노트

#### 노트

First Example: StarMask had no interest and didn’t exchange any numbers. So, you simply need to announce the inversion of the initial permutation.

Second Example: As you follow the orders of StarMask, the permutation changes as below. On each line, red numbers represent the maximum and blue numbers represent the minimum of a given range.

|  |  |
| --- | --- |
| permutation | inversion |
| [5 6 3 4 2 1] | 13 |
| 5 [1 3 4] 2 6 | 6 |
| 5 4 3 1 2 6 | 9 |
