# 1190. Reverse Substrings Between Each Pair of Parentheses

## Link
https://leetcode.com/problems/reverse-substrings-between-each-pair-of-parentheses/description/?envType=daily-question&envId=2026-09-29

## Problem

Given a string `s` consisting of lowercase English letters and parentheses, reverse the strings inside each pair of matching parentheses, starting from the innermost pair.

Return the resulting string without any parentheses.

### Example 1

**Input:**

```text
s = "(abcd)"
```

**Output:**

```text
"dcba"
```

### Example 2

**Input:**

```text
s = "(u(love)i)"
```

**Output:**

```text
"iloveu"
```

**Explanation:**

First, reverse `"love"`:

```text
love → evol
```

Then reverse the entire substring:

```text
uevoli → iloveu
```

### Example 3

**Input:**

```text
s = "(ed(et(oc))el)"
```

**Output:**

```text
"leetcode"
```

---

## Approach

This solution avoids repeatedly creating and reversing substrings.

Instead, it uses:

1. A stack to find every pair of matching parentheses.
2. An array `pair[]` to store the matching index of each parenthesis.
3. A directional traversal of the original string.
4. Whenever a parenthesis is encountered, jump to its matching parenthesis and reverse the traversal direction.

This effectively simulates all required reversals without physically reversing the substrings.

### Key Idea

For example:

```text
(u(love)i)
```

The matching-parentheses array lets us jump between:

```text
(       )
 \     /
  (love)
```

When a parenthesis is encountered:

```java
i = pair[i];
direction = -direction;
```

Changing the direction means the characters inside the parentheses are traversed backwards.

Therefore, instead of performing multiple string reversals, each character is processed during a single traversal.


## Algorithm

### 1. Find matching parentheses

Use an integer stack.

When encountering:

```text
(
```

push its index.

When encountering:

```text
)
```

pop the corresponding opening parenthesis and store both indices:

```java
pair[open] = close;
pair[close] = open;
```

### 2. Traverse the string

Start at index `0` with direction `+1`.

For every character:

* If it is a letter, append it to the result.
* If it is a parenthesis:

  * Jump to its matching parenthesis.
  * Reverse the traversal direction.

```java
i = pair[i];
direction = -direction;
```

### 3. Return the result

All parentheses are skipped, leaving only the resulting characters.

## Java Solution

```java
class Solution {
    public String reverseParentheses(String s) {
        int n = s.length();
        char[] chars = s.toCharArray();
        int[] pair = new int[n];

        int[] stack = new int[n];
        int top = -1;

        // Find matching parentheses.
        for (int i = 0; i < n; i++) {
            if (chars[i] == '(') {
                stack[++top] = i;
            } else if (chars[i] == ')') {
                int open = stack[top--];

                pair[open] = i;
                pair[i] = open;
            }
        }

        StringBuilder result = new StringBuilder(n);

        int i = 0;
        int direction = 1;

        while (i >= 0 && i < n) {
            if (chars[i] == '(' || chars[i] == ')') {
                i = pair[i];
                direction = -direction;
            } else {
                result.append(chars[i]);
            }

            i += direction;
        }

        return result.toString();
    }
}
```

## Complexity

Let `n` be the length of the string.

* **Time:** `O(n)`
* **Space:** `O(n)`

The string is traversed in linear time, while the matching-parentheses and stack arrays require linear additional space.


## Key Takeaway

The important optimization is to avoid physically reversing substrings.

Instead of:

```text
find substring
→ reverse substring
→ append substring
→ repeat
```

the solution uses:

```text
find matching parentheses
→ jump between matching pairs
→ change traversal direction
→ append characters once
```

This reduces the worst-case time complexity from **O(n²)** to **O(n)**.
