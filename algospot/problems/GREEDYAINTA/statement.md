# Greedy Seunghyun

문제 ID: `GREEDYAINTA`

## 문제

#### 문제

Jaehyun, who teaches $N$ children in his kindergarten, decided to give them candies, because it was his birthday! Each child is denoted as an integer number from 0 to $N-1$. Because Jaehyun tends to give freedom to the kids, he let them take candies as much as they want. As a result, child $i$ ($0 \le i < N$) took $a\_{i}$ candies.

Seunghyun, who is denoted as child $0$, was disappointed because he couldn't obtain all the candies. After a deep thought, Seunghyun came out with an idea. He gathered all the kids in the kindergarten, and suggested to distribute candies by the following way:

1. Seunghyun chooses an integer $k$ which is bigger than $0$ and smaller than $N$.
2. Child $k$ must hand out one candy to each of child $0$, child $1$, ... child $k-1$. After this, child $k$ will lose $k$ candies, and child $0$, child $1$, $\cdots$, child $k-1$ will get one candy.

All the children agreed because they thought that it was a great idea! However, as the only purpose of this suggestion was taking all the candies, Seunghyun wants to know whether he could achieve his goal by repeating this process 0 or more times.

You are to write a program which determines whether Seunghyun can get all the candies.

## 입력

#### 입력

The first line of the input contains the number of test cases $T$.

Each test case starts with a line containing $N$ ($2 \le N \le 100,000$), the number of children that Jaehyun teaches. The following line contains $N$ space-separated integers $a\_{0}, a\_{1}, \cdots, a\_{N-1}$, where $a\_{i}$ ($0 \le a\_{i} \le 10^{9}$) is the number of candies that child $i$ has taken.

## 출력

#### 출력

For each test case, if Seunghyun can take all candies, print "POSSIBLE" (without quotes). If not print "IMPOSSIBLE" (without quotes).

## 노트

#### 노트
