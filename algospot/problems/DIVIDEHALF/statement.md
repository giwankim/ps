# Divide Into Half

문제 ID: `DIVIDEHALF`

## 문제

#### 문제

Recently, abundant amount of offshore minerals has been revealed by Andromeda Express Co. near South Korea. ROK and AE argued about the rights for those minerals; finally they decided to divide the region into half.

The region found can be expressed as planar convex polygon on the sea, with N vertices defining the border of the region. The region includes M volcanic islands, which are never on the border. We plan to build research labs and harvesting facilities on those volcanic island. So the two groups agreed to choose two islands to build each one's main facilities on them, and split the region; each point in this region will belong to the nearer base.

Since the harvesting equipments can move in only four cardinal directions (parallel to x-axis or y-axis), the distance between a point and a base is not measured by the usual Euclidean distance, but by Manhattan distance. The manhattan distance for two points - (x1, y1), (x2, y2) is defined by `|x1 - x2| + |y1 - y2|`.

Write a program that chooses two islands that will minimize the difference of two split regions' areas.

## 입력

#### 입력

The first line of the input will contain the number of test cases C (C <= 50).

The first line of following test cases will contain two integers, N and M (3 <= N <= 50, 2 <= M <= 50). Next N lines will specify the coordinates of border vertices with two integers, x\_i and y\_i. The vertices will be given in either clockwise or counter-clockwise order. The border may contain three or more collinear points. Next M lines of each test case will supply the coordinates of the volcanic islands by giving two integers, x\_j and y\_j.

All coordinates are integers in the range [-1000, 1000].

## 출력

#### 출력

For each test case, print the minimal difference between the two split regions, by choosing appropriate islands. Absolute/relative errors less than 1e-7 will be tolerated.

## 노트

#### 노트
