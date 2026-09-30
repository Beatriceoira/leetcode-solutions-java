# 17. Letter Combinations of a Phone Number

## Link
https://leetcode.com/problems/letter-combinations-of-a-phone-number/description/

## Problem

Given a string containing digits from `2` to `9`, return all possible letter combinations that the digits could represent.

The digit-to-letter mapping follows a standard telephone keypad:

```text
2 → abc
3 → def
4 → ghi
5 → jkl
6 → mno
7 → pqrs
8 → tuv
9 → wxyz
```

Return the combinations in any order.

### Example 1

```text
Input:  digits = "23"
Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
```

### Example 2

```text
Input:  digits = "2"
Output: ["a","b","c"]
```

## Approach

This problem can be solved using **backtracking**.

Each digit represents a set of possible letters. We choose one letter for the current digit, recursively process the next digit, and undo the choice when returning from the recursive call.

For example:

```text
digits = "23"

        ""
      / | \
     a  b  c
   / | \ ...
  ad ae af
```

Each path from the root to a leaf represents one complete combination.

## Backtracking Process

For every digit:

1. Look up its corresponding letters.
2. Choose one letter.
3. Store it at the current position.
4. Recursively process the next digit.
5. Once a complete combination is formed, add it to the result.
6. The next iteration overwrites the current position with another letter.

A reusable `char[]` is used to construct combinations efficiently:

```java
char[] current = new char[digits.length()];
```

This avoids repeatedly creating intermediate strings during recursion.

A new `String` is created only when a complete combination is ready to be added to the result.

## Algorithm

1. Create a mapping from digits to their corresponding letters.
2. Return an empty list if `digits` is empty.
3. Create a `char[]` with the same length as `digits`.
4. Start a recursive backtracking process at index `0`.
5. At each recursive level:

   * Get the letters associated with the current digit.
   * Try each available letter.
   * Store the letter in `current[index]`.
   * Recurse to `index + 1`.
6. When `index == digits.length()`, convert `current` into a `String` and add it to the result.
7. Return the result.

## Implementation

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    private static final String[] MAP = {
        "", "", "abc", "def", "ghi",
        "jkl", "mno", "pqrs", "tuv", "wxyz"
    };

    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();

        if (digits.isEmpty()) {
            return result;
        }

        char[] current = new char[digits.length()];
        backtrack(digits, 0, current, result);

        return result;
    }

    private void backtrack(
        String digits,
        int index,
        char[] current,
        List<String> result
    ) {
        if (index == digits.length()) {
            result.add(new String(current));
            return;
        }

        String letters = MAP[digits.charAt(index) - '0'];

        for (int i = 0; i < letters.length(); i++) {
            current[index] = letters.charAt(i);
            backtrack(digits, index + 1, current, result);
        }
    }
}
```

## Correctness

At each position in `digits`, the algorithm considers every letter that the corresponding phone key represents.

For each possible letter:

* It places that letter at the current position.
* It recursively generates every possible combination for the remaining digits.

When the recursion reaches the end of the input, every position in `current` contains a valid letter choice, so the resulting string is one valid combination.

Because every possible letter is considered at every digit position, all possible combinations are generated exactly once.

## Complexity

Let:

* `n` = number of digits
* `k` = maximum number of letters mapped to a digit

For this problem, `k ≤ 4`.

There are at most:

```text
4ⁿ
```

possible combinations.

Each combination contains `n` characters.

### Time

```text
O(n × 4ⁿ)
```

The `n` factor comes from constructing each resulting string.

### Space

```text
O(n × 4ⁿ)
```

for the returned combinations, plus:

```text
O(n)
```

for the recursion stack and reusable character array.

The output space is unavoidable because every valid combination must be returned.

## Key Insight

The problem forms a **Cartesian product** of the letters represented by each digit.

For:

```text
"23"
```

we have:

```text
abc × def
```

which produces:

```text
ad ae af
bd be bf
cd ce cf
```

Backtracking explores this Cartesian product systematically while using only `O(n)` auxiliary space for the current combination.

Using a reusable `char[]` instead of repeatedly concatenating strings also minimizes unnecessary intermediate object creation.
