# 20. Valid Parentheses

[**LeetCode Problem**](https://leetcode.com/problems/valid-parentheses/description/?envType=daily-question&envId=2026-10-01)

**Difficulty:** Easy
**Language:** Java
**Topics:** String, Stack

## Problem

Given a string `s` containing only:

```text
( ) [ ] { }
```

determine whether the string contains valid parentheses.

A string is valid when:

1. Every opening bracket is closed by the same type of bracket.
2. Brackets are closed in the correct order.
3. Every closing bracket has a corresponding opening bracket.

### Example 1

```text
Input: s = "()"
Output: true
```

### Example 2

```text
Input: s = "()[]{}"
Output: true
```

### Example 3

```text
Input: s = "(]"
Output: false
```

### Example 4

```text
Input: s = "([])"
Output: true
```

### Example 5

```text
Input: s = "([)]"
Output: false
```

## Approach

The problem follows a **Last-In, First-Out (LIFO)** pattern, making a stack the natural data structure.

When an opening bracket is encountered, push it onto the stack:

```text
( → push
[ → push
{ → push
```

When a closing bracket is encountered, it must match the most recently opened bracket.

For example:

```text
Input: ([{}])

Stack process:

(       → (
[       → ( [
{       → ( [ {
}       → ( [
]       → (
)       → empty
```

If a closing bracket doesn't match the stack's top element, the string is invalid.

## Optimizations

### 1. Reject odd-length strings immediately

Every valid bracket requires a matching closing bracket, so a valid string must have an even number of characters.

```java
if ((n & 1) != 0) {
    return false;
}
```

Using:

```java
n & 1
```

avoids the modulo operation while checking whether the length is odd.

### 2. Use a primitive `char[]` as the stack

Instead of using:

```java
Stack<Character>
```

or:

```java
Deque<Character>
```

the solution uses:

```java
char[] stack = new char[n];
```

This avoids:

* `Character` boxing
* collection overhead
* additional node/object allocations

A simple integer tracks the stack's top:

```java
int top = 0;
```

### 3. Process the string once

Each character is inspected exactly once.

When opening:

```java
stack[top++] = c;
```

When closing:

```java
char open = stack[--top];
```

This gives constant-time push and pop operations.

### 4. Early termination

The algorithm immediately returns `false` when:

* A closing bracket appears with an empty stack.
* A closing bracket does not match the most recent opening bracket.

This prevents unnecessary processing after the result is already known.

## Algorithm

```text
1. If the string length is odd, return false.
2. Create a char[] stack.
3. For each character:
   - If it is an opening bracket, push it.
   - Otherwise:
       - If the stack is empty, return false.
       - Pop the most recent opening bracket.
       - Check whether it matches the closing bracket.
4. Return true only if the stack is empty.
```

## Complexity

Let `n` be the length of the string.

### Time Complexity

```text
O(n)
```

Every character is processed exactly once.

### Space Complexity

```text
O(n)
```

In the worst case, the string consists entirely of opening brackets:

```text
(((((((
```

The entire string may need to be stored in the stack.

The implementation uses **O(1) additional state besides the stack**, with the stack itself requiring `O(n)` space.

## Java Solution

```java
class Solution {
    public boolean isValid(String s) {
        int n = s.length();

        // Odd-length strings can never be valid.
        if ((n & 1) != 0) {
            return false;
        }

        char[] stack = new char[n];
        int top = 0;

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);

            if (c == '(' || c == '[' || c == '{') {
                stack[top++] = c;
            } else {
                if (top == 0) {
                    return false;
                }

                char open = stack[--top];

                if ((c == ')' && open != '(') ||
                    (c == ']' && open != '[') ||
                    (c == '}' && open != '{')) {
                    return false;
                }
            }
        }

        return top == 0;
    }
}
```

## Key Takeaway

**Valid Parentheses is a classic stack problem.**

The optimized implementation combines:

* LIFO stack behavior
* Primitive `char[]` storage
* Bitwise odd-length checking
* Early termination
* Single-pass processing

Result:

```text
Time:  O(n)
Space: O(n)
```

with minimal runtime and object-allocation overhead.
