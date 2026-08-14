# Lucky Lucky Number

문제 ID: `LUCKYNUM`

## 문제

#### 문제

Haden is so interested in numbers, especially large natural numbers. He defined 'lucky lucky numbers' as following:

First, he picks two natural numbers n and k. n can be quite large (up to 200 digits) and k must be between 5 and 8, inclusive.

Then, he defines the lucky lucky number as a number consisting of k's only and with the same length as that of n. For instance, if n is a three digit number and k is 5, the lucky lucky number is 555. Haden becomes quite bored of this lucky lucky number definition so that he defined 'alternate lucky number'. Any alternate lucky number, given n and k, must satisfy the following two properties:

1. the number must be an anagram of n
2. the number must be closest to the lucky lucky number of n and k.

Here, 'closest' means absolute difference between two numbers. Depending on n and k, there can be several such alternate lucky numbers. The best alternate lucky number among them is the largest. Haden wants you to help him find the best alternate lucky number given n and k.

## 입력

#### 입력

First line contains the number of test cases T (less than 200). Next T lines contain two integers each, n and k. n is a natural number with no leading zero(s), up to 200 digits. k is one of 5, 6, 7, and 8.

## 출력

#### 출력

For each test case, print the lucky lucky number, which is an anagram of n. Note that the number should not contain leading zero(s).

## 노트

#### 노트
