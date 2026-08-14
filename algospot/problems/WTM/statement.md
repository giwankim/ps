# What-the-Mole

문제 ID: `WTM`

## 문제

#### 문제

Wonha works for a game development company nowadays. He had to learn several technologies, but he was tired of reading technical documents. So he decided to write and demonstrate a simple game. The name is chosen as *"What-the-Mole"* (due to copyright issues).

What-the-Mole contains a game panel that simply consists of nine holes that are aligned as a $3 \times 3$ grid. $N$ moles appear on the holes randomly in position and time, and the objective of the game is to hit these moles as many times as possible.

A player uses a cursor to hit the moles. At the start of the game, cursor is on the top-left hole. There are two actions that the player may do. One action is to move the cursor to its adjacent holes (two holes are adjacent if they share an edge on the grid), and it takes 0.9 seconds. Note that the player cannot do anything else with the cursor while moving.

Another action is to swing the hammer on the current hole where the cursor is on. It's a little bit complicated as some animations and effects should be played. If a player tries to hit the mole, animations are played for 0.1 seconds. After that, the judgment of whether the hitting is successful or not takes place; if there is a mole, then the hitting is judged as successful, otherwise not. Successful hitting scores one point, and the hit mole will disappear immediately. If a mole appears exactly at the same time of the judgment, it will be judged as a successful hitting.

The moles will appear after a non-negative integer number of seconds after the start of the game, and will disappear 2.75 seconds after the appearance. Let us denote the duration of the appearance of the $i$-th mole as $T\_i$, and the coordinates of the hole at which the $i$-th mole appears as $(R\_i, C\_i)$. The mole will appear on the hole located at $R\_i$-th row and $C\_i$-th column, at time $T\_i$.

Wonha was worried about the case where two or more moles appear and occupy the same hole -- notice that even if two moles appear at different times, they can still occupy the same hole if their appearance time intervals overlap. Thus, he made some tricks in his program to avoid such cases -- even if a player does not hit any moles, it is guaranteed that, at any point in time, no two moles will occupy the same hole at the same time. The game will end when the last mole disappears.

Wonha tested the game several times, and he found that his pseudo-random number generator is broken. If he changes the system time to specific moment and clicks the menu button several times, the pattern of the game will always be the same, just like Pokemon's RNG trick. Now he is curious about the maximum score for this specific pattern of the game. Help him to find out maximum score, given how the moles will appear and disappear.

## 입력

#### 입력

Your program is to read from standard input. The input consists of $T$ test cases.  
The number of test cases $T$ is given in the first line of the input.  
The first line of each test case contains an integer, $N$.  
Each line of the next N lines contains three integers $R\_i, C\_i \; (1 \le R\_i, C\_i \le 3)$, and $T\_i \;(0 \le T\_i \le 100)$, separated by space(s). Each line describes when and where a mole appears.

## 출력

#### 출력

Your program is to write to standard output. Print exactly one line for each test case.  
The line should contain a single integer: the maximum score for a given game pattern.

## 노트

#### 노트
