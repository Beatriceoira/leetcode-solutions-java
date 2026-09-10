# LeetCode #3870 — Count Commas in Range

## Problem

You are given an integer `n`.

Return the **total number of commas** used when writing all integers from `[1, n]` inclusive using standard number formatting.

In standard formatting:

- A comma is inserted after every three digits from the right.
- Numbers with fewer than 4 digits contain no commas.

### Examples

```text
Input: n = 1002
Output: 3
```

Explanation:

```text
1,000 → 1 comma
1,001 → 1 comma
1,002 → 1 comma
```

Total:

```text
3 commas
```

```text
Input: n = 998
Output: 0
```

Explanation:

All numbers from `1` through `998` contain fewer than four digits, so no commas are used.

## Approach

Instead of checking every number individually, we count how many commas each range of numbers contributes.

The number of commas changes whenever numbers reach another group of three digits:

```text
1,000          → 1 comma
1,000,000      → 2 commas
1,000,000,000  → 3 commas
```

For a given threshold, every number from that threshold through `n` contributes an additional comma.

### Range 1: 1,000+

For numbers from `1,000` to `n`, each number has at least one comma.

Count:

```text
n - 1,000 + 1
= n - 999
```

### Range 2: 1,000,000+

For numbers from `1,000,000` to `n`, each number has an additional comma.

Count:

```text
n - 1,000,000 + 1
= n - 999,999
```

Since the problem constraint allows values up to `10^5`, only the first range is actually relevant for the official problem. The additional branches make the implementation capable of handling larger values.

## Algorithm

1. If `n < 1,000`, return `0`.
2. If `n < 1,000,000`, return `n - 999`.
3. If `n < 1,000,000,000`, calculate the contribution from both comma positions.
4. Otherwise, calculate the contribution from three comma positions.
5. Use `long` arithmetic for the final calculation to prevent intermediate integer overflow.
6. Return the result as an integer.

## Walkthrough

For:

```text
n = 1002
```

Since:

```text
1002 >= 1000
1002 < 1,000,000
```

the second condition applies:

```text
answer = 1002 - 999
       = 3
```

Therefore:

```text
Output = 3
```

For:

```text
n = 998
```

we have:

```text
998 < 1000
```

so:

```text
Output = 0
```

## Correctness

Every number below `1,000` contains zero commas.

Every number from `1,000` through `999,999` contains exactly one comma.

Every number from `1,000,000` through `999,999,999` contains exactly two commas.

Therefore, the solution can count commas by dividing the range into digit-length intervals and adding the appropriate number of commas contributed by each interval.

The formulas account for every number in `[1, n]` exactly once for each comma it contains.

Thus, the algorithm returns the total number of commas.

## Complete Implementation

```java
class Solution {
    public int countCommas(int n) {
        if (n < 1_000) return 0;
        if (n < 1_000_000) return n - 999;
        if (n < 1_000_000_000) return 2 * n - 1_000_998;

        return 3L * n - 1_000_000_997 > Integer.MAX_VALUE
                ? (int)(3L * n - 1_000_000_997)
                : (int)(3L * n - 1_000_000_997);
    }
}
```

## Complexity

### Time Complexity

```text
O(1)
```

The algorithm performs only a fixed number of conditional checks regardless of the size of `n`.

### Space Complexity

```text
O(1)
```

Only primitive variables are used and no additional data structures are allocated.

## Optimization

The solution avoids:

- Iterating from `1` to `n`
- Converting numbers to strings
- Formatting individual numbers
- Arrays
- Collections
- Hash tables
- Recursion
- Dynamic programming

It directly calculates the number of commas using arithmetic.

### Redundant Overflow Check

The final expression currently contains:

```java
return 3L * n - 1_000_000_997 > Integer.MAX_VALUE
        ? (int)(3L * n - 1_000_000_997)
        : (int)(3L * n - 1_000_000_997);
```

Both branches perform the exact same cast and return the exact same value.

Therefore, this can simply be written as:

```java
return (int)(3L * n - 1_000_000_997);
```

However, for the **official constraint `n <= 10^5`**, this entire third branch is unreachable. The truly minimal solution for the given problem is:

```java
class Solution {
    public int countCommas(int n) {
        return n < 1_000 ? 0 : n - 999;
    }
}
```

This directly reflects the actual constraint and requires only one comparison and one subtraction.

## Key Concepts

- Mathematical counting
- Range counting
- Digit grouping
- Conditional branching
- Integer arithmetic
- Overflow awareness
- Constant-time optimization

## Language

Java

## LeetCode 

https://leetcode.com/problems/count-commas-in-range/description/?envType=daily-question&envId=2026-09-08