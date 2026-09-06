# LeetCode #9 — Palindrome Number

## Problem

Given an integer `x`, return `true` if `x` is a palindrome, and `false` otherwise.

A palindrome number reads the same backward as forward.

### Examples

```text
Input:  x = 121
Output: true
Explanation: 121 reads as 121 from left to right and right to left.
```

```text
Input:  x = -121
Output: false
Explanation: From left to right it reads -121, but backward it becomes 121-.
```

```text
Input:  x = 10
Output: false
Explanation: 10 reads as 01 backward.
```

### Constraints

```text
-2³¹ <= x <= 2³¹ - 1
```

### Follow-Up

Solve the problem without converting the integer to a string.

## Approach

Instead of converting the number to a string or reversing the entire integer, reverse only half of the digits.

For example:

```text
x = 12321

Original:       12321
Reverse half:      12
Middle digit:       3
```

After reversing half:

```text
x          = 12
reversed   = 123
```

For numbers with an odd number of digits, the middle digit does not matter, so we remove it using:

```java
reversed / 10
```

## Important Edge Cases

### 1. Negative numbers

Negative numbers cannot be palindromes because the minus sign only appears on one side.

```java
if (x < 0) {
    return false;
}
```

### 2. Numbers ending in zero

A number ending in `0` cannot be a palindrome unless the number itself is `0`.

For example:

```text
10 → 01
```

Therefore:

```java
if (x % 10 == 0 && x != 0) {
    return false;
}
```
## Half-Reversal Optimization

A common approach is to reverse the entire number:

```text
123456 → 654321
```

However, this is unnecessary.

We only need to compare the first half with the reversed second half.

The loop continues while:

```java
x > reversed
```

This guarantees that we stop after processing approximately half the digits.

### Even number of digits

```text
1221

x = 12
reversed = 12
```

Comparison:

```java
x == reversed
```

Result:

```text
true
```

### Odd number of digits

```text
12321

x = 12
reversed = 123
```

The middle digit is `3`, so remove it:

```java
reversed / 10 = 12
```

Comparison:

```java
x == reversed / 10
```

Result:

```text
true
```

---

## Algorithm

1. If `x` is negative, return `false`.
2. If `x` ends in `0` and is not `0`, return `false`.
3. Initialize `reversed = 0`.
4. Reverse digits from `x` until half of the number has been processed.
5. Check:

   * `x == reversed` for an even number of digits.
   * `x == reversed / 10` for an odd number of digits.
6. Return the result.

---

## Walkthrough

Consider:

```text
x = 12321
```

### Iteration 1

```text
x = 12321
reversed = 0
```

Take the last digit:

```text
1
```

Update:

```text
reversed = 1
x = 1232
```

### Iteration 2

Take the last digit:

```text
2
```

Update:

```text
reversed = 12
x = 123
```

### Iteration 3

Take the last digit:

```text
3
```

Update:

```text
reversed = 123
x = 12
```

Now:

```text
x <= reversed
```

Stop.

Since this is an odd-length number:

```text
reversed / 10 = 12
```

Compare:

```text
x == reversed / 10
12 == 12
```

Therefore:

```text
true
```

---

## Correctness

The algorithm reverses only the second half of the digits.

When the loop terminates:

* For an even number of digits, both halves contain the same number of digits.
* For an odd number of digits, `reversed` contains one additional middle digit.

Therefore, removing the middle digit with `reversed / 10` allows the two halves to be compared directly.

If the two halves are equal, the original number reads identically in both directions and is therefore a palindrome.

## Complete Implementation

```java
class Solution {
    public boolean isPalindrome(int x) {
        // Negative numbers are never palindromes.
        // Numbers ending in 0 are only palindromes when x == 0.
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }

        int reversed = 0;

        // Reverse only half of the digits.
        while (x > reversed) {
            reversed = reversed * 10 + x % 10;
            x /= 10;
        }

        // Even digits: x == reversed
        // Odd digits: x == reversed / 10
        return x == reversed || x == reversed / 10;
    }
}
```

## Complexity

Let `d` be the number of digits in `x`.

### Time Complexity

```text
O(log₁₀(x))
```

Only approximately half of the digits are processed.

### Space Complexity

```text
O(1)
```

Only a few integer variables are used.

## Why This Is Optimized

This solution avoids:

- String conversion
- Character arrays
- Extra arrays
- Full integer reversal
- Additional data structures
- Integer overflow from reversing the entire number

The half-reversal technique is both time-efficient and memory-efficient while directly satisfying the follow-up requirement.

## Key Concepts

- Integer manipulation
- Digit extraction
- `% 10` to obtain the last digit
- `/ 10` to remove the last digit
- Half reversal
- Overflow avoidance
- Constant-space algorithms

## Language

Java

## LeetCode

Problem: 9 — Palindrome Number
Link:
