# 32. Longest Valid Parentheses

**Difficulty:** Hard
**Language:** Java
**Topics:** String, Two Pointers, Greedy


## Problem

Given a string `s` containing only `'('` and `')'`, return the length of the **longest valid (well-formed) parentheses substring**.

A valid parentheses string must have every opening parenthesis matched with a closing parenthesis in the correct order.

### Example 1

```text
Input: s = "(()"

Output: 2

Explanation:
The longest valid substring is "()".
```

### Example 2

```text
Input: s = ")()())"

Output: 4

Explanation:
The longest valid substring is "()()".
```

### Example 3

```text
Input: s = ""

Output: 0
```

## Approach

Instead of using a stack or dynamic programming array, this solution uses **two counters**:

* `left` — number of `'('`
* `right` — number of `')'`

The string is scanned twice.

### Pass 1 — Left to Right

When scanning from left to right:

* Increment `left` for `'('`.
* Increment `right` for `')'`.
* When `left == right`, the substring is balanced and its length is `2 * right`.
* When `right > left`, there are more closing parentheses than opening parentheses, so the current substring can never become valid. Reset both counters.

For example:

```text
s = ")()())"

)     → invalid → reset
()    → balanced → length 2
()()  → balanced → length 4
)     → invalid → reset
```

### Why One Pass Is Not Enough

A left-to-right scan cannot detect every valid substring.

Consider:

```text
s = "(()"
```

The counters become:

```text
(   → left = 1, right = 0
((  → left = 2, right = 0
(() → left = 2, right = 1
```

There are more opening parentheses than closing parentheses, so the counters never become equal.

However, the substring:

```text
()
```

is valid.

## Pass 2 — Right to Left

To handle excess opening parentheses, scan the string in reverse.

The logic is mirrored:

* Increment `left` for `'('`.
* Increment `right` for `')'`.
* When `left == right`, update the maximum length.
* When `left > right`, reset both counters.

This catches valid substrings that the forward scan misses.

For:

```text
s = "(()"
```

the reverse scan sees:

```text
) → right = 1
() → left = 1, right = 1 → length 2
```

Therefore the answer is correctly found.

## Algorithm

1. Initialize `max = 0`.
2. Scan from left to right.

   * Count opening and closing parentheses.
   * Update `max` whenever the counts are equal.
   * Reset when closing parentheses exceed opening parentheses.
3. Reset the counters.
4. Scan from right to left.

   * Count opening and closing parentheses.
   * Update `max` whenever the counts are equal.
   * Reset when opening parentheses exceed closing parentheses.
5. Return `max`.

## Optimized Java Solution

```java
class Solution {
    public int longestValidParentheses(String s) {
        int n = s.length();

        if (n < 2) return 0;

        int max = 0;
        int left = 0;
        int right = 0;

        // Left to right
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '(') {
                left++;
            } else {
                right++;
            }

            if (left == right) {
                int len = right << 1;
                if (len > max) {
                    max = len;
                }
            } else if (right > left) {
                left = 0;
                right = 0;
            }
        }

        // The entire string is already valid
        if (max == n) return max;

        left = 0;
        right = 0;

        // Right to left
        for (int i = n - 1; i >= 0; i--) {
            if (s.charAt(i) == '(') {
                left++;
            } else {
                right++;
            }

            if (left == right) {
                int len = left << 1;
                if (len > max) {
                    max = len;
                }
            } else if (left > right) {
                left = 0;
                right = 0;
            }
        }

        return max;
    }
}
```

## Complexity

Let `n` be the length of the string.

### Time Complexity

```text
O(n)
```

The string is traversed twice:

```text
O(n) + O(n) = O(n)
```

The two passes remain linear.

### Auxiliary Space

```text
O(1)
```

Only a fixed number of primitive variables are used:

* `n`
* `max`
* `left`
* `right`
* `i`
* `len`

No stack, array, `List`, or `Map` is allocated.

## Why This Is Memory Efficient

A common solution uses a stack:

```text
O(n) auxiliary space
```

A dynamic-programming solution can also require:

```text
O(n) auxiliary space
```

This approach requires only counters:

```text
left
right
max
```

Therefore its auxiliary memory usage is constant regardless of the input size.

The input `String` itself is provided by LeetCode and is not counted as memory allocated by the algorithm.

## Key Optimization Details

### 1. No Stack

There is no need to store indices or unmatched parentheses.

### 2. No DP Array

The solution does not maintain a value for every character.

### 3. Two Linear Passes

The reverse pass handles cases where the forward pass encounters unmatched `'('`.

### 4. Direct Maximum Comparison

Instead of:

```java
max = Math.max(max, len);
```

the solution uses:

```java
if (len > max) {
    max = len;
}
```

This avoids the method call at the source level and keeps the hot loop minimal.

### 5. Early Exit

If:

```java
max == n
```

the entire string is valid, so no longer answer is possible.

### 6. Bit Shift for Doubling

```java
right << 1
```

is equivalent to:

```java
right * 2
```

for these non-negative integer counters.

The difference is generally negligible because modern Java JIT compilers optimize simple multiplication aggressively, but the shift keeps the operation explicit.

## Key Takeaway

The key insight is that a valid parentheses substring requires equal numbers of opening and closing parentheses **without either side becoming invalid first**.

A single directional scan handles only one type of imbalance:

```text
too many ')'
```

The reverse scan handles the opposite:

```text
too many '('
```

By scanning in both directions, the algorithm achieves:

```text
Time:  O(n)
Space: O(1)
```

with no auxiliary data structures.
