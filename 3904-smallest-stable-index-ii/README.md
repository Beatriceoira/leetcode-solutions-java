# 3904. Smallest Stable Index II

## Problem

Given an integer array `nums` of length `n` and an integer `k`, define the instability score of an index `i` as:

```text
max(nums[0..i]) - min(nums[i..n-1])
```

An index is stable if its instability score is less than or equal to `k`.

Return the smallest stable index. If no stable index exists, return `-1`.

## Examples

### Example 1

```text
Input:
nums = [5,0,1,4]
k = 3

Output:
3
```

Explanation:

```text
i = 0:
max([5]) - min([5,0,1,4]) = 5 - 0 = 5

i = 1:
max([5,0]) - min([0,1,4]) = 5 - 0 = 5

i = 2:
max([5,0,1]) - min([1,4]) = 5 - 1 = 4

i = 3:
max([5,0,1,4]) - min([4]) = 5 - 4 = 1
```

Since `1 <= 3`, index `3` is the first stable index.

### Example 2

```text
Input:
nums = [3,2,1]
k = 1

Output:
-1
```

Every index has instability score `2`, which is greater than `1`.

### Example 3

```text
Input:
nums = [0]
k = 0

Output:
0
```

The instability score is:

```text
0 - 0 = 0
```

Therefore, index `0` is stable.

## Approach

The instability score requires two range queries for every index:

```text
max(nums[0..i])
min(nums[i..n-1])
```

Recomputing either range independently for every index would result in `O(n²)` time.

Instead, precompute all suffix minimums and maintain the prefix maximum while scanning from left to right.

### 1. Suffix Minimum

Create:

```text
min[i] = minimum value in nums[i..n-1]
```

Build it from right to left.

For example:

```text
nums = [5, 0, 1, 4]

min[3] = 4
min[2] = min(1, 4) = 1
min[1] = min(0, 1) = 0
min[0] = min(5, 0) = 0

min = [0, 0, 1, 4]
```

### 2. Prefix Maximum

While scanning from left to right, maintain:

```text
mx = max(nums[0..i])
```

At every index, check:

```text
mx - min[i] <= k
```

The first index satisfying this condition is the answer.

## Optimization

Instead of writing:

```java
if ((long) mx - min[i] <= k)
```

we can rearrange the inequality:

```text
mx - min[i] <= k
```

into:

```text
mx <= min[i] + k
```

Because the constraints guarantee:

```text
0 <= nums[i] <= 10^9
0 <= k <= 10^9
```

we have:

```text
min[i] + k <= 2 * 10^9
```

which fits safely inside Java's signed `int` range.

This allows the hot loop to use only integer arithmetic.

## Algorithm

1. Let `n = nums.length`.
2. Allocate an `int[] min` for suffix minimums.
3. Set `min[n - 1] = nums[n - 1]`.
4. Traverse from right to left:

   * Maintain the current minimum.
   * Store it in `min[i]`.
5. Set the prefix maximum to `0`.
6. Traverse from left to right:

   * Update the prefix maximum.
   * Check whether:

     ```text
     prefixMax <= min[i] + k
     ```
   * If true, return `i`.
7. If no index is stable, return `-1`.

## Walkthrough

For:

```text
nums = [5,0,1,4]
k = 3
```

First construct the suffix minimum array:

```text
nums:      [5, 0, 1, 4]
suffixMin: [0, 0, 1, 4]
```

Now scan from left to right.

### Index 0

```text
prefixMax = 5
suffixMin = 0

5 <= 0 + 3
5 <= 3  → false
```

### Index 1

```text
prefixMax = 5
suffixMin = 0

5 <= 0 + 3
5 <= 3  → false
```

### Index 2

```text
prefixMax = 5
suffixMin = 1

5 <= 1 + 3
5 <= 4  → false
```

### Index 3

```text
prefixMax = 5
suffixMin = 4

5 <= 4 + 3
5 <= 7  → true
```

Therefore:

```text
answer = 3
```

## Correctness

For every index `i`:

```text
prefixMax = max(nums[0..i])
```

because the prefix maximum is updated with every element encountered during the left-to-right scan.

Similarly:

```text
min[i] = min(nums[i..n-1])
```

because the suffix-minimum array is constructed from right to left.

Therefore the actual instability score at index `i` is:

```text
prefixMax - min[i]
```

The index is stable exactly when:

```text
prefixMax - min[i] <= k
```

which is equivalent to:

```text
prefixMax <= min[i] + k
```

The algorithm scans indices from `0` upward and immediately returns the first index satisfying this condition.

Therefore, the returned index is exactly the smallest stable index.

## Complete Implementation

```java
class Solution {
    public int firstStableIndex(int[] nums, int k) {
        final int n = nums.length;
        final int[] min = new int[n];

        // Build suffix minimum.
        int m = nums[n - 1];
        min[n - 1] = m;

        for (int i = n - 2; i >= 0; i--) {
            final int v = nums[i];
            if (v < m) m = v;
            min[i] = m;
        }

        // Find first stable index.
        int mx = 0;

        for (int i = 0; i < n; i++) {
            final int v = nums[i];
            if (v > mx) mx = v;

            if (mx <= min[i] + k) {
                return i;
            }
        }

        return -1;
    }
}
```

## Complexity

Let `n = nums.length`.

### Time Complexity

```text
O(n)
```

There are two linear scans:

1. Build the suffix minimum array.
2. Find the first stable index.

Therefore:

```text
O(n) + O(n) = O(n)
```

### Space Complexity

```text
O(n)
```

The only auxiliary data structure is the `int[] min` array.

## Why Not O(n²)?

A naive implementation could calculate:

```text
max(nums[0..i])
min(nums[i..n-1])
```

independently for every `i`.

That would repeatedly scan the same elements and result in:

```text
O(n²)
```

The suffix-minimum preprocessing eliminates this repeated work.


## Performance Optimizations

This implementation is designed for low runtime overhead:

- O(n) optimal asymptotic runtime.
- Uses a single primitive `int[]`.
- No objects or collections.
- No sorting.
- No streams.
- No boxing/unboxing.
- Direct comparisons instead of `Math.min()` / `Math.max()`.
- Uses `int` arithmetic safely under the problem constraints.
- Returns immediately when the first stable index is found.
- Uses sequential array access, which is cache-friendly.
- Only two linear passes over the input.

The algorithm is therefore already near the practical performance ceiling for an unmodified-input `O(n)` solution.

## Key Concepts

- Prefix maximum
- Suffix minimum
- Precomputation
- Range queries
- Greedy left-to-right search
- Array optimization
- Constant-time index evaluation


## Language
Java

## LeetCode

3904 — Smallest Stable Index II(https://leetcode.com/problems/smallest-stable-index-ii/description/?envType=daily-question&envId=2026-09-05)