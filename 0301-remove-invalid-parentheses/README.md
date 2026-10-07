# 301. Remove Invalid Parentheses

Link - https://leetcode.com/problems/remove-invalid-parentheses/description/?envType=daily-question&envId=2026-10-07

## Problem

Given a string `s` containing parentheses and lowercase English letters, remove the **minimum number of invalid parentheses** to make the string valid.

Return all possible valid strings that can be obtained using the minimum number of removals.

The result must contain **unique strings**, and the strings may be returned in any order.

## Approach

Use a **two-pass depth-first search**.

A parentheses string can become invalid in two ways:

1. There are too many closing parentheses `)`.
2. There are too many opening parentheses `(`.

Instead of trying every possible removal, the algorithm removes only parentheses that are proven to be invalid.

### Step 1: Remove Invalid `)`

Scan the string from left to right while maintaining a balance:

* `(` increases the balance.
* `)` decreases the balance.

If the balance becomes negative, there is an invalid `)`.

We then try removing each possible `)` in that invalid range.

Consecutive identical parentheses are skipped when choosing the removal position to prevent duplicate results.

### Step 2: Remove Invalid `(`

After all invalid `)` have been removed, the string may still contain unmatched `(`.

Reverse the string and swap the roles of `(` and `)`.

The same DFS logic can then remove the remaining invalid opening parentheses.

This allows the same algorithm to handle both types of imbalance.

## Example

```text
Input:
s = "()())()"
```

There is one extra closing parenthesis:

```text
()())()
    ^
```

Removing the appropriate `)` produces:

```text
(())()
()()()
```

Therefore:

```text
Output:
["(())()", "()()()"]
```

## Java Solution

```java
class Solution {
    private final List<String> result = new ArrayList<>();

    public List<String> removeInvalidParentheses(String s) {
        remove(s, 0, 0, '(', ')');
        return result;
    }

    private void remove(
        String s,
        int scanStart,
        int removeStart,
        char open,
        char close
    ) {
        int balance = 0;

        for (int i = scanStart; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == open) {
                balance++;
            } else if (c == close) {
                balance--;
            }

            if (balance >= 0) {
                continue;
            }

            for (int j = removeStart; j <= i; j++) {
                if (s.charAt(j) == close &&
                    (j == removeStart || s.charAt(j - 1) != close)) {

                    String next =
                        s.substring(0, j) + s.substring(j + 1);

                    remove(
                        next,
                        i,
                        j,
                        open,
                        close
                    );
                }
            }

            return;
        }

        String reversed = new StringBuilder(s).reverse().toString();

        if (open == '(') {
            remove(reversed, 0, 0, ')', '(');
        } else {
            result.add(reversed);
        }
    }
}
```

## Algorithm Walkthrough

For:

```text
s = "()())()"
```

### First pass

We scan for invalid `)`:

```text
(  → balance = 1
)  → balance = 0
(  → balance = 1
)  → balance = 0
)  → balance = -1  ← invalid
```

At this point, one `)` must be removed.

The DFS explores the possible `)` removal positions while skipping consecutive duplicates.

This produces candidates such as:

```text
(())()
()()()
```

### Second pass

Each candidate is checked for unmatched `(`.

If unmatched opening parentheses remain, the string is reversed and the same procedure is performed with the parenthesis roles swapped.

When both passes complete successfully, the resulting string is valid and added to `result`.

## Duplicate Handling

Duplicate results are avoided during DFS with:

```java
if (s.charAt(j) == close &&
    (j == removeStart || s.charAt(j - 1) != close))
```

This prevents removing multiple equivalent consecutive parentheses at the same recursion level.

For example:

```text
()())()
```

contains consecutive `)` characters. Removing either identical character can produce the same resulting string, so only the first removal choice is explored.

## Why Two Passes?

The key insight is that once the scan encounters:

```text
balance < 0
```

the current `)` is definitely invalid.

After removing all excess `)`, the only possible remaining imbalance is excess `(`.

Reversing the string transforms the problem:

```text
Original:
too many '('

Reversed:
too many ')'
```

Therefore, the same removal algorithm can solve both cases.

## Edge Cases

### Already Valid

```text
Input:
"()"

Output:
["()"]
```

No removals are necessary.

### All Closing Parentheses

```text
Input:
"))"

Output:
[""]
```

Both parentheses must be removed.

### All Opening Parentheses

```text
Input:
"((("

Output:
[""]
```

All opening parentheses must be removed.

### Mixed Invalid Parentheses

```text
Input:
")("

Output:
[""]
```

Both parentheses are unmatched.

### Letters Only

```text
Input:
"abc"

Output:
["abc"]
```

Letters do not affect the parentheses balance and are always preserved.

## Complexity Analysis

Let `n` be the length of the string.

* **Time:** `O(2^n)` in the worst case
* **Auxiliary Space:** `O(n)` recursion depth, excluding generated results
* **Output Space:** `O(k · n)`, where `k` is the number of valid minimum-removal results

The exponential worst case is inherent because the problem may require returning exponentially many valid strings.

## Key Takeaways

* Calculate invalid parentheses through a running balance.
* Remove only parentheses that are proven to be invalid.
* Use DFS to explore minimum-removal possibilities.
* Skip consecutive identical removal choices to prevent duplicates.
* Reverse the string and swap parenthesis roles to handle unmatched `(`.
* Avoid brute-forcing every possible subset of parentheses.

## Complexity Summary

| Metric             | Complexity                  |
| ------------------ | --------------------------- |
| Time               | `O(2^n)` worst case         |
| Auxiliary Space    | `O(n)`                      |
| Output Space       | `O(k · n)`                  |
| Approach           | DFS + pruning               |
| Duplicate Handling | Consecutive-removal pruning |
| Minimum Removals   | Guaranteed                  |
| Valid Results      | All unique solutions        |
