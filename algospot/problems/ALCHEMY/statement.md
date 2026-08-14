# Alchemy

문제 ID: `ALCHEMY`

## 문제

#### 문제

Alchemists are mysterious people who try to synthesize valuable materials (such as gold) out of mundane materials (such as copper or lead). According to legends, their work has culminated in a cryptic piece of book, called the Emerald Tablet. The Emerald Tablet is said to contain the ultimate recipe --- for synthesizing gold, and the Elixir of Life!

By a lucky coincidence, you found an ancient book that you suspect to be the Emerald Tablet. This book deals with $N$ kinds of materials, each of them numbered with unique integers from 1 to $N$. Each material is either:

* Synthesizable: synthesizable materials can be made from mixing other materials (possibly synthesizable or non-synthesizable) listed in this book. We would be rich if gold was synthesizable, and we would be immortal if Elixir of Life was!
* Non-synthesizable: non-synthesizable materials cannot be made from other materials, so you'll have to harvest or buy them.

The book lists all of the N materials that are known to the world, both synthesizable and non-synthesizable. The book also provides a recipe for each of the synthesizable material. A recipe specifies a list of the ingredients need to synthesize the target material, where each ingredient is one of the $N$ materials.

To synthesize a (synthesizable) material, you need to put the ingredients of the target material into the "Mysterious Flask", **one at a time**. The Mysterious Flask will take care of mixing the ingredients. As soon as you put the last ingredient into the flask, the target material will be synthesized, and pop out of the flask magically.

This all sounds very simple, and we seem to be on our way to infinite wealth and eternal life, but there is a problem in practice; you may not be able to synthesize what you want!

For example, consider the following case: there are two synthesizable materials, X and Y. X can be made by mixing A and C in the flask, and Y can be made by mixing A, B, C and D. In order to make Y, you can try to put the 4 ingredients into the flask in the following order: A, B, C and D. However, the moment you put C in the flask, X will be synthesized in the flask, and pop up!

You would think you can avoid this problem by putting the ingredients in a different order, such as A, B, D, and then C. Well, after you put C into the flask, it contains the ingredients of Y as well as those of X --- which of the two will be synthesized in the flask, then? After numerous experiments, you have finally figured out the following rule:

* When there are two or more materials that can be synthesized (from the ingredients in the flask), the Mysterious Flask mixes and synthesizes the one that needs the largest number of ingredients.
* In case of a tie, the material with the largest number (which has the highest reactivity) will be synthesized.

With the above rule in effect, Y will be synthesized as it requires 4 ingredients while X requires 2 ingredients, in our example.

Unfortunately, the recipes in the Emerald Tablet do not tell us in what order we should put the ingredients into the flask. But we can't stop here! To gain infinite wealth and eternal life, write a program that finds an ordering of ingredients that can synthesize the desired material for each recipe.

## 입력

#### 입력

The first line of the input will contain the number of test case, $T (T \le 100)$.

Each test case starts with a line containing $N (1 \le N \le 16)$, the number of known materials. $N$ following lines will give information of each material, from $1$ to $N$. The $i+1$-th line contains an integer $c\_{i}$ which is the number of ingredients needed for synthesizing material $i$. Then $c\_{i}$ distinct integers follow, which are the number of ingredients needed (none of them equals $i$). If $c\_{i}$ is $0$, material i is non-synthesizable.

Test cases are separated by a blank line.

## 출력

#### 출력

For each test case, print $N$ lines. Each of this lines should be a reordered recipe for each of the materials, from $1$ to $N$.   
Each line should begin with $c\_{i}$, and $c\_{i}$ numbers should follow which is the order of the ingredients. If the material was listed as synthezable, but cannot be synthezed, print a line containing "`IMPOSSIBLE`" (without quotes) instead.  
If there are multiple orderings that can synthesize a material, print any of them.

Separate adjacent test cases by a blank line.

## 노트

#### 노트
