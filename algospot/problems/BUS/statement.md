# Bus Assignment

문제 ID: `BUS`

## 문제

#### 문제

The 2024 UCPC summer contest is finally over! The day after the contest, participants will join an excursion to Kyungbok palace to blow off the steam of the contest. Everyone is looking forward to it, and the organizing committee arranged everything for the trip.

However one small problem remains; the transportation. The committee plans to rent two buses to take participants to the palace and back - a Tayo bus, and a Tobot bus. And each participant has a different preference on which bus to ride. If the $i$th participant takes the Tayo bus, (s)he will get a satisfaction value of $A[i]$. Taking the Tobot bus instead will give him(her) a satisfaction value of $B[i]$. (Each participant can take only one bus.) The total satisfaction between all participants is calculated by the sum of individual satisfaction values.

Okay, this sounds like an easy problem - everyone can take the bus they prefer(each bus is big enough for all participants)! However, the problem is complicated by the fact people's satisfaction *decreases* when two friends take different buses. If $i$th participant and $j$th participant take different buses, the total satisfaction will decrease by $H[i,j]$! However, if either $i$ or $j$ doesn't go to the excursion at all, there will be no satisfaction decreases.

The organizing committee wants to find a plan that maximizes the overall satisfaction. To do this, they can assign each participant to one of the buses, or exclude him(her) from the excursion at all.

Write a program that calculates the maximum satisfaction.

## 입력

#### 입력

The input consists of multiple test cases. The first line of the input will contain the number of test cases $T$.

Each test case begins with a line containing $N$ ($2 \le N \le 200$), the number of participants. Each of the next $N$ lines will contain two integers, $A[i]$ and $B[i]$. The next $N$ lines will give you the friendship information between the participants. Each line will contain $N$ integers, and the $j$th number of $i$th line will be $H[i,j]$.

You can assume the following:

* All preferences ($A[i]$, $B[i]$, $H[i,j]$) are nonnegative integers less than or equal to 1000.
* $H[i,j] = H[j,i]$ for all $i$ and $j$.
* $H[i,j] = 0$ when $i=j$.

## 출력

#### 출력

For each test case, print a single line with the maximum satisfaction achievable.

## 노트

#### 노트

The optimal solution for the second test case:

* Participant 1 doesn't go to the excursion.
* Participant 2 takes the Tayo bus.
* Participant 3 takes the Tobot bus.

The individual satisfaction from participant 2 and 3 are 5 and 7, respectively. Also, since the two participants are taking different buses, the total satisfaction is decreased by $H[2,3] = 1$.

Description 작성: [JongMan](/user/username/JongMan "JongMan")
