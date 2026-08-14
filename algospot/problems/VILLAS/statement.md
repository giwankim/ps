# Resort Villas

문제 ID: `VILLAS`

## 문제

#### 문제

A billionaire of the Andromeda Galaxy decided to build some new resort villas to spend his winter vacation. There are N candidate planets, and he wants to choose K (1 <= K <= N) of them for the villas. To travel easily between the villas, he wants to choose the planets so the longest traveling distance between two villas is minimized. When traveling, he will be using some space roads with his space shuttle. Space roads connect planets and/or space stations bidirectionally; there are M space stations in the Andromeda Galaxy, and he can pass them by while traveling.

Write a program which helps him choosing the planets.

## 입력

#### 입력

The first line of the input will contain the number of test cases C (1 <= C <= 100). Each test case starts with a line containing four integers N, M, K, S (1 <= N <= 16,1 <= M <= 200,1 <= S <= 20,000). In the following S lines in the form `A B C', denoting there is a space road between A and B with a length of C (1 <= C <= 1,000,000). A and B are the names of the locations connected by the road; the first N capital characters of the alphabet are used to denote the planets, and the first M natural numbers will be used to denote the space stations. You may assume that it is possible to travel between every pair of planets through some space roads.

## 출력

#### 출력

For each test case, print out a single line denoting the minimum longest distance.

## 노트

#### 노트
