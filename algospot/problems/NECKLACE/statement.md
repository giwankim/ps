# Colorful Necklace

문제 ID: `NECKLACE`

## 문제

#### 문제

You have a bunch of beads, and each bead is coated with one of C colors. You would like to connect them into necklaces. However, you want to make sure each of such necklaces will have no two beads of the same color (in other words, all beads in a single necklace should have distinct colors), because you think that is less boring. On top of it, each necklace should use exactly N beads.

Your plan is to make as many necklaces as possible. Write a program that calculates the maximum number of necklaces you can make, given the number of beads you have for each of the C colors.

## 입력

#### 입력

The first line of input contains T, the number of test cases.  
Each test case starts with a line that contains N (1 <= N <= 10,000) and C (1 <= C <= 10,000). N is the number of beads needed for each necklace and C is the number of colors of the beads.   
The next line contains C numbers, denoted by b\_1, b\_2, ..., b\_C where b\_i is the number of beads you have of the color i (1 <= b\_i <= 10^9).

For each test case, the total number of beads you have is always a multiple of N and is no greater than 10^9.

## 출력

#### 출력

For each test case, your program must output the maximum number of necklaces you can make.

## 노트

#### 노트
