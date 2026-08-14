# Triangle Intersection

문제 ID: `TRIANGLE`

## 문제

#### 문제

There are two triangles in 3D space. Write a program that determines the shape of the intersection of the two triangles. The answer should be one of the followings: no intersection (empty), point, segment, or others.

## 입력

#### 입력

The first line of the input will contain the number of test cases C. Each test case consists of two lines, each containing the coordinates of a triangle in the following order: (x1, y1, z1), (x2, y2, z2), (x3, y3, z3) All coordinates are integers with absolute value equal to or less than 1000.

## 출력

#### 출력

Print one line for each test case. If the two triangles are disjoint, print `EMPTY`. If two triangles intersect at a point, print `POINT`. If the intersection is a line segment, print `SEGMENT`. Otherwise, print `OTHERS`.

## 노트

#### 노트
