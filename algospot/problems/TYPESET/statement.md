# Typesetting Deadline

문제 ID: `TYPESET`

## 문제

#### 문제

The administrators of *algospot.com*. are busy preparing yet another programming contest. They have selected $N$ problems from the problem pool that will appear in the next contest.

Unfortunately, at this time, only one of the administrators, the **L**ord of the **T**remendously **D**azzling and **T**errific **L**and, is able to typeset the problems. Because typesetting requires a lot of concentration, the administrator may process one problem at a time. Yet she may freely choose the order of the problems to typeset. Each problem requires a different amount of time of typesetting. Let us denote the required typesetting time for the $i$-th problem as $T\_i.$

The director of the contest is going to set a *deadline* for typesetting. As a result, the typesetter, **LTDTL** will feel some sort of *uneasiness*. Let us consider the following model for measuring uneasiness, given the non-negative deadline $D$:

$$\sum\_{i \in [1, N]} {A\cdot D + B \cdot \mathrm{Tardiness}\_i + C \cdot \mathrm{Earliness}\_i}$$

where $A, B$ and $C$ are non-negative integer constants.

Before defining *tardiness* and *earliness*, let us take a close look at the first term. $A \cdot D$ represents the psychological pressure due to the late deadline. Although it appears to the typesetter that she has more time to typeset the problems, it also means that the typesetter will be occupied by all the hectic work for a longer period of time, and thus she will be feeling uneasy for a long time. Hence, the further the deadline is from now, the more uneasy the typesetter will feel.

$\mathrm{Tardiness}\_i$ means the 'lateness' of the $i$-th problem. $\mathrm{Tardiness}\_i$ is zero if the typesetting for the $i$-th problem will be done no later than the deadline. Otherwise, it will be defined as the difference between the finish time of the $i$-th problem and the deadline.

Analogously, $\mathrm{Earliness}\_i$ is zero if the typesetting of the $i$-th problem is done later than the deadline. Otherwise, it will be defined as the difference between the finish time of the $i$-th problem and the deadline.

For example, if we assume $D = 5$ and the finish time of the problem is $7$, then the uneasiness value for that problem will be $A\cdot 5 + B \cdot 2 + C \cdot 0$.

The minimum amount of the uneasiness will be dependent on the deadline and the order of typesetting problems. Write a program that finds the minimum amount of uneasiness.

## 입력

#### 입력

Your program is to read from standard input. The input consists of $T$ test cases.  
The number of test cases $T$ is given in the first line of the input.  
The first line of each test case contains four integers $N\;(1 \le N \le 1,000)$, $A, B$ and $C\;(0 \le A, B, C \le 100)$.  
The next line will contain $N$ integers, $T\_i\;(1 \le T\_i \le 1,000)$, separated by spaces.

## 출력

#### 출력

Your program is to write to standard output. Print exactly one line for each test case.  
The line should contain the minimum uneasiness.

## 노트

#### 노트
