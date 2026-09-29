# 15. 3Sum

[**LeetCode Problem**](https://leetcode.com/problems/3sum/)

**Difficulty:** Medium
**Language:** Java
**Topic:** Array, Two Pointers, Sorting

## Problem

Given an integer array `nums`, return all unique triplets `[nums[i], nums[j], nums[k]]` such that:

```text
i != j
i != k
j != k
```

and:

```text
nums[i] + nums[j] + nums[k] == 0
```

The solution must not contain duplicate triplets.

### Example 1

```text
Input: nums = [-1,0,1,2,-1,-4]

Output:
[[-1,-1,2],[-1,0,1]]
```

### Example 2

```text
Input: nums = [0,1,1]

Output:
[]
```

### Example 3

```text
Input: nums = [0,0,0]

Output:
[[0,0,0]]
```

## Approach

The solution uses **sorting + two pointers**.

First, sort the array:

```text
[-1, 0, 1, 2, -1, -4]
        ↓
[-4, -1, -1, 0, 1, 2]
```

For each index `i`, treat `nums[i]` as the first element of the triplet. Then use two pointers:

* `left = i + 1`
* `right = nums.length - 1`

Calculate:

```text
nums[i] + nums[left] + nums[right]
```

### If the sum is too small

Move `left` forward:

```java
left++;
```

Because the array is sorted, increasing `left` increases the sum.

### If the sum is too large

Move `right` backward:

```java
right--;
```

Because the array is sorted, decreasing `right` decreases the sum.

### If the sum is zero

Add the triplet to the result and skip duplicate values on both sides.

## Duplicate Handling

Duplicate first elements are skipped:

```java
if (i > 0 && a == nums[i - 1]) {
    continue;
}
```

After finding a valid triplet, duplicate `left` and `right` values are skipped:

```java
int leftValue = nums[left];
int rightValue = nums[right];

while (left < right && nums[left] == leftValue) {
    left++;
}

while (left < right && nums[right] == rightValue) {
    right--;
}
```

This guarantees that the result contains only unique triplets.

## Early Pruning

Because the array is sorted, the search can be terminated or skipped when a solution is impossible.

### Largest possible sum is negative

If the current value plus the two largest remaining values is still less than zero:

```java
if (a + nums[n - 2] + nums[n - 1] < 0) {
    continue;
}
```

There is no possible combination for this `a`.

### Smallest possible sum is positive

If the three smallest available values already exceed zero:

```java
if (a + nums[i + 1] + nums[i + 2] > 0) {
    break;
}
```

Since subsequent values are even larger, no later iteration can produce zero.

### Positive first element

If:

```java
a > 0
```

then every remaining value is also positive, so a sum of zero is impossible:

```java
if (a > 0) {
    break;
}
```

## Complexity

Let `n` be the number of elements and `k` be the number of triplets returned.

### Time

Sorting:

```text
O(n log n)
```

Two-pointer traversal:

```text
O(n²)
```

Overall:

```text
O(n²)
```

The `O(n²)` search dominates the sorting cost.

### Space

The algorithm uses constant auxiliary space:

```text
O(1)
```

The returned result itself requires:

```text
O(k)
```

space.

Therefore:

```text
Auxiliary Space: O(1)
Output Space:    O(k)
```

## Java Solution

```java
class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;

        Arrays.sort(nums);

        for (int i = 0; i < n - 2; i++) {
            int a = nums[i];

            // Skip duplicate first values.
            if (i > 0 && a == nums[i - 1]) {
                continue;
            }

            // All remaining values are positive.
            if (a > 0) {
                break;
            }

            // Even the two largest values cannot reach 0.
            if (a + nums[n - 2] + nums[n - 1] < 0) {
                continue;
            }

            // Even the three smallest values exceed 0.
            if (a + nums[i + 1] + nums[i + 2] > 0) {
                break;
            }

            int left = i + 1;
            int right = n - 1;

            while (left < right) {
                int sum = a + nums[left] + nums[right];

                if (sum < 0) {
                    left++;
                } else if (sum > 0) {
                    right--;
                } else {
                    result.add(Arrays.asList(
                        a,
                        nums[left],
                        nums[right]
                    ));

                    int leftValue = nums[left];
                    int rightValue = nums[right];

                    while (left < right && nums[left] == leftValue) {
                        left++;
                    }

                    while (left < right && nums[right] == rightValue) {
                        right--;
                    }
                }
            }
        }

        return result;
    }
}
```

## Key Takeaway

The key optimization is transforming the problem from a brute-force `O(n³)` search into an `O(n²)` **sorted two-pointer search**.

The additional early-pruning conditions reduce unnecessary iterations while keeping the algorithm at:

```text
Time:  O(n²)
Space: O(1) auxiliary
```
