# LeetCode 3903 — Smallest Stable Index I

## Problem

You are given an integer array `nums` of length `n` and an integer `k`.

For every index `i`, define its instability score as:

```text
max(nums[0..i]) - min(nums[i..n-1])
```

An index `i` is considered stable if its instability score is less than or equal to `k`.

Return the smallest stable index.

If no stable index exists, return `-1`.



## Examples

### Example 1

```text
Input:
nums = [5,0,1,4]
k = 3

Output:
3
```

At each index:

```text
i = 0 → max([5])       - min([5,0,1,4]) = 5 - 0 = 5
i = 1 → max([5,0])     - min([0,1,4])   = 5 - 0 = 5
i = 2 → max([5,0,1])   - min([1,4])     = 5 - 1 = 4
i = 3 → max([5,0,1,4]) - min([4])       = 5 - 4 = 1
```

Since `1 <= 3`, index `3` is stable.

Therefore:

```text
Output: 3
```



### Example 2

```text
Input:
nums = [3,2,1]
k = 1

Output:
-1
```

Every index has an instability score of `2`, which is greater than `1`.

Therefore, no stable index exists.



### Example 3

```text
Input:
nums = [0]
k = 0

Output:
0
```

The only index has:

```text
0 - 0 = 0
```

which satisfies:

```text
0 <= 0
```



# Approach

A direct solution would calculate the maximum on the left and minimum on the right for every index.

Doing this independently would require repeatedly scanning portions of the array and could lead to `O(n²)` time.

Instead, precompute all suffix minimums.

We maintain:

1. `suffixMin[i]` — minimum value from index `i` through `n - 1`
2. `prefixMax` — maximum value from index `0` through the current index

Then the instability score at index `i` is immediately:

```text
prefixMax - suffixMin[i]
```

If this value is at most `k`, we have found the smallest stable index.



# Step 1 — Build the Suffix Minimum Array

Define:

```text
suffixMin[i] = min(nums[i], nums[i+1], ..., nums[n-1])
```

Start from the right:

```java
suffixMin[n - 1] = nums[n - 1];
```

Then:

```java
for (int i = n - 2; i >= 0; i--) {
    suffixMin[i] = Math.min(nums[i], suffixMin[i + 1]);
}
```

For example:

```text
nums       = [5, 0, 1, 4]

suffixMin = [0, 0, 1, 4]
```

So:

```text
suffixMin[0] = 0
suffixMin[1] = 0
suffixMin[2] = 1
suffixMin[3] = 4
```



# Step 2 — Maintain the Prefix Maximum

Scan the array from left to right.

Initially:

```java
int prefixMax = nums[0];
```

At every index:

```java
prefixMax = Math.max(prefixMax, nums[i]);
```

This gives:

```text
max(nums[0..i])
```

without rescanning the prefix.

For:

```text
nums = [5,0,1,4]
```

the prefix maximum values are:

```text
i = 0 → 5
i = 1 → 5
i = 2 → 5
i = 3 → 5
```



# Step 3 — Check Stability

At index `i`, the instability score is:

```text
prefixMax - suffixMin[i]
```

Therefore:

```java
if ((long) prefixMax - suffixMin[i] <= k) {
    return i;
}
```

Because we scan from left to right, the first index satisfying the condition is automatically the smallest stable index.

If the loop finishes without finding one:

```java
return -1;
```



# Algorithm

1. Create a `suffixMin` array.
2. Fill it from right to left.
3. Initialize `prefixMax`.
4. Scan `nums` from left to right.
5. Update `prefixMax`.
6. Calculate the instability score:

   ```text
   prefixMax - suffixMin[i]
   ```
7. If the score is `<= k`, immediately return `i`.
8. If no index satisfies the condition, return `-1`.


# Walkthrough

Consider:

```text
nums = [5,0,1,4]
k = 3
```

### Suffix minimums

```text
nums:
        5  0  1  4

suffix:
        0  0  1  4
```

### Scan from left to right

#### Index 0

```text
prefixMax = 5
suffixMin = 0

score = 5 - 0 = 5
```

Since:

```text
5 > 3
```

index `0` is not stable.



#### Index 1

```text
prefixMax = 5
suffixMin = 0

score = 5 - 0 = 5
```

Not stable.



#### Index 2

```text
prefixMax = 5
suffixMin = 1

score = 5 - 1 = 4
```

Not stable.



#### Index 3

```text
prefixMax = 5
suffixMin = 4

score = 5 - 4 = 1
```

Since:

```text
1 <= 3
```

index `3` is stable.

The algorithm immediately returns:

```text
3
```



# Why This Works

For every index `i`:

```text
prefixMax = max(nums[0..i])
```

because it is continuously updated with the maximum value encountered from the beginning of the array.

Similarly:

```text
suffixMin[i] = min(nums[i..n-1])
```

because the suffix array is constructed from right to left.

Therefore:

```text
prefixMax - suffixMin[i]
```

is exactly the instability score defined by the problem.

Since indices are examined in increasing order, the first index whose score is at most `k` must be the smallest stable index.



# Complete Implementation

```java
class Solution {
    public int firstStableIndex(int[] nums, int k) {
        int n = nums.length;

        // suffixMin[i] = minimum value from i to n - 1
        int[] suffixMin = new int[n];
        suffixMin[n - 1] = nums[n - 1];

        for (int i = n - 2; i >= 0; i--) {
            suffixMin[i] = Math.min(nums[i], suffixMin[i + 1]);
        }

        int prefixMax = nums[0];

        for (int i = 0; i < n; i++) {
            prefixMax = Math.max(prefixMax, nums[i]);

            if ((long) prefixMax - suffixMin[i] <= k) {
                return i;
            }
        }

        return -1;
    }
}
```



# Complexity

Let `n` be the length of `nums`.

### Time Complexity

Building the suffix minimum array:

```text
O(n)
```

Scanning the array:

```text
O(n)
```

Therefore:

```text
Time: O(n)
```

### Space Complexity

The suffix minimum array contains `n` elements:

```text
Space: O(n)
```



# Optimization

The naive approach might do something similar to:

```text
for every i:
    find max(nums[0..i])
    find min(nums[i..n-1])
```

This repeatedly scans the array and can result in:

```text
O(n²)
```

operations.

This implementation reduces the work to two linear passes:

```text
Right → Left:
    compute suffix minimums

Left → Right:
    maintain prefix maximum
    check stability
```

Result:

```text
O(n²) → O(n)
```

The algorithm is already asymptotically optimal because every element must potentially be inspected.



# Key Concepts

- Prefix Maximum
- Suffix Minimum
- Prefix/Suffix Preprocessing
- Linear Scan
- Greedy Early Return
- Array Traversal
- Time Complexity Optimization



## Language

Java

## LeetCode

Problem: 3903 — Smallest Stable Index I (https://leetcode.com/problems/smallest-stable-index-i/description/?envType=daily-question&envId=2026-09-04)
Difficulty: Easy
