# Bacteria

문제 ID: `BACTERIA`

## 문제

#### 문제

A famous biologist, **K. C. Morris**, studies on the behavior of bacteria. So he wants to simulate a large swarm of bacteria.

There is a square dish, divided into W columns and H rows - it means there are W x H cells in the dish. Each cell is in one of two possible states, **live** or **dead**. **Live** means there are bacteria in the cell, and **dead** means there aren't. Each cell has at most four horizontally or vertically adjacent cells. Because bacteria are affected by their neighbors, the state of each live cell can be changed:

* If a live cell has fewer than two live neighbors, it is changed to the dead state after a second because of under-population.
* If a live cell has more than two live neighbors, it is changed to the dead state after a second because of overcrowding.
* If a live cell has exactly two live neighbors, it remains in the live state.

With the above rule, the swarm of bacteria can be extinct. For example, Figure 1 shows the 3 × 4 dish with 7 live cells, and Figure 2 shows the number of live neighbors for each live cell. Only 3 live cells will remain in the live state after one second, and the swarm will be extinct after two seconds. But the swarm in Figure 4 will not be extinct, because all live cells have exactly two live neighbors.

```
    **.           12.           .*.           ***
    .**           .32           ..*           *.*
    .**           .32           ..*           *.*
    .*.           .1.           ...           ***

[Figure 1]    [Figure 2]    [Figure 3]    [Figure 4]
```

Given initial states of cells, write a program that figure out when the swarm will be extinct.

## 입력

#### 입력

The first line of the input contains one integer T, the number of test cases.

Each test case begins with a line containing two positive integers W and H, the width and height of the dish (W, H <= 500). Then H lines follow, each containing W characters. The characters describe the initial states of the cells; '.' denotes a dead cell, and '\*' denotes a live cell.

## 출력

#### 출력

For each test case, print the time in seconds that the warm will be extinct in one line. If the swarm will not be extinct forever, print -1 instead.

## 노트

#### 노트
