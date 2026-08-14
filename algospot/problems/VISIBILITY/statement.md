# Visibility Measure

문제 ID: `VISIBILITY`

## 문제

#### 문제

In computer graphics, identifying visible lines and surfaces is an important task.  
When a polygon or a polyhedron is rendered by a graphical program, only visible parts(lines and surfaces) are considered and rendered.  
This might reduce the computational time and space, and most of all, it makes the rendered object to be realistic.

![judge-attachments/6ba8d6588d3d6102525e45582928851f/figure1.png](http://algospot.com/media/judge-attachments/6ba8d6588d3d6102525e45582928851f/figure1.png)

Consider a polyhedron(pentahedron) in the figure.  
When it is viewed from the front, only two surfaces are visible and rendered as in the figure in the middle.  
However, if the other surfaces are rendered as in the right-most figure,  
the result is not realistic(it is rather like bottom-back view).

When a polygon on two dimensional plane is given, the visibility measure could be defined roughly as follows.  
If there exists a point outside the polygon where the number of visible line segments of the polygon is K, we say that the polygon is K-visible.  
The **visibility measure** of the polygon is the maximum value of K.

A formal definition is as follows:  
First, let S be the set of points on the boundary of a polygon.  
For a point ![](http://eq.springnote.com/tex_image?source=q'%20\notin%20S), a point ![](http://eq.springnote.com/tex_image?source=p'%20\in%20S) is visible from q if and only if

![](http://eq.springnote.com/tex_image?source=%20%20%20%20%5Cnexists%20r%20%5Cin%20S%20%5C%3B%5C%3B%5Ctextrm%7Bsuch%20that%7D%5C%3B%5C%3B%20p%20%5Cneq%20r%20%5C%3B%5C%3B%5Cland%5C%3B%5C%3B%20p%2C%5C%3B%20q%2C%5C%3B%20r%5C%3B%20%5Ctextrm%7Bare%20co-linear%7D%20%5C%3B%5C%3B%5Cland%5C%3B%5C%3B%0A%09%7C%5Coverline%7Bqr%7D%7C%20%3C%20%7C%5Coverline%7Bqp%7D%7C%0A)

where ![](http://eq.springnote.com/tex_image?source=%7C%5Coverline%7Bqr%7D%7C)denotes the distance between q and r.

For a fixed q, we define V(q,S) as the set of line segments of polygon S which are visible from q.  
If there exists a point ![](http://eq.springnote.com/tex_image?source=q'%20%5Cnotin%20S) where the cardinality(number of elements) of V(q',S) is K, we say that S is K-visible.  
The **visibility measure** of S is the maximum value of K which satisfies that S is K-visible.

Given a convex polygon S on two dimensional plane, write a program to compute the visibility measure of S.

## 입력

#### 입력

Your program is to read from standard input. The input consists of T test cases.  
The number of test cases T is given in the first line of the input.  
The first line of each test case contains an integer, n(3 <= n <= 20,000),  
the number of vertices of S.  
Each line of the next n lines contains two integers, x and y,  
where (x, y) is the coordinates of each vertex (-10^9 <= x,y <= 10^9).  
For each test case, the order of vertices would be either clockwise or counter-clockwise, and no three points(vertices) are co-linear.

## 출력

#### 출력

Your program is to write to standard output. Print exactly one line for each test case.  
The line should contain an integer indicating the visibility measure of the given polygon.

## 노트

#### 노트
