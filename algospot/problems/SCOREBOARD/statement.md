# Scoreboard

문제 ID: `SCOREBOARD`

## 문제

#### 문제

ICPC(Individual Coders Programming Contest) is an annual and international contest sponsored and hosted by Algospot. This year, an overwhelming number of problems were proposed for the contest, and the judges decided to use all of them. As a result, the scoreboard table layout were broken due to large number of columns. To resolve this problem, judges decided to  
abbreviate the coders handle(ID).

The abbreviated handle must be a prefix of the coder’s original handle, and it should not be a prefix of any other coder’s abbreviated handle. Furthermore, judges may choose one special coder and display his/her name as an empty string of length 0. Though an empty string is a prefix of any coder’s abbreviated handle, we will accept it as a special case. The problem is to  
minimize the sum of the length of abbreviated handles. For example, assume that there are three coders “jm”, “joe”, and “ainu”. If judges choose “ainu”, the abbreviated handles will be “jm”, “jo”, and “” (empty string) with total length of 4. However, if judges choose “joe”, the abbreviated handles will be “j”, “” and “a” with total length 2, and 2 is the minimum length we can achieve. Given the list of the coders handle, write a program to compute the minimum total abbreviated length. You may assume that the handles are distinct, and there is no handle that is prefix of any other coder’s handle.

## 입력

#### 입력

Your program is to read from standard input. The input consists of T test cases. The number of test cases T(1 <= T <= 100) is given in the first line of the input. In the first line of each case, an integer n(1 <= n <= 500) will be given. In the next n lines, each coder’s handle will be given. A Handle will contain only lowercase alphabets. The length of any handle will be greater than 0, and will not exceed 500.

## 출력

#### 출력

Your program is to write to standard output. Print exactly one line for each test case. For each test case, print the minimum total abbreviated length.

## 노트

#### 노트
