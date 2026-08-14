# Circle Routes

문제 ID: `CIRCLEROUTES`

## 문제

#### 문제

The city of Algospot has lots of circle bus routes. A circle bus route means the bus’s route ends at where it started. Donghyun runs a bus company, which will be servicing a new circle bus route. He wants to find out the minimum number of buses he needs to meet the bus schedule. The schedule works like this:

1. Every day, the first bus departs from the terminal at time $S$.
2. After that, a new bus will depart from the terminal periodically, with a fixed interval $D$. For example, a new bus will depart at time $S$, $S+D$, $S+2D$, and so on.
3. The last bus of the day departs the terminal at $E$. (You can assume the difference between $E$ and $S$ is a multiple of $D$.)
4. Each bus takes a fixed amount of time, $C$, to traverse the route and come back to the terminal.

For example, let’s assume the following scenario: the first bus departs at 05:50, and a new bus departs every 30 minutes until the last bus departs at 20:20. How many buses do we need, if it takes 65 minutes for a bus to traverse the route?

The first bus departs at 05:50 and returns to the terminal at 06:55. The second bus departs at 06:20 and returns at 07:25. The third bus departs at 06:50, and returns at 07:55. The fourth bus must depart at 07:20 - but at this time, the first bus is already back in the terminal, so Donghyun only needs 3 buses to serve this schedule.

Write a program to find the minimum number of buses.

## 입력

#### 입력

The first line of the input gives the number of test cases $T$. ($1 \le T \le 10\,000$) $T$ lines follow, each containing a test case. The test case consists of four strings $S$, $E$, $D$ and $C$, in the form HH:MM. $S$, $E$ ($S \le E$) represent the time of the first and the last bus, respectively. $D$ and $C$ ($0 < D$, $0 < C$) each represent the time interval between buses, and the time it takes a bus to traverse the route.

In each string HH:MM, HH represents the hour part of the time (or time interval), and MM represents the minute part. Concretely, HH will be a zero-padded integer between $0$ and $23$ ($00$, $01$, $\cdots$, $23$). Likewise, MM will be a zero-padded integer between $0$ and $59$ ($00$, $01$, $\cdots$, $59$).

## 출력

#### 출력

Print a single line per each test case, containing the minimum number of buses required for the schedule.

## 노트

#### 노트
