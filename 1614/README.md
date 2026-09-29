# 1614. Maximum Nesting Depth of the Parentheses

## Link
https://leetcode.com/problems/maximum-nesting-depth-of-the-parentheses/submissions/2157209722/?envType=daily-question&envId=2026-09-29

## Problem

Given a valid parentheses string `s`, return the **nesting depth** of `s`.

The nesting depth is the maximum number of nested parentheses at any point in the string.

### Example 1

**Input:**

```text
s = "(1+(2*3)+((8)/4))+1"
```

**Output:**

```text
3
```

**Explanation:**
The digit `8` is inside three nested pairs of parentheses.

### Example 2

**Input:**

```text
s = "(1)+((2))+(((3)))"
```

**Output:**

```text
3
```

### Example 3

**Input:**

```text
s = "()(())((()()))"
```

**Output:**

```text
3
```

## Approach

The solution uses a **depth counter** to track the current level of nested parentheses.

1. When encountering `'('`, increment `depth`.
2. Update `maxDepth` if the current depth is greater.
3. When encountering `')'`, decrement `depth`.
4. Return `maxDepth` after processing the entire string.

Since the input is guaranteed to be a valid parentheses string, the depth will never become negative.

## Complexity

* **Time:** `O(n)` — each character is processed once.
* **Space:** `O(1)` — only two integer variables are used.

## Java Solution

```java
class Solution {
    public int maxDepth(String s) {
        int depth = 0;
        int maxDepth = 0;

        for (char c : s.toCharArray()) {
            if (c == '(') {
                depth++;
                maxDepth = Math.max(maxDepth, depth);
            } else if (c == ')') {
                depth--;
            }
        }

        return maxDepth;
    }
}
```

## Key Insight

The nesting depth is simply the **maximum number of currently open parentheses** encountered while scanning the string from left to right.

For example:

```text
"((()))"

(  → 1
(  → 2
(  → 3  ← maximum depth
)  → 2
)  → 1
)  → 0
```

Therefore, the maximum nesting depth is `3`.
