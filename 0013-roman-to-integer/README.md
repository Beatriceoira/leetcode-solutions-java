# 13. Roman to Integer

**LeetCode Problem:** [13. Roman to Integer](https://leetcode.com/problems/roman-to-integer/)
**Difficulty:** Easy
**Language:** Java
**Topics:** Hash Table, Math, String

## Problem

Given a Roman numeral string, convert it into its corresponding integer.

Roman numerals use seven symbols:

| Symbol | Value |
| ------ | ----: |
| `I`    |     1 |
| `V`    |     5 |
| `X`    |    10 |
| `L`    |    50 |
| `C`    |   100 |
| `D`    |   500 |
| `M`    |  1000 |

Roman numerals are normally written from largest to smallest. However, certain combinations use subtraction:

* `IV` = 4
* `IX` = 9
* `XL` = 40
* `XC` = 90
* `CD` = 400
* `CM` = 900

## Approach

The solution processes the string from left to right and handles the six subtractive cases directly.

For each character:

* `I` is normally worth `1`, but becomes `-1` when followed by `V` or `X`.
* `X` is normally worth `10`, but becomes `-10` when followed by `L` or `C`.
* `C` is normally worth `100`, but becomes `-100` when followed by `D` or `M`.
* `V`, `L`, and `D` always contribute their normal values.
* `M` contributes `1000`.

Because the input is guaranteed to be a valid Roman numeral, no additional validation is necessary.

### Example

For:

```text
MCMXCIV
```

The values become:

```text
M   C   M   X   C   I   V
100 -100 1000 -10 100 -1  5
```

Therefore:

```text
1000 - 100 + 1000 - 10 + 100 - 1 + 5 = 1994
```

## Complexity

* **Time:** `O(n)`
* **Space:** `O(1)`

No additional data structures are required, and the algorithm makes a single pass through the string.

## Key Takeaway

The important observation is that Roman numeral subtraction only occurs in six specific combinations. Handling those combinations directly eliminates the need for a lookup table or auxiliary data structure while keeping the implementation constant-space.
