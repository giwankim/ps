# Collecting Bills

문제 ID: `BILLS`

## 문제

#### 문제

After being retired from ACM-ICPC, Mr. R decided to quit studying computer science to become a farmer. This year, he harvested $N$ cabbages, and the price of one cabbage is $K$ won. R wants to sell all of these cabbages to the Farm Co-Op, but the Farm Co-Op has a rather complicated purchasing policy:

1. R can split the cabbages into any number of bunches, and sell them separately.
2. Every time R cells a bunch of cabbages, he has two ways to collect his earnings:
   1. He can get a check: however, checks can be issued only for amounts multiple of 10,000 won, and the remaining money will be thrown away.
   2. He can get the money wired to his bank account: however, the bank will take 10% as transaction fee and round down the remaining money to the nearest won.

So, how much money can Mr.R can get at most by selling all of the cabbages?

## 입력

#### 입력

The first line of the input file will contain the number of test cases, $C$. Each line consists of two integers, $N$ and $K$.

* $C \le 1,000$
* $1 \le N \le 100,000,000$
* $1 \le K \le 100,000,000$
* $K \times N \le 2^{31}-1$

## 출력

#### 출력

For each test case, print the maximum amount of money Mr.R can get.

## 노트

#### 노트
