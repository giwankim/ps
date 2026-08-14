# Binary Search Tree

문제 ID: `BST`

## 문제

#### 문제

In computer science, a **binary search tree** (BST) is a binary tree data structure which has the following properties:

* Each node in the tree has a unique numeric key.
* The left subtree of a node X only contains nodes with keys less than X's key.
* The right subtree of a node X only contains nodes with keys greater than X's key.
* Both the left and right subtrees must also be binary search trees.

This problem is very simple. Given a binary tree, figure out whether it is a binary search tree or not.

## 입력

#### 입력

The first line of the input contains one integer T, the number of test cases.

Each test case describes a binary tree with N (1 <= N <= 100) nodes, indexed by integers 1 to N. The first line of each test case contains N.  
Then N line follow, each containing three integers L\_i, R\_i, K\_i (1 <= i <= N).

* L\_i denotes the index of the left child of node i. If the node doesn't have a left child, L\_i = 0.
* R\_i denotes the index of the right child of node i. If the node doesn't have a right child, R\_i = 0.
* K\_i denotes the key of the node i. 0 <= K\_i <= 1000.

You can safely assume that the input forms a valid binary tree.

## 출력

#### 출력

For each test case, print "YES" in one line if the given binary tree is a binary search tree. If not, print "NO" instead.

## 노트

#### 노트
