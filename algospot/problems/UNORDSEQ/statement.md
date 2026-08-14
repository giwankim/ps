# Unordered Subsequence

문제 ID: `UNORDSEQ`

## 문제

#### 문제

The sequence is called **ordered** if it is **non-decreasing** (i.e. Ai <= Ai+1 for 1 <= i < N) or **non-increasing** (i.e. Ai >= Ai+1 for 1 <= i < N). Therefore, the sequence [1, 3, 3, 1] is **not** ordered.

You are given a sequence of numbers. You are to find its shortest subsequence which is **not** ordered. Note that subsequence does not need to be consecutive.

## 입력

#### 입력

The first line of the input contains one integer T, the number of test cases.

The first line of each test case contains one integer N (1 <= N <= 10^5).  
The second line contains N space-seperated integers - the given sequence.  
Absolute values of numbers in input sequences do not exceed 10^6.

## 출력

#### 출력

If the given sequence does not contain any unordered subsequences, output 0.

Otherwise, output the length K of the shortest such subsequence.  
Then output K integers on the next line, representing indexes of the subsequence from the input sequence.

If there are several solutions, output the lexicographically earlist one.  
Sequence A is lexicographically earlier than sequence B if Ai < Bi for the smallest index i such that Ai != Bi.

## 노트

#### 노트
