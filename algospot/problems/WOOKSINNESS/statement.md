# Wooksin-ness of A Graph

문제 ID: `WOOKSINNESS`

## 문제

#### 문제

At the headquarters of algospot.com, Toivoa “the chairman” has been pestering Astein “the slave” for a new graph problem for so long. So Astein came up with the following problem.

Wooksin-ness(욱신함) of an undirected graph G(V;E) is defined as the minimum number of additional edges in order to have a cycle in the graph. Stated formally, you can write it as:

min|E' - E| s.t. E' is superset of E and G'(V, E') has at least one cycle

However, loops (edges that start and end at the same vertex) and multiple edges between a single pair of vertices are not allowed.

Write a program that calculates Wooksin-ness of given graph.

## 입력

#### 입력

The input consists of T test cases. The number of test cases T is given in the first line of the input.

The first line of each test case contains two integers V and E (1 <= V <= 100, 0 <= E <= 1,000), where V represents the number of vertices in the graph, and E represents the number of edges. The vertices are numbered from 0 to V - 1. The following E lines will each contain two integers, which are the number  
of two vertices connected by an edge.

There will be no loops in the input data. There will be at most one edge between a pair of vertices.

## 출력

#### 출력

Print exactly one line for each test case. The line should contain an integer indicating the minimum number of additional edges we need to add to the graph to get a cycle. If this is impossible, print -1 instead.

## 노트

#### 노트
