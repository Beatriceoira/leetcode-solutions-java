# 115. Distinct Subsequences

## Problem

Given two strings `s` and `t`, return the number of distinct subsequences of `s` that equal `t`.

A subsequence is formed by deleting zero or more characters from a string while keeping the remaining characters in their original order.

The answer is guaranteed to fit in a 32-bit signed integer.

## Examples

### Example 1

```text
Input:
s = "rabbbit"
t = "rabbit"

Output:
3
```

There are three distinct ways to select characters from `"rabbbit"` to form `"rabbit"`.

### Example 2

```text
Input:
s = "babgbag"
t = "bag"

Output:
5
```

There are five distinct subsequences of `s` that form `"bag"`.

## Approach

This problem is solved using dynamic programming.

Instead of explicitly generating every subsequence, we count how many ways each prefix of `t` can be formed from the characters processed so far in `s`.

Define:

```text
dp[j] = number of ways to form t[0..j-1]
```

Initially:

```text
dp[0] = 1
```

There is exactly one way to form an empty string: select nothing.

All other states start at `0`.

When processing a character `s[i]`, if it matches `t[j - 1]`, we have two possibilities:

1. Skip `s[i]`.
2. Use `s[i]` to match `t[j - 1]`.

Therefore:

```text
dp[j] += dp[j - 1]
```

The DP array is traversed backwards so that `dp[j - 1]` still represents the previous state from before processing the current character.

## Why Traverse Backwards?

Consider:

```text
s = "a"
t = "aa"
```

When processing the first `a`, we must not use that same character twice.

If we traversed forward:

```text
dp[1] → dp[2]
```

the newly updated `dp[1]` could immediately be used to update `dp[2]`, incorrectly treating the same character as two characters.

By traversing backwards:

```text
dp[2] → dp[1]
```

`dp[j - 1]` remains the value from the previous iteration.

This is the standard technique for compressing a 2D DP into a 1D DP.

## Optimization

The straightforward implementation checks every position of `t` for every character of `s`:

```java
for (int j = n; j >= 1; j--)
```

However, after processing only `i + 1` characters of `s`, it is impossible to have matched more than `i + 1` characters of `t`.

Therefore, we can restrict the inner loop:

```java
final int limit = Math.min(i + 1, n);

for (int j = limit; j > 0; j--)
```

This eliminates unnecessary iterations during the beginning of the scan.

## Character Array Optimization

The target string is converted once:

```java
final char[] target = t.toCharArray();
```

This allows the hot loop to use direct array access:

```java
if (c == target[j - 1])
```

instead of repeatedly calling:

```java
t.charAt(j - 1)
```

The JVM already optimizes `String.charAt()`, so this is a relatively small optimization, but it keeps the inner loop simple and predictable.

## Why Use `long[]`?

The final answer is guaranteed to fit within a signed 32-bit integer, but using:

```java
long[] dp
```

is safer for intermediate DP states.

The guarantee applies to the final result `dp[n]`. Intermediate states representing shorter prefixes of `t` can potentially become much larger.

Using `long` prevents intermediate integer overflow.

The final result can safely be converted back to `int`:

```java
return (int) dp[n];
```

because the problem guarantees that the answer fits in a 32-bit signed integer.

## Algorithm

1. Let `m = s.length()` and `n = t.length()`.
2. If `t` is longer than `s`, return `0`.
3. Convert `t` into a character array.
4. Create a `long[] dp` of size `n + 1`.
5. Set:

   ```text
   dp[0] = 1
   ```
6. Process every character of `s`.
7. For each character:

   - Determine the maximum relevant target prefix length.
   - Traverse the target positions backwards.
   - If the characters match, add `dp[j - 1]` to `dp[j]`.
8. Return `dp[n]`.

## Walkthrough

Consider:

```text
s = "rabbbit"
t = "rabbit"
```

Initially:

```text
dp = [1, 0, 0, 0, 0, 0, 0]
```

The first `r` matches the first character of `t`:

```text
dp = [1, 1, 0, 0, 0, 0, 0]
```

As the `a`, `b`, `b`, `b`, `i`, and `t` characters are processed, matching states accumulate.

The three different choices for which `b` characters to select result in:

```text
dp[6] = 3
```

Therefore:

```text
answer = 3
```

## Correctness

For every processed prefix of `s`, `dp[j]` represents the number of distinct subsequences of that prefix that equal `t[0..j-1]`.

When the current character does not match `t[j-1]`, no new subsequences can be created for that state, so `dp[j]` remains unchanged.

When the characters match, every subsequence that already forms `t[0..j-2]` can append the current character to form `t[0..j-1]`.

Thus:

```text
dp[j] = dp[j] + dp[j - 1]
```

The backward traversal ensures that `dp[j - 1]` comes from the previous processing state, preventing the current character from being reused.

Therefore, after processing the entire string `s`, `dp[n]` is exactly the number of distinct subsequences of `s` equal to `t`.

## Complete Implementation

```java
class Solution {
    public int numDistinct(String s, String t) {
        final int m = s.length();
        final int n = t.length();

        if (n > m) return 0;

        final char[] target = t.toCharArray();
        final long[] dp = new long[n + 1];
        dp[0] = 1;

        for (int i = 0; i < m; i++) {
            final char c = s.charAt(i);
            final int limit = Math.min(i + 1, n);

            for (int j = limit; j > 0; j--) {
                if (c == target[j - 1]) {
                    dp[j] += dp[j - 1];
                }
            }
        }

        return (int) dp[n];
    }
}
```

---

## Complexity

Let:

```text
m = s.length()
n = t.length()
```

### Time Complexity

```text
O(m × n)
```

In the worst case, every character of `s` can require checking every relevant character of `t`.

### Space Complexity

```text
O(n)
```

Only the 1D DP array and the character array of `t` are stored.

## Performance Characteristics

This implementation avoids the memory overhead of a traditional 2D DP table.

### Compared with 2D DP

```text
2D DP:
Time:  O(m × n)
Space: O(m × n)

1D DP:
Time:  O(m × n)
Space: O(n)
```

The optimized solution therefore retains the same asymptotic runtime while dramatically reducing memory usage.

Additional optimizations include:
- Primitive `long[]` DP array
- Primitive `char[]` target
- No recursion
- No collections
- No boxing
- No object creation inside the main loops
- Backward DP traversal
- Early target-length limit
- Immediate `0` return when `t.length() > s.length()`
- Sequential memory access

## Key Concepts

- Dynamic Programming
- 1D DP
- Subsequence Counting
- State Compression
- Backward Traversal
- Prefix Matching
- Memory Optimization

## Language

Java

## LeetCode

115 — Distinct Subsequences(https://leetcode.com/problems/distinct-subsequences/description/?envType=daily-question&envId=2026-09-06)