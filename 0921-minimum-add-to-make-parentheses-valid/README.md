# 921. Minimum Add to Make Parentheses Valid

[LeetCode Problem](https://leetcode.com/problems/minimum-add-to-make-parentheses-valid/)

## Problem

A parentheses string is valid if it is empty, can be split into two valid parentheses strings, or can be enclosed by a pair of parentheses containing a valid string.

Given a parentheses string `s`, you can insert either `'('` or `')'` at any position.

Return the **minimum number of insertions** required to make `s` valid.

### Example 1

**Input:**

```text
s = "())"
```

**Output:**

```text
1
```

### Example 2

**Input:**

```text
s = "((("
```

**Output:**

```text
3
```

## Approach

Use a **greedy balance counter** to track unmatched opening parentheses.

* When encountering `'('`, increment the `open` counter.
* When encountering `')'`:

  * If there is an unmatched `'('`, match it by decrementing `open`.
  * Otherwise, the `')'` has no matching opening parenthesis, so an `'('` must be inserted. Increment `additions`.
* After processing the entire string, any remaining unmatched `'('` requires a corresponding `')'`.

The final answer is:

```text
additions + open
```

### Why This Works

Every unmatched closing parenthesis requires exactly one inserted opening parenthesis.

Similarly, every unmatched opening parenthesis requires exactly one inserted closing parenthesis.

Therefore, the minimum number of insertions is simply the total number of unmatched parentheses.

## Algorithm

1. Initialize `open = 0` to track unmatched `'('`.
2. Initialize `additions = 0` to track required opening parentheses.
3. Traverse the string:

   * If the character is `'('`, increment `open`.
   * If the character is `')'` and `open > 0`, decrement `open`.
   * Otherwise, increment `additions`.
4. Return `additions + open`.

## Complexity

* **Time:** `O(n)`
* **Space:** `O(1)`

Where `n` is the length of the string.

## Java Implementation

```java
class Solution {
    public int minAddToMakeValid(String s) {
        int open = 0;
        int additions = 0;

        for (char c : s.toCharArray()) {
            if (c == '(') {
                open++;
            } else if (open > 0) {
                open--;
            } else {
                additions++;
            }
        }

        return additions + open;
    }
}
```

## Key Takeaway

A valid parentheses string must maintain a non-negative balance throughout the scan and finish with a balance of `0`.

By greedily matching every possible `')'` with a previous `'('`, the algorithm identifies exactly which parentheses need to be inserted without using a stack.
