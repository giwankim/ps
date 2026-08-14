# Brackets

문제 ID: `BRACKETS`

## 문제

#### 문제

We give the following inductive definition of a “regular brackets” sequence:

* the empty sequence is a regular brackets sequence,
* if $s$ is a regular brackets sequence, then $(s)$ and $[s]$ are regular brackets sequences, and
* if $a$ and $b$ are regular brackets sequences, then $ab$ is a regular brackets sequence.
* no other sequence is a regular brackets sequence

For instance, all of the following character sequences are regular brackets sequences:

```
(), [], (()), ()[], ()[()]
```

while the following character sequences are not:

```
(, ], )(, ([)], ([(]
```

Given a brackets sequence of characters $a\_1, a\_2, \cdots, a\_n$, your goal is to find the length of the longest regular brackets sequence that is a subsequence of $s$. That is, you wish to find the largest $m$ such that for indices $i\_1, i\_2, \cdots, i\_m$ where $1 \le i\_1 < i\_2 < \cdots < i\_m \le n$, $a\_{i\_1}, a\_{i\_2}, \cdots, a\_{i\_m}$ is a regular brackets sequence.

For an example, given the initial sequence `([([]])]`, the longest regular brackets subsequence is `[([])]`.

## 입력

#### 입력

The input test file will contain multiple test cases. Each input test case consists of a single line containing only the characters (, ), [, and ]; each input test will have length between 1 and 100, inclusive. The end-of-file is marked by a line containing the word “end” and should not be processed.

## 출력

#### 출력

For each input case, the program should print the length of the longest possible regular brackets subsequence on a single line.

## 노트

#### 노트
