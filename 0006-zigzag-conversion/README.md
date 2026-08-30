# LeetCode #6 — Zigzag Conversion

## Problem

Given a string `s` and an integer `numRows`, write the characters of `s` in a **zigzag pattern** across the specified number of rows.

The result is then read **row by row** to produce the converted string.

For example:

```text
s = "PAYPALISHIRING"
numRows = 3
```

The zigzag pattern is:

```text
P   A   H   N
A P L S I I G
Y   I   R
```

Reading each row from left to right produces:

```text
PAHNAPLSIIGYIR
```


# Examples

## Example 1

```text
Input:
s = "PAYPALISHIRING"
numRows = 3

Output:
"PAHNAPLSIIGYIR"
```

The characters are arranged as:

```text
P   A   H   N
A P L S I I G
Y   I   R
```

Reading across the rows:

```text
Row 1 → PAHN
Row 2 → APLSIIG
Row 3 → YIR
```

Result:

```text
PAHNAPLSIIGYIR
```

---

## Example 2

```text
Input:
s = "PAYPALISHIRING"
numRows = 4

Output:
"PINALSIGYAHRPI"
```

The pattern becomes:

```text
P     I     N
A   L S   I G
Y A   H R
P     I
```

Reading row by row:

```text
P I N
A L S I G
Y A H R
P I
```

Result:

```text
PINALSIGYAHRPI
```

---

## Example 3

```text
Input:
s = "A"
numRows = 1

Output:
"A"
```

With only one row, no zigzag conversion is necessary.


# Approach

The easiest way to model the zigzag is to simulate moving through the rows.

We maintain:

```text
currentRow
```

and a direction:

```text
goingDown
```

For each character:

1. Add the character to the current row.
2. If we reach the top row, change direction to **down**.
3. If we reach the bottom row, change direction to **up**.
4. Move to the next row.

For example, with:

```text
numRows = 4
```

the row movement is:

```text
0
1
2
3
2
1
0
1
2
3
...
```

This repeats continuously.


# Important Edge Cases

## One Row

If:

```text
numRows = 1
```

there is no zigzag.

Therefore:

```java
return s;
```


## More Rows Than Characters

For example:

```text
s = "ABC"
numRows = 5
```

The string cannot actually occupy five rows.

The result is simply:

```text
ABC
```

Therefore, we can also immediately return `s` when:

```text
numRows >= s.length()
```


# Optimized Approach

Instead of constructing the physical zigzag grid, we only store the characters belonging to each row.

Use:

```java
StringBuilder[] rows
```

where each `StringBuilder` represents one row.

For:

```text
PAYPALISHIRING
```

with three rows:

```text
rows[0] → PAHN
rows[1] → APLSIIG
rows[2] → YIR
```

We then concatenate the rows.

This avoids storing unnecessary spaces or creating a two-dimensional grid.



# Algorithm

1. If `numRows == 1` or `numRows >= s.length()`, return `s`.
2. Create one `StringBuilder` for each row.
3. Start at row `0`.
4. Initially move downward.
5. For every character:

   * Append it to the current row.
   * If the top row is reached, change direction to downward.
   * If the bottom row is reached, change direction to upward.
   * Move to the next row.
6. Concatenate all rows.
7. Return the resulting string.



# Implementation

```java
class Solution {
    public String convert(String s, int numRows) {
        int n = s.length();

        if (numRows == 1 || numRows >= n) {
            return s;
        }

        StringBuilder[] rows = new StringBuilder[numRows];

        for (int i = 0; i < numRows; i++) {
            rows[i] = new StringBuilder();
        }

        int row = 0;
        int direction = 1;

        for (int i = 0; i < n; i++) {
            rows[row].append(s.charAt(i));

            if (row == 0) {
                direction = 1;
            } else if (row == numRows - 1) {
                direction = -1;
            }

            row += direction;
        }

        StringBuilder result = new StringBuilder(n);

        for (StringBuilder currentRow : rows) {
            result.append(currentRow);
        }

        return result.toString();
    }
}
```


# Example Walkthrough

Consider:

```text
s = "PAYPALISHIRING"
numRows = 3
```

Initially:

```text
row = 0
direction = 1
```

The characters are distributed like this:

```text
P → row 0
A → row 1
Y → row 2
P → row 1
A → row 0
L → row 1
I → row 2
S → row 1
H → row 0
I → row 1
R → row 2
I → row 1
N → row 0
G → row 1
```

The resulting rows are:

```text
Row 0: PAHN
Row 1: APLSIIG
Row 2: YIR
```

Concatenating:

```text
PAHN + APLSIIG + YIR
```

produces:

```text
PAHNAPLSIIGYIR
```



# Direction Handling

The direction can be represented with an integer:

```text
1  = down
-1 = up
```

At the top:

```text
row == 0
```

we move down:

```text
direction = 1
```

At the bottom:

```text
row == numRows - 1
```

we move up:

```text
direction = -1
```

This produces the repeating movement:

```text
0 → 1 → 2 → 3 → 2 → 1 → 0 → 1 → 2 → ...
```


# Complexity

Let:

```text
n = s.length()
```

### Time Complexity

Every character is processed exactly once.

Appending each character is amortized `O(1)`.

The final concatenation also processes each character once.

Therefore:

```text
Time: O(n)
```



### Space Complexity

The output itself requires `O(n)` space.

The row builders collectively contain every character:

```text
O(n)
```

Therefore:

```text
Space: O(n)
```

This is effectively optimal because the method must produce a new string containing `n` characters.



# Optimization

This implementation avoids several unnecessary operations:

* No 2D character matrix
* No padding spaces
* No coordinate calculations
* No repeated string concatenation with `+`
* No sorting
* No recursion
* No simulation of empty cells

Instead, it directly distributes characters into their final output rows.

The result is:

```text
Time:  O(n)
Space: O(n)
```

Since every input character must be processed and the output itself has `n` characters, this is asymptotically optimal.


# Alternative Mathematical Approach

The zigzag pattern has a repeating cycle.

For `numRows > 1`, the cycle length is:

```text
cycleLength = 2 * numRows - 2
```

For example, with four rows:

```text
0
1
2
3
2
1
```

The cycle length is:

```text
2 × 4 - 2 = 6
```

A mathematical implementation can process characters row-by-row using this cycle instead of explicitly tracking direction.

However, the direction-based implementation is generally:

* Easier to understand
* Easier to maintain
* Less error-prone
* Still `O(n)`
* Already optimal asymptotically



# Key Concepts

* String Manipulation
* Simulation
* StringBuilder
* Array of StringBuilders
* Direction Tracking
* Edge Case Handling
* Time Complexity Optimization
* Space Complexity



# Language

**Java**


## LeetCode

[LeetCode #6 — Zigzag Conversion](https://leetcode.com/problems/zigzag-conversion/description/)
