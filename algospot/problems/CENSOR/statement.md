# Censorship

문제 ID: `CENSOR`

## 문제

#### 문제

As part of the new educational reform program, the CS department has decided to engage in censorship of school texts. In this problem, you must help the department by writing a program which eliminates from an input text string all occurrences of strings from a set of words to be filtered.

More formally, a word w can be removed from another string s if w is a substring of s (i.e., the characters of w appear consecutively in s). Given a text string s and a set T of words to be filtered, return the length of the shortest possible string that can result from iteratively removing words in T from s. Each word in T may be removed from s an unlimited number of times.

## 입력

#### 입력

The input test file will contain multiple cases, with each case on a single line of input. Each test case begins with a single integer n (where 1 ≤ n ≤ 50) indicating the size of the set T followed by a text string s to be processed. Then, n strings t1 . . . tn indicating the words of T follow. The text string and all of the filter words are guaranteed to contain only the characters ‘a’ through ‘z’ and will have lengths between 1 and 50.

All filter words will be unique. Input is terminated by a single line containing the number 0; do not process this line.

## 출력

#### 출력

For each test case, print a single integer indicating the minimum length resulting string possible.

Possible reductions giving the lengths shown for the three sample inputs are:

* ccdedefcde → cdefcde → fcde → f
* aabaab → baab → ab → ǫ
* aabaab → baab → bb → ǫ,

where ǫ denotes the empty string.

## 노트

#### 노트
