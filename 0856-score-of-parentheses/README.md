# 856. Score of Parentheses

Link - https://leetcode.com/problems/score-of-parentheses/description/

## Problem

Given a balanced parentheses string `s`, calculate its score according to these rules:

* `()` has a score of `1`.
* `AB` has a score of `A + B`, where `A` and `B` are balanced parentheses strings.
* `(A)` has a score of `2 * A`.

The input string is guaranteed to be balanced.

### Example 1

```text
Input: s = "()"
Output: 1
```

### Example 2

```text
Input: s = "(())"
Output: 2
```

### Example 3

```text
Input: s = "()()"
Output: 2
```

## Approach

A common solution uses a stack to track the scores of nested parentheses.

However, because the input is guaranteed to be balanced, we can solve the problem using only:

* `depth` to track the current nesting level
* `score` to store the accumulated result

The key observation is:

```text
() at depth d contributes 2^d
```

where `d` is the nesting depth after removing the closing parenthesis.

For example:

```text
(())()
```

The first `()` is nested once:

```text
(()) → 2
```

The second `()` is at the outermost level:

```text
() → 1
```

Total:

```text
2 + 1 = 3
```

## How the Algorithm Works

### Opening Parenthesis

When encountering:

```text
(
```

increase the nesting depth:

```java
depth++;
```

### Closing Parenthesis

When encountering:

```text
)
```

first decrease the depth:

```java
depth--;
```

Then check whether the previous character was `(`:

```java
s.charAt(i - 1) == '('
```

If so, we found the primitive pair:

```text
()
```

Its score is:

```text
2^depth
```

In Java, this can be calculated efficiently using:

```java
1 << depth
```

## Example Walkthrough

Consider:

```text
s = "(())()"
```

Process the string from left to right:

```text
(       depth = 1
((      depth = 2
(()     depth = 1 → score += 2^1 = 2
(())    depth = 0
()      depth = 0 → score += 2^0 = 1
```

Final score:

```text
2 + 1 = 3
```

## Algorithm

1. Initialize `score = 0`.
2. Initialize `depth = 0`.
3. Iterate through the string.
4. If the current character is `'('`, increment `depth`.
5. Otherwise:

   * Decrement `depth`.
   * If the previous character is `'('`, add `2^depth` to `score`.
6. Return `score`.

## Optimized Java Solution

```java
class Solution {
    public int scoreOfParentheses(String s) {
        int score = 0;
        int depth = 0;

        for (int i = 0, n = s.length(); i < n; i++) {
            if (s.charAt(i) == '(') {
                depth++;
            } else {
                depth--;

                if (s.charAt(i - 1) == '(') {
                    score += 1 << depth;
                }
            }
        }

        return score;
    }
}
```

## Why No Stack Is Needed

A stack-based solution can store the score associated with every nesting level.

For example:

```text
( ( ) )
```

would require tracking multiple levels of nested state.

However, the only information needed to calculate the score of a primitive `()` is its current depth.

Therefore, instead of storing:

```text
stack → O(n)
```

we only maintain:

```text
depth → O(1)
score → O(1)
```

This reduces the auxiliary space from `O(n)` to `O(1)`.

## Bit Shift Optimization

The score of a primitive pair is:

```text
2^depth
```

Instead of using:

```java
(int) Math.pow(2, depth)
```

the solution uses:

```java
1 << depth
```

A left shift by `depth` positions is equivalent to multiplying `1` by `2^depth`.

For example:

```text
1 << 0 = 1
1 << 1 = 2
1 << 2 = 4
1 << 3 = 8
```

This avoids floating-point arithmetic and is appropriate because the score is always a power of two for an individual primitive pair.

## Complexity

Let `n` be the length of the string.

### Time Complexity

```text
O(n)
```

The string is traversed exactly once.

### Auxiliary Space

```text
O(1)
```

Only two integer variables are maintained:

```text
score
depth
```

No stack, array, map, or other data structure is required.

## Key Takeaway

The main insight is that a primitive `()` contributes a score determined entirely by its nesting depth.

```text
depth 0 → 1
depth 1 → 2
depth 2 → 4
depth 3 → 8
```

Therefore, instead of explicitly constructing the nested score structure with a stack, we can track the nesting depth directly.

The final solution achieves:

```text
Time:  O(n)
Space: O(1)
```

while making only a single pass through the input.
