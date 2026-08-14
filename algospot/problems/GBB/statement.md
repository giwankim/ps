# Limited Gawi-Bawi-Bo

문제 ID: `GBB`

## 문제

#### 문제

After gambling with his all money, finally Kaiji faced bankruptcy. His main creditor Endo Yuji, suggested another type of gambling, card game named Limited Gawi-Bawi-Bo.

In this game, two players are given cards where one of Gawi, Bawi, or Bo is drawn. The game consists of one or more rounds. In each round, each player shows one card from his cards. Gawi cards win against Bo cards, Bo cards win against Bawi cards, and Bawi cards win against Gawi cards. If one player wins, another player’s card will be discarded. If they show same type of cards, all two cards will be discarded. After this, next round starts. The game will end when one player loses all his cards. The player that loses all his cards loses, and the remaining player will be the winner. If two players lose all their cards simultaneously, a special match will be played. In this match, two players will play traditional Gawi-Bawi-Bo game, until winner is decided. So if a special match is played, each player’s winning probability is exactly 0.5.

Kaiji will bet all his remaining money, and he’ll lose all money if he lost. But if he wins, he’ll get rid of all his dept. So he’ll try to find a strategy to maximize his likelihood of winning in the game. His opponent, Kouji, is also as desperate as Kaiji, facing bankruptcy himself. But he lacks tactics, and he’ll show random card in each round.  
Kaiji knows how many cards he and the opponent have of each kind at the start of the game. Given the starting condition, calculate the probability of Kaiji winning the game.

## 입력

#### 입력

Your program is to read from standard input. The input consists of T ( 100) test cases. The number of test cases T is given in the first line of the input. For each test case, 6 non-negative integers will be given, representing starting condition of Kaiji and his opponent Kouji. Number of Kaiji’s Gawi, Bawi, Bo cards, and number of Kouji’s Gawi, Bawi, Bo cards, will be given respectively. Number of each player’s total cards will be between 1 and 20, inclusive.

## 출력

#### 출력

Your program is to write to standard output. Print exactly one number for each test case, representing the maximal probability of Kaiji winning the game. Your result should be accurate to within a relative or absolute value of 10^-5.

## 노트

#### 노트
