# Nerd, or Not Nerd?

문제 ID: `NERDS`

## 문제

#### 문제

Algospot.com's annual programming contest is drawing near, and there are overwhelming number of registrations (10,000+!). We have only 5 judges, so we cannot handle this number of teams. Therefore, we decided to admit only the "true nerds" to the contest.

JongMan came up with a theory that whether a person's *nerdiness* is dependent on a linear combination of two factors: their shoe size and their typing speed per minute. In his theory, the nerd score

```
F = A * (shoe size) + B * (typing speed per minute)
```

will be able to determine whether the guy is a nerd or not. The higher the score, the nerdier the guy is. We intend to set a threshold T; we will only admit people with scores equal to or above T.

However, will this theory work? To find it out, we grabbed the data from some people we personally know. We know their shoe size and typing speeds, and we know whether each of them is a nerd or not. Will there be a set of parameters A, B and T which can successfully separate nerds from the others? Write a program to confirm this.

Please note that A, B and T can be arbitrary real numbers.

## 입력

#### 입력

The first line of the input file will contain the number of test cases, C (C = 50). Each test case begins n (6 <= n <= 5,000) with the number of people we personally know. n lines will follow with three nonnegative integers in each line. The first integer is 1 if the person is a nerd, and 0 otherwise. The second number and the third number will represent the person's shoe size and typing speed per minute. These two numbers will be in the range [0, 10000].

There will be always at least three people from both group of people - nerds and non-nerds. Also, for each group, the two vectors formed by gathering their shoe sizes and typing speed, will be linearly independent. That is, for one group, we make one vector [shoe[0], shoe[1], shoe[2], ..] and another vector [speed[0], speed[1], speed[2], ..] and they will be linearly independent.

## 출력

#### 출력

For each test case, print one line. The line must contain "THEORY HOLDS" if there are one or more set of parameters which satisfies the given data. "THEORY IS INVALID" should be printed in the other case. See the sample output for details.

## 노트

#### 노트

* Case 1: There are infinite many solutions - for example, let A = -8, B = 10. Then, the eight people's nerdiness score is 14, 16, 18, 34, -22, 10, 6, 8 respectively. We set T = 12, and everybody is correctly classified.
* Case 2: There is no solution which will correctly classify nerds in this case.
* Case 3: There are two people with same data, but one is a nerd and the other is not. So, there is no way to tell them apart.
