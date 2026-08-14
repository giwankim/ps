# Normal Distribution

문제 ID: `NORMDIST`

## 문제

#### 문제

Hyun-hwan is the instructor for ACM-ICPC contestants in Ajou University. Last week, he held a team selection contest for the seoul regional contest. After the contest is over, he calculated scores of each contestants using an obscure formula. According to this formula, each contest receives a score between 0 to N − 1 (inclusive) and N is multiple of 10.

As a measure of the quality of the problem set, we want to find out if distribution of the scores follow the normal distribution or not: we believe a better problem set will result in normally distributed scores. Unfortunately, Hyun-hwan is not good at statistics, so he came up with a simple alternative procedure. The procedure is as follows:

Let f[i] = number of scores between 10\*i to 10\*i+9 ( 0 <= i <= N/10-1 ) Then, if the following relation

```
f[0] < f[1] < ... < f[k] > f[k + 1] > ... > f[N/10 − 1]
```

holds for some k (k > 0 and N/10-1-k > 0), we can say 'the distribution of the scores is similar to normal distribution', otherwise, we can’t say that.

Write a program that will determine distribution of given scores is similar to normal distribution.

## 입력

#### 입력

Your program is to read from standard input. The input consists of T test cases. The number of test cases T (1 <= T <= 20) is given in the first line of the input. In the first line of each case, two integers S (1 <= S <= 20, 000) and N (10 <= N <= 100) will be given. In the second line of each case, scores of S contestants will be given.

## 출력

#### 출력

Your program is to write to standard output. Print exactly one line for each test case. For each test case, print ‘`YES`’ if the distribution of the scores is similar to normal distribution, otherwise print ‘`NO`’(without quotes).

## 노트

#### 노트
