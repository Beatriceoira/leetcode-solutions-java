# 12. Integer to Roman

**LeetCode:** [12. Integer to Roman](https://leetcode.com/problems/integer-to-roman/)
**Difficulty:** Medium
**Language:** Java

## Problem

Given an integer from `1` to `3999`, convert it into its corresponding Roman numeral.

Roman numerals use the following symbols:

| Symbol | Value |
| ------ | ----: |
| I      |     1 |
| V      |     5 |
| X      |    10 |
| L      |    50 |
| C      |   100 |
| D      |   500 |
| M      |  1000 |

Certain values use subtractive notation:

* `4 = IV`
* `9 = IX`
* `40 = XL`
* `90 = XC`
* `400 = CD`
* `900 = CM`

### Example

```text
Input: 3749
Output: "MMMDCCXLIX"
```

---

## Approach

This solution uses a **greedy algorithm**.

The Roman numeral values are stored from largest to smallest, including all six subtractive combinations:

```text
1000  -> M
900   -> CM
500   -> D
400   -> CD
100   -> C
90    -> XC
50    -> L
40    -> XL
10    -> X
9     -> IX
5     -> V
4     -> IV
1     -> I
```

For each value:

1. Check whether it can be subtracted from `num`.
2. If it can, append its corresponding Roman symbol.
3. Subtract the value from `num`.
4. Continue until the value can no longer be used.

Because the values are processed from largest to smallest, the greedy approach naturally produces the correct Roman numeral representation.

---

## Implementation

```java
class Solution {
    public String intToRoman(int num) {
        final int[] values = {
            1000, 900, 500, 400,
            100, 90, 50, 40,
            10, 9, 5, 4, 1
        };

        final String[] symbols = {
            "M", "CM", "D", "CD",
            "C", "XC", "L", "XL",
            "X", "IX", "V", "IV", "I"
        };

        final StringBuilder result = new StringBuilder(15);

        for (int i = 0; i < values.length; i++) {
            while (num >= values[i]) {
                num -= values[i];
                result.append(symbols[i]);
            }
        }

        return result.toString();
    }
}
```

## Why It Works

Consider `1994`:

```text
1994 >= 1000 → M
Remaining: 994

994 >= 900 → CM
Remaining: 94

94 >= 90 → XC
Remaining: 4

4 >= 4 → IV
Remaining: 0
```

Result:

```text
MCMXCIV
```

The inclusion of the subtractive values (`900`, `400`, `90`, `40`, `9`, `4`) ensures that invalid forms such as `DCCCC` or `VIIII` are never generated.

---

## Complexity

There are only **13 fixed Roman numeral values**, and the input is bounded by `3999`.

* **Time:** `O(1)`
* **Space:** `O(1)`

The output itself requires at most a small constant amount of space.

---

## Key Takeaway

A **greedy approach** works because Roman numerals are constructed from the largest valid decimal-place representation down to the smallest.

Including the subtractive forms directly in the lookup arrays makes the conversion simple, readable, and efficient.
