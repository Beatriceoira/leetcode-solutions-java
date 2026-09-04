# LeetCode 8 — String to Integer (atoi)

## Problem

Implement the `myAtoi(string s)` function, which converts a string into a 32-bit signed integer.

The conversion follows these rules:

1. Ignore leading spaces.
2. Determine the sign from an optional `+` or `-`.
3. Read consecutive digits and construct the integer.
4. Stop reading when a non-digit character is encountered.
5. Clamp values outside the 32-bit signed integer range:

```text
[-2³¹, 2³¹ - 1]
```

Therefore:

```text
Integer.MIN_VALUE = -2,147,483,648
Integer.MAX_VALUE =  2,147,483,647
```

If no digits are found after the optional sign, return `0`.


## Examples

### Example 1

```text
Input:
s = "42"

Output:
42
```

The string begins directly with digits, so the entire number is converted.



### Example 2

```text
Input:
s = " -042"

Output:
-42
```

The algorithm:

```text
1. Skips the leading space.
2. Detects '-'.
3. Reads "042".
4. Converts it to -42.
```

Leading zeros do not affect the numerical result.



### Example 3

```text
Input:
s = "1337c0d3"

Output:
1337
```

The digits are read until `c` is encountered.

```text
1337 | c0d3
     ↑
   stop
```

---

### Example 4

```text
Input:
s = "0-1"

Output:
0
```

`0` is a valid digit, but `-` is not a digit, so parsing stops immediately.



### Example 5

```text
Input:
s = "words and 987"

Output:
0
```

The first character is not a digit or sign, so no number can be parsed.


# Approach

The solution performs a single left-to-right scan through the string.

The process has three stages:

```text
Leading whitespace
       ↓
Sign detection
       ↓
Digit conversion
       ↓
Overflow checking
```

This avoids creating substrings or using regular expressions.



# Step 1 — Skip Leading Whitespace

The algorithm first skips all leading spaces:

```java
while (i < n && s.charAt(i) == ' ') {
    i++;
}
```

For example:

```text
"   -42"
   ↑
```

After this step, `i` points to `-`.

If the string contains only spaces:

```java
if (i == n) return 0;
```



# Step 2 — Determine the Sign

The next character is checked for `+` or `-`:

```java
int sign = 1;

if (s.charAt(i) == '+' || s.charAt(i) == '-') {
    sign = (s.charAt(i) == '-') ? -1 : 1;
    i++;
}
```

The default sign is positive.

Therefore:

```text
"42"   → +1
"+42"  → +1
"-42"  → -1
```

Only one sign character is accepted.


# Step 3 — Build the Number

Digits are processed while they are valid:

```java
while (i < n && Character.isDigit(s.charAt(i))) {
    num = num * 10 + (s.charAt(i) - '0');
    i++;
}
```

The standard decimal construction:

```text
num = num × 10 + digit
```

is used.

For:

```text
"1337"
```

the calculation is:

```text
0
→ 1
→ 13
→ 133
→ 1337
```


# Step 4 — Stop at the First Non-Digit

The loop condition is:

```java
Character.isDigit(s.charAt(i))
```

Therefore, parsing automatically stops when a non-digit is encountered.

For:

```text
"1337c0d3"
```

the algorithm processes:

```text
1 → 13 → 133 → 1337
```

and stops at:

```text
c
```

Characters after the first non-digit are ignored.



# Step 5 — Handle Overflow

A 32-bit signed integer can only represent:

```text
-2,147,483,648
to
 2,147,483,647
```

The implementation uses a `long` while constructing the number:

```java
long num = 0;
```

This provides additional space to detect values that exceed the `int` range.

For positive values:

```java
if (sign == 1 && num > Integer.MAX_VALUE) {
    return Integer.MAX_VALUE;
}
```

For negative values:

```java
if (sign == -1 && -num < Integer.MIN_VALUE) {
    return Integer.MIN_VALUE;
}
```

Thus:

```text
"2147483648"  → 2147483647
"-2147483649" → -2147483648
```



# Why `long` Is Used

The final answer is an `int`, but the intermediate value is stored as:

```java
long num
```

This is important because the parsed number may temporarily exceed the `int` range.

For example:

```text
2147483648
```

cannot be represented by an `int`, but it can be represented by a `long`.

The algorithm detects the overflow before converting the value back to `int`.



# Walkthrough

Consider:

```text
s = " -042"
```

### Initial State

```text
i = 0
num = 0
sign = 1
```

### Skip Whitespace

The first character is a space.

```text
i = 1
```

Now:

```text
" -042"
  ↑
```

### Read Sign

The character is `-`.

Therefore:

```text
sign = -1
i = 2
```

### Read Digits

The digits are:

```text
0 → 4 → 2
```

The number develops as:

```text
0
→ 0
→ 4
→ 42
```

### Final Result

```java
return (int)(sign * num);
```

becomes:

```text
-1 × 42 = -42
```

Therefore:

```text
Output: -42
```


# Algorithm

```text
1. Initialize index i = 0.
2. Skip leading spaces.
3. If the string ends, return 0.
4. Determine the sign.
5. Read consecutive digits.
6. Add each digit to the number.
7. Check for positive or negative overflow.
8. Stop at the first non-digit.
9. Return sign × number.
```



# Complete Implementation

```java
class Solution {
    public int myAtoi(String s) {
        int i = 0, n = s.length();

        // Skip leading whitespace
        while (i < n && s.charAt(i) == ' ') {
            i++;
        }

        if (i == n) {
            return 0;
        }

        // Determine sign
        int sign = 1;

        if (s.charAt(i) == '+' || s.charAt(i) == '-') {
            sign = (s.charAt(i) == '-') ? -1 : 1;
            i++;
        }

        // Build number
        long num = 0;

        while (i < n && Character.isDigit(s.charAt(i))) {
            num = num * 10 + (s.charAt(i) - '0');

            // Positive overflow
            if (sign == 1 && num > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }

            // Negative overflow
            if (sign == -1 && -num < Integer.MIN_VALUE) {
                return Integer.MIN_VALUE;
            }

            i++;
        }

        return (int) (sign * num);
    }
}
```


# Complexity

Let `n` be the length of the string.

### Time Complexity

The string is scanned at most once:

```text
O(n)
```

### Space Complexity

Only a few primitive variables are used:

```text
O(1)
```

Therefore:

```text
Time:  O(n)
Space: O(1)
```


# Optimization

This implementation is already asymptotically optimal.

### Single Pass

The algorithm never rescans the string:

```text
O(n)
```

### No Extra Strings

It does not use:

- `substring()`
- `split()`
- Regular expressions
- Character arrays
- Collections

### Early Overflow Detection

As soon as the number exceeds the valid range, the appropriate boundary value is returned immediately:

```java
if (sign == 1 && num > Integer.MAX_VALUE)
    return Integer.MAX_VALUE;
```

or:

```java
if (sign == -1 && -num < Integer.MIN_VALUE)
    return Integer.MIN_VALUE;
```

This avoids continuing to parse unnecessarily large numbers.


# Key Concepts

- String Parsing
- Two-Pointer / Index Traversal
- Sign Handling
- ASCII Digit Conversion
- Integer Overflow
- Boundary Clamping
- Early Termination
- Single-Pass Algorithms
- Constant-Space Algorithms



## Language

Java

## LeetCode

Problem: 8 — String to Integer (atoi) 
Difficulty: Medium
Link: https://leetcode.com/problems/string-to-integer-atoi/description/