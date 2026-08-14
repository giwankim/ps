# Change

문제 ID: `CHANGE`

## 문제

#### 문제

Sue is waiting in line at the grocery store. Being in a hurry, she wants to pay with exact change when she gets to the front of the line. However, she does not know how much her items are going to cost; instead, she only knows an upper bound C on their total cost. Given a list of the various coins Sue has in her pocket, your goal is to determine the minimum number of coins she must take out in order to ensure that she can make exact change for every amount from 1 to C.

## 입력

#### 입력

The input test file will contain multiple cases. Each test case begins with a single line containing two integers C (where 1 ≤ C ≤ 1000000000) and m (where 1 ≤ m ≤ 1000), where C is the maximum amount for which Sue must be able to make change, and m is the number of unique coin denominations Sue has in her pocket. The next m lines each contain two numbers, vi (where 1 ≤ vi ≤ 1000) and ni (where 1 ≤ ni ≤ 1000), where vi is the value of the ith coin denomination, and ni is the number of coins of that denomination that Sue has in her pocket. Input is terminated by a single line containing the number 0; do not process this line.

## 출력

#### 출력

For each test case, either print a single line containing the number of coins Sue must use in order to make exact change for all amounts up to C, or print “Not possible” if exact change cannot always be made with any combination of coins in Sue’s pocket.

## 노트

#### 노트
