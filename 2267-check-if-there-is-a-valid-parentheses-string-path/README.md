# 2267. Check if There Is a Valid Parentheses String Path

Java solution for **LeetCode 2267 — Check if There Is a Valid Parentheses String Path**.

The solution uses **dynamic programming with bitsets** to efficiently track all possible parentheses balances at each cell. Instead of iterating through every possible balance individually, reachable balances are packed into `long` values and processed **64 balances at a time** using bitwise operations.

## Problem

Given an `m × n` grid containing only `'('` and `')'`, determine whether there exists a path from the top-left cell to the bottom-right cell that:

* Moves only **right** or **down**
* Produces a valid parentheses string

A parentheses string is valid when:

* Every prefix has at least as many `'('` as `')'`
* The total number of `'('` equals the total number of `')'`

For example:

```text
(())
```

is valid, while:

```text
)(
```

is not.

## Approach

### 1. Parentheses Balance

Represent the parentheses balance as:

```text
'(' → +1
')' → -1
```

A valid path must therefore:

```text
balance >= 0
```

at every point and finish with:

```text
balance == 0
```

### 2. Bitset Dynamic Programming

For every grid column, maintain a bitset representing all reachable balance values.

A bit represents whether a particular balance is reachable:

```text
bit 0  → balance 0
bit 1  → balance 1
bit 2  → balance 2
...
bit 63 → balance 63
```

A Java `long` stores 64 bits, allowing 64 possible balances to be processed simultaneously.

For larger balances, additional `long` values are used.

### 3. Parentheses Transitions

When the current cell contains `'('`, every reachable balance increases by one.

This is implemented using a left shift:

```java
x << 1
```

When the cell contains `')'`, every reachable balance decreases by one:

```java
x >>> 1
```

Carry bits transfer between adjacent `long` values so balances can cross the 64-bit boundaries.

### 4. Rolling DP

A cell can only be reached from:

```text
↑ above
← left
```

Therefore, we do not need the complete `m × n` DP table.

The flat array:

```java
long[] dp
```

stores the reachable balances for each column.

The state from above is already stored in the current column, while the state from the left is stored in the previous column.

## Optimized Implementation

```java
class Solution {

    public boolean hasValidPath(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int len = m + n - 1;

        if ((len & 1) != 0) {
            return false;
        }

        if (grid[0][0] == ')' || grid[m - 1][n - 1] == '(') {
            return false;
        }

        int words = (len >>> 6) + 1;
        long[] dp = new long[n * words];

        // '(' -> balance 1
        dp[0] = 2L;

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {

                if (r == 0 && c == 0) {
                    continue;
                }

                int base = c * words;

                // Merge from left.
                if (c > 0) {
                    int left = base - words;

                    for (int w = 0; w < words; w++) {
                        dp[base + w] |= dp[left + w];
                    }
                }

                if (grid[r][c] == '(') {

                    long carry = 0;

                    for (int w = 0; w < words; w++) {
                        int i = base + w;
                        long x = dp[i];

                        dp[i] = (x << 1) | carry;
                        carry = x >>> 63;
                    }

                } else {

                    long carry = 0;

                    for (int w = words - 1; w >= 0; w--) {
                        int i = base + w;
                        long x = dp[i];

                        dp[i] = (x >>> 1) | carry;
                        carry = x << 63;
                    }
                }
            }
        }

        // Bit 0 represents balance 0.
        return (dp[(n - 1) * words] & 1L) != 0;
    }
}
```

## Why Bitsets?

A conventional DP approach might maintain:

```text
dp[row][column][balance]
```

Since:

```text
m, n <= 100
```

the balance can be as large as:

```text
m + n - 1 <= 199
```

This results in a large number of individual boolean states.

The bitset approach compresses these states.

Instead of checking:

```text
balance = 0
balance = 1
balance = 2
...
balance = 199
```

individually, one `long` handles 64 balances simultaneously.

For the maximum grid size:

```text
m + n - 1 = 199
```

only:

```text
ceil(200 / 64) = 4
```

`long` values are required per column.

## Complexity

Let:

```text
L = m + n - 1
```

The bitset representation contains approximately:

```text
L / 64
```

machine words.

Therefore:

**Time**

```text
O(m × n × L / 64)
```

**Space**

```text
O(n × L / 64)
```

With `m, n <= 100`, at most four `long` values are required per column.

## Optimizations Used

### Early rejection

A valid parentheses string must have an even number of characters:

```java
if ((len & 1) != 0) {
    return false;
}
```

The path also cannot begin with `')'` or end with `'('`:

```java
if (grid[0][0] == ')' || grid[m - 1][n - 1] == '(') {
    return false;
}
```

### Flat memory layout

Instead of using nested arrays:

```java
long[][][]
```

the implementation uses:

```java
long[] dp
```

This reduces array indirection and improves memory locality.

### Rolling columns

Only the current column states are required because each cell depends on:

```text
above
left
```

This significantly reduces memory usage.

### Bitwise balance transitions

Opening parentheses:

```java
x << 1
```

Closing parentheses:

```java
x >>> 1
```

allow many balance states to be updated simultaneously.

### No per-cell allocations

The DP reuses the same primitive `long[]`, avoiding object creation inside the main loops.

## Example

Input:

```text
[
  ["(", "(", "("],
  [")", "(", ")"],
  ["(", "(", ")"],
  ["(", "(", ")"]
]
```

A valid path can produce:

```text
()(())
```

or:

```text
((()))
```

Therefore:

```text
Output: true
```

## Key Takeaway

The main optimization is to treat the set of reachable parentheses balances as a **bitset rather than individual DP states**.

This changes the practical amount of work from roughly:

```text
m × n × (m + n)
```

balance operations to approximately:

```text
m × n × (m + n) / 64
```

machine-word operations while retaining the same underlying dynamic-programming logic.

## Language

* Java
* Bitwise operations
* Dynamic Programming
* Bitset optimization

## LeetCode

Problem: 2267. Check if There Is a Valid Parentheses String Path
Link: https://leetcode.com/problems/check-if-there-is-a-valid-parentheses-string-path/description/?envType=daily-question&envId=2026-09-29