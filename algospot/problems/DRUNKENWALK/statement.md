# Drunken Walk

문제 ID: `DRUNKENWALK`

## 문제

#### 문제

After having a bit too much to drink in the evening, you ﬁnd yourself going on a long walk on a directed graph—but not too long, as there are no cycles. You start at vertex 0, and whenever you are at a vertex, you will randomly leave the vertex along one of its outgoing edges with probability proportional to the weight of the edge.

You continue walking until you reach a vertex that has no edges leaving it, after which you fall asleep. The length of your walk is the number of edges you traverse. Moreover, before leaving vertex 0, you may choose one edge from anywhere in the graph that you do not like and ignore it during your walk (or you may choose to not ignore any of them). Determine the longest possible expected length of your walk.

## 입력

#### 입력

The input consists of multiple test cases. Each test case begins with a line containing two integers N, 2 ≤ N ≤ 10,000, and M, 1 ≤ M ≤ 100,000, the number of vertices and edges in the graph, respectively. The next M lines each contain three integers u, v, and w (1 ≤ w ≤ 1,000), indicating that there is a directed edge from vertex u to vertex v (numbered from 0 to N − 1) with weight w. The input terminates with a line with N = M = 0.

## 출력

#### 출력

For each test case, print out a single line that contains the longest possible expected length of your walk. Your answer will be considered correct if it is within 10^(−8) absolute or relative precision. In the ﬁrst sample case, ignoring the edge from vertex 0 to vertex 3 gives the maximum possible expected length of 2. (Without ignoring it, the expected length is 1.5.)

## 노트

#### 노트
