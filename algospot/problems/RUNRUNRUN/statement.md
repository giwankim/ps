# Run Run Run

문제 ID: `RUNRUNRUN`

## 문제

#### 문제

In the world of Altertania, there is a large chessboard with N^2 cells. The board has N rows and N columns; rows are numbered from 1 to N starting from the top, and columns are numbered from 1 to N starting from the left. Each cell (r, c) are located by its row number r and column number c.

With this ample real estate, chess pieces do not fight anymore and live peacefully together.

A knight wants to visit his friend rook for his birthday. He still has his horse with him, so he can jump around like he used to. See the following diagram for the jumps he can make.

![judge-attachments/e5b339b9bfa91ee9996f172f8e713803/knight.png](assets/67fae7f461114591-knight.png)

However, he has a problem: his horse is old and lazy, so it will refuse to make more than a single move each day. Alas! At this rate, he cannot make it before rook’s birthday.

Don’t despair, all is not lost. Some cells in this chessboard are converted to vegetable patches. At the start of each day, the knight steps into a cell by a single move. If that cell has vegetable patch, he can feed his horse some carrots and the horse will agree to make a few more moves that day. The number of additional moves the horse can make is at most the nutrition value of the carrot, which is an integer between 1 and 5 (inclusive). The horse will not eat carrots twice in a single day.

Write a program to calculate the minimal days the knight can reach his friend.

## 입력

#### 입력

The input consists of T test cases. The number of test cases T is given in the first line of the input.

The first line of each test case contains two integers N (4 <= N <= 1,000) and V (0 <= V <= 10,000). The second line contains four integers R\_K, C\_K, R\_R, C\_R (1 <= R\_K, C\_K, R\_R, C\_R <= N). The knight is currently at (R\_K, C\_K), and the rook lives at (R\_R, C\_R). V lines will follow: each line will contain three integers R\_i, C\_i and V\_i (1 <= R\_i, C\_i <= N, 1 <= V\_i <= 5). V\_i is the nutrition value of carrots from vegetable patch at (R\_i, C\_i).

There will be at most one vegetable patch in a single cell. The knight’s starting position will not be a vegetable patch nor the rook’s position.

## 출력

#### 출력

Print exactly one line for each test case. The line should contain an integer indicating the number of days the knight needs to reach his friend.

## 노트

#### 노트
