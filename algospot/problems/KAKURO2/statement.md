# Kakuro II

문제 ID: `KAKURO2`

## 문제

#### 문제

Having written a input file generator in [Kakuro I](http://algospot.com/judge/problem/read/KAKURO1), we can now set problems to solve Kakuro puzzles.

Write a program to solve kakuro puzzles. For Kakuro rules, refer to [Kakuro I](http://algospot.com/judge/problem/read/KAKURO1).

## 입력

#### 입력

The first line of input file has the number of test cases T.

In the first line of each test case, the size of the game board N (<= 20) is given. The next N lines will give a description of the board, from top to bottom. These lines will have N numbers, where 0 denote black/hint cells, and 1 denote white cells. In the next line, the number of hint Q is given. The following Q lines give the hints on the board, each described with four integers: y, x, direction, and sum. sum is the value of the clue (1 <= sum <= 45), and (y, x) is the 1-based coordinate of the hint cell. direction is 0 if hint clue is a horizontal sum, 1 if the clue is a vertical sum.

You can assume for all test cases, there will be a unique valid solution.

## 출력

#### 출력

For each test case, print out the solved Kakuro board in N lines each with N numbers. Print 0 for black or hint cells, and print the filled number for white cells.

## 노트

#### 노트
