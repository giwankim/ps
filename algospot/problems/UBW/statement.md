# Unlimited Blade Works

문제 ID: `UBW`

## 문제

#### 문제

What do you think of magic? Magic used to be a secretive and mysterious art, only known by a select few during the ancient era. However, modern-day magic is very different: it is very scientific, reproducible, and has a textual form. Maybe you have seen it, or have used it already. Here's an example:

```
def main
 loop 5
  loop 3
   Unlimited
  Blade
 Works
```

Modern day magic is defined by a **magic code**, which is composed of one or more **module** definitions. For example, the above magic code defines a "main" module. A module contains a series of **operations**, which will be executed in order when the module is **invoked**. Each operation is either a **loop** or a **module invocation**.

The operations that are contained by a module could be identified by a single space character (that is, indented by a single space) before the operation. Therefore, "main" module above consists of two operations; `loop 5` and `Works`.

What do these operations do?

* An operation in the form of `loop N` contains 0 or more operations, and executes them $N$ times. You can identify the contained operations by looking for one more space in front of those operations; for example, `loop 5` operation above contains two operations `loop 3` and `Blade`. `loop 3` operation contains only a single operation, `Unlimited`.
* All other operations are in the form of `ModuleName`, and they simply invoke the given module. After the invoked module terminates (all operations in the invoked module is executed), we will resume executing this module at the next operation.

For example, `loop 3` operation above will invoke the module `Unlimited` 3 times, and then we will proceed to the next operation `Blade`.

(For a concrete description on magic code, look below.)

Your friend in WinterTree city is an unpredictable guy. He makes blades for a living. Today he wrote a new magic code, which can be performed by invoking the "main" module. The magic will eventually call the module "Blade" 0 or more times - every time the "Blade" module is called, it will create a blade. However, he is not sure how many blades this magic is going to make. You, as one of his wizard friends, should help him to calculate how many blades will be created after unlimited time is passed.

### Magic code specification

* A **magic code** is a text file, consisting of multiple lines.
  + Each line can be "indented" by prepending one or more space characters.
  + A magic code consists of one or more module definitions.
* A **module definition** begins with a line of the form "def (modulename)".
  + Zero or more consecutive lines follow, each containing a single operation indented by one or more spaces. Out of those, all operations that are indented by a single space that come before the next module definition or the end of the magic code are contained by this module.
* An **operation** is either:
  + A **loop** operation in the form of `loop N`.
    - N is a positive integer, less than 10.
    - Zero or more lines follow, each containing a single operation indented **more** than the loop operation. Out of those, all operations that are indented by exactly **one more space than the loop operation** are contained by this loop.
  + A module invocation in the form of `ModuleName`.

## 입력

#### 입력

The first line of the input contains the number of test cases T.

Each test case starts with N ($1 \le N \le 80$), the number of lines in the magic code. After that, N lines of magic code will be given.

You can assume the following:

* All lines are 120 characters or shorter.
* All module names consist of 1 to 15 alphanumeric characters.
* There will always be a "main" module.
* No two modules will have the same name.
* The code will not define modules named "Blade", "def" or "loop".
* The code will not invoke any module that is not defined within the code (except "Blade", of course).

## 출력

#### 출력

For each test case, print the number of blades manufactured when the "main" module is invoked, after infinite time passes.

If this number is greater than 100,000, print -1 instead.

## 노트

#### 노트

Description 검수: [JongMan](/user/username/JongMan "JongMan")
