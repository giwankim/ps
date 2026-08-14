# Divisibility

문제 ID: `DIVIS`

## 문제

#### 문제

On the planet Zoop, numbers are represented in base 62, using the digits

0, 1, . . . , 9, A, B, . . . , Z, a, b, . . . , z, where

* A (base 62) = 10 (base 10)
* B (base 62) = 11 (base 10)
* z (base 62) = 61 (base 10).

Given the digit representation of a number x in base 62, your goal is to determine if x is divisible by 61.

## 입력

#### 입력

The input test file will contain multiple cases. Each test case will be given by a single string containing only  
the digits ‘0’ through ‘9’, the uppercase letters ‘A’ through ‘Z’, and the lowercase letters ’a’ through ’z’. All  
strings will have a length of between 1 and 10000 characters, inclusive. The end-of-input is denoted by a  
single line containing the word “end”, which should not be processed.

## 출력

#### 출력

For each test case, print “yes” if the number is divisible by 61, and “no” otherwise.

In the first example, 1v3 = 1 × 622 + 57 × 62 + 3 = 7381, which is divisible by 61.  
In the second example, 2P6 = 2 × 622 + 25 × 62 + 6 = 9244, which is not divisible by 61.

## 노트

#### 노트
