# LeetCode #3871 — Count Commas in Range II

## Problem

Given an integer `n`, return the total number of commas used when writing every integer from `1` to `n` inclusive using standard number formatting.

A comma is placed after every three digits from the right.

Numbers with fewer than four digits do not contain commas.

### Examples

```text
Input:  n = 1002
Output: 3

Explanation:
1,000 → 1 comma
1,001 → 1 comma
1,002 → 1 comma

Total = 3
```

```text
Input:  n = 998
Output: 0
```

```text
Input:  n = 1,004,590
Output: 1,008,182
```

## Approach

Instead of formatting every number individually, count how many numbers contain a comma at each digit position.

### First comma

Every number from:

```text
1,000 to n
```

contains at least one comma.

The number of such values is:

```text
n - 1,000 + 1
= n - 999
```

### Second comma

Every number from:

```text
1,000,000 to n
```

contains a second comma.

The number of such values is:

```text
n - 1,000,000 + 1
= n - 999,999
```

The same pattern continues for larger digit groups.

Because the constraint is:

```text
1 <= n <= 10^15
```

there can be at most 5 commas per number, corresponding to:

```text
1,000
1,000,000
1,000,000,000
1,000,000,000,000
1,000,000,000,000,000
```

Therefore, we can simply check these five thresholds.

## Algorithm

1. Initialize `result = 0`.
2. If `n >= 1,000`, add `n - 999`.
3. If `n >= 1,000,000`, add `n - 999,999`.
4. If `n >= 1,000,000,000`, add `n - 999,999,999`.
5. If `n >= 1,000,000,000,000`, add `n - 999,999,999,999`.
6. If `n >= 1,000,000,000,000,000`, add `n - 999,999,999,999,999`.
7. Return `result`.

## Walkthrough

For:

```text
n = 1,004,590
```

### First comma

Numbers from `1,000` through `1,004,590`:

```text
1,004,590 - 999 = 1,003,591
```

### Second comma

Numbers from `1,000,000` through `1,004,590`:

```text
1,004,590 - 999,999 = 4,591
```

### Total

```text
1,003,591 + 4,591
= 1,008,182
```

Therefore:

```text
Output = 1,008,182
```

## Correctness

For every comma position `10^(3k)`, all integers greater than or equal to that threshold contain another comma.

For example:

- `1,000` and above have at least 1 comma.
- `1,000,000` and above have at least 2 commas.
- `1,000,000,000` and above have at least 3 commas.

For a threshold `p`, the number of integers from `p` through `n` is:

```text
n - p + 1
```

The algorithm adds this quantity independently for every possible comma position. Thus, every comma appearing in every formatted number is counted exactly once.

Therefore, the algorithm correctly returns the total number of commas.

## Complete Implementation

```java
class Solution {
    public long countCommas(long n) {
        long result = 0;

        if (n >= 1_000L)
            result += n - 999L;

        if (n >= 1_000_000L)
            result += n - 999_999L;

        if (n >= 1_000_000_000L)
            result += n - 999_999_999L;

        if (n >= 1_000_000_000_000L)
            result += n - 999_999_999_999L;

        if (n >= 1_000_000_000_000_000L)
            result += n - 999_999_999_999_999L;

        return result;
    }
}
```

## Complexity

### Time Complexity

```text
O(1)
```

There are exactly five threshold checks regardless of the size of `n`.

### Space Complexity

```text
O(1)
```

Only a single `long` accumulator is used.

## Optimization

This implementation is optimized for the given constraint of `n <= 10^15`.

- No string conversion
- No number formatting
- No arrays
- No loops
- No collections
- No recursion
- No modulo operations
- No object allocation
- Uses primitive `long` arithmetic
- Exactly five threshold checks
- Constant time and constant auxiliary space

The solution avoids iterating through all numbers from `1` to `n`, which would be infeasible for values as large as `10^15`.

## Key Concepts

- Mathematical counting
- Digit grouping
- Threshold counting
- Constant-time algorithms
- Primitive arithmetic
- Range counting

## Language

Java

## LeetCode 

https://leetcode.com/problems/count-commas-in-range-ii/description/?envType=daily-question&envId=2026-09-09
