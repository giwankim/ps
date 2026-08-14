# Most Valuable Programmer

문제 ID: `MVP`

## 문제

#### 문제

The season of professional programming league has just ended and it’s time for the MVP(Most Valuable Programmer) voting! There are n newspapers reporters who can vote, and the number of candidate programmers is m.

The voting process will be in several stages:

* Each reporter sorts the list of programmers in his/her preference, and will vote for the most preferred programmer on his list.
* If the programmer has eliminated in former stage, he/she will vote for the second most preferred programmer.
* If that programmer’s also eliminated, he/she will vote for the third most preferred programmer, and so on.

After each stage, the programmers who got the least number of votes will be eliminated. In case of tie, all programmers tied at the least number of votes will be eliminated. However, if all candidate programmers have the same number of votes, all of them will share the MVP title and the voting process also ends.

Given each reporter’s sorted list, compute who will be the MVP.

## 입력

#### 입력

Your program is to read from standard input. The input consists of T test cases. The number of test cases T(1 <= T <= 100) is given in the first line of the input. In the first line of each case, two integer n (1 <= n <= 100) and m (3 <= m <= 10) will be given. In the next n lines, the preference list of each reporter will be given. Each number is in the range of 1 and m, and no number is repeated. The first number in the list represents the reporter’s most preferred programmer, and the m-th number will represent the least preferred programmer.

## 출력

#### 출력

Your program is to write to standard output. Print exactly one line for each test case. For each test case, print the number of the MVP(s) in increasing order. Separate each player’s number by a single space, and do not print any heading/trailing whitespace(s).

## 노트

#### 노트
