# Election

문제 ID: `ELECT`

## 문제

#### 문제

Given the voting preferences of a population of $M$ people, you are to determine the winner of an election among $N$ candidates, numbered $1, \cdots ,N$. For this problem, the $M$ people are partitioned into $G$ “groups”where all members within a group have the same voting preferences. The candidate preferences for a group are specified by listing candidates from most preferred to least preferred. Election results are determined by an instant-runoff voting procedure.  
In this method, the first choices of the M people in the population are counted and the least popular candidate is eliminated. In the event of a tie, the highest-numbered candidate is eliminated. Then, the eliminated candidate is removed from the preference list of all $M$ individuals in the population, and again the least popular candidate is eliminated. This process repeats until only a single candidate is left.

## 입력

#### 입력

The input test file will contain multiple test cases. Each input test case begins with a single line containing the integers $G$ and $N$ where $2 \le N \le 5$ and $1 \le G \le 20$. The next $G$ lines are of the format “$M\_i\;a\_{i\_1}\;a\_{i\_2}\; \cdots a\_{i\_N}$” where $1 \le Mi \le 20$ and $a\_{i\_1}, \cdots , a\_{i\_N}$ is a permutation of the integers $1, \cdots ,N$. $M\_i$ is the number of individuals in the $i$th group, and $a\_{i\_1}, \cdots , a\_{i\_N}$ is the ordering of the $N$ candidates from most preferred to  
least preferred for the $i$th group. The end-of-file is marked by a test case with $G = N = 0$ and should not be processed.

## 출력

#### 출력

For each input case, the program should print the winner of the election on a single line.

## 노트

#### 노트
