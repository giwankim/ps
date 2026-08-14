# Master of Baduk

문제 ID: `MASTEROFBADUK`

## 문제

#### 문제

Baduk is an ancient board game which requires deep strategy and tactics, where two players (Black and White) exchange their turns to place a stone in one of empty positions of the board. Black play with black stones, and White with white stones. You can also ‘pass’ to give up your opportunity to place a stone.

Baduk is known to be notoriously hard to solve with computers, but don’t worry! We will be dealing with a simplified and highly abstracted version of the game. See below for the rules of the game.

In this version of Baduk, each player has one stone group. Each empty position which is adjacent to a stone is called a liberty. We can classify each liberty as one of the following three types:

* Shared liberties ($S$) - empty positions adjacent to both colors of stones.
* Inner liberties ($I$) - empty positions completely enclosed within a stone group.
* Outer liberties ($O$) - empty positions that is adjacent to a stone, which are not either Shared or Inner liberties.

Using these definitions, we can summarize the entire state of the game board into 5 non-negative integers: $BO$ (# of Outer liberties of Black), $BI$ (# of Inner liberties of Black), $WO$ (# of Outer liberties of White), $WI$ (# of Inner liberties of White), $S$ (# of liberties shared by White and Black).

At each turn, a player can choose one of the numbers and decrease it by 1. There are a few rules:

1. No move can result in a negative number of liberties. (i.e. $BO$, $BI$, $WO$, $WI$, $S$ should all be $0$ or greater after a move)
2. To decrease $BI$ from $1$ to $0$, both $BO$ and $S$ must be $0$ already.
3. To decrease $WI$ from $1$ to $0$, both $WO$ and $S$ must be $0$ already.
4. A player can pass his/her turn.

The winner of the game is determined as follows.

* A player loses when it does not have any liberties. For example, when $BO = BI = S = 0$, White wins.
* When all five numbers become $0$ by decreasing $S$ the last time, the last person who made the move wins.

Write a program that, given the 5 numbers, determine who will win assuming Black plays first, and both players play optimally. Note that there are cases where no player can win the game.

## 입력

#### 입력

The first line of the input will contain the number of test cases $N$. ($1 \le N \le 10\,000$) $N$ lines follow, with 5 non-negative integers, each representing $BO$, $BI$, $S$, $WO$ and $WI$, in this order. ($0 \le BO$, $WO$, $S \le 10^9$, $0 \le BI$, $WI \le 2$, $BO + BI + S \ge 1$, $WO + WI + S \ge 1$)

## 출력

#### 출력

For each test case, print a single line containing the winner of the game ("White" or "Black"). In case no one can win the game, print "Neither".

## 노트

#### 노트
