# 1111. Maximum Nesting Depth of Two Valid Parentheses Strings

## Problem

Given a valid parentheses string `seq`, split its characters into two disjoint subsequences `A` and `B` such that:

* Both `A` and `B` are valid parentheses strings.
* Every character from `seq` belongs to exactly one of the two subsequences.
* `max(depth(A), depth(B))` is minimized.

Return an array where:

* `answer[i] = 0` if `seq[i]` belongs to `A`.
* `answer[i] = 1` if `seq[i]` belongs to `B`.

The subsequences do not need to be contiguous.

### Example 1

```text
Input:  seq = "(()())"
Output: [0,1,1,1,1,0]
```

### Example 2

```text
Input:  seq = "()(())()"
Output: [0,0,0,1,1,0,1,1]
```

## Approach

The goal is to distribute nested parentheses between the two subsequences as evenly as possible.

Maintain the current nesting `depth` while scanning the string.

For every parenthesis, assign it to a group based on the parity of the current depth:

```text
answer[i] = depth % 2
```

Using bitwise parity:

```text
answer[i] = depth & 1
```

This alternates deeply nested parentheses between the two groups.

For example, for:

```text
seq = "((()))"
```

The nesting depths are:

```text
1 2 3 3 2 1
```

The corresponding assignments are:

```text
1 0 1 1 0 1
```

Therefore, the nesting is distributed between `A` and `B` instead of concentrating all nested parentheses in one subsequence.

### Handling closing parentheses

For a closing parenthesis, the current depth still represents the nesting level of that parenthesis before it closes.

Therefore:

1. Assign the closing parenthesis using the current depth.
2. Decrease the depth afterward.

For an opening parenthesis:

1. Increase the depth.
2. Assign the parenthesis using the new depth.

This guarantees that matching opening and closing parentheses are assigned to the same subsequence.

## Algorithm

1. Initialize `depth = 0`.
2. Create an integer array `answer` of length `seq.length()`.
3. Iterate through every character in `seq`.
4. If the character is `'('`:

   * Increment `depth`.
   * Assign `depth & 1` to `answer[i]`.
5. If the character is `')'`:

   * Assign `depth & 1` to `answer[i]`.
   * Decrement `depth`.
6. Return `answer`.

## Implementation

```java
class Solution {
    public int[] maxDepthAfterSplit(String seq) {
        int n = seq.length();
        int[] answer = new int[n];

        int depth = 0;

        for (int i = 0; i < n; i++) {
            if (seq.charAt(i) == '(') {
                depth++;
                answer[i] = depth & 1;
            } else {
                answer[i] = depth & 1;
                depth--;
            }
        }

        return answer;
    }
}
```

## Correctness

At every nesting level, the algorithm alternates between the two subsequences.

For an opening parenthesis at depth `d`, it is assigned to:

```text
d % 2
```

Its corresponding closing parenthesis is encountered while the nesting depth is still `d`, so it receives the same assignment.

Therefore, each matching pair remains within the same subsequence, preserving the validity of both `A` and `B`.

Because the assignments alternate by depth, nested levels are distributed between the two subsequences. Thus, if the original string has maximum depth `D`, the two resulting subsequences have depths at most approximately half of `D`, which is the minimum possible maximum depth.

## Complexity

Let `n` be the length of `seq`.

* **Time:** `O(n)`
* **Space:** `O(n)` for the output array
* **Auxiliary Space:** `O(1)`

The solution performs exactly one pass through the string.

## Key Insight

The problem does not require explicitly constructing `A` and `B`.

The only information needed is the **current nesting depth**.

By alternating group assignments according to depth parity:

```java
answer[i] = depth & 1;
```

we split the nesting between the two subsequences as evenly as possible in a single pass.
