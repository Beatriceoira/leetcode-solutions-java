# 18. 4Sum

[**LeetCode Problem**](https://leetcode.com/problems/4sum/)

**Difficulty:** Medium
**Language:** Java
**Topics:** Array, Sorting, Two Pointers

## Problem

Given an integer array `nums` and an integer `target`, return all unique quadruplets:

```text
[nums[a], nums[b], nums[c], nums[d]]
```

such that:

```text
nums[a] + nums[b] + nums[c] + nums[d] == target
```

The four indices must be distinct, and the result must not contain duplicate quadruplets.

The answer can be returned in any order.

### Example 1

```text
Input:
nums = [1,0,-1,0,-2,2]
target = 0

Output:
[[-2,-1,1,2],
 [-2,0,0,2],
 [-1,0,0,1]]
```

### Example 2

```text
Input:
nums = [2,2,2,2,2]
target = 8

Output:
[[2,2,2,2]]
```

## Approach

The solution uses:

1. **Sorting**
2. **Two fixed indices**
3. **Two pointers**
4. **Duplicate skipping**
5. **Early pruning**

After sorting the array, two values are fixed using `i` and `j`, while `left` and `right` search for the remaining two values.

The structure becomes:

```text
Sort
  ↓
Fix i
  ↓
Fix j
  ↓
left ─────────── right
```

For every combination of `i` and `j`, calculate:

```text
nums[i] + nums[j] + nums[left] + nums[right]
```

### If the sum is too small

Move `left` forward:

```java
left++;
```

Because the array is sorted, this increases the sum.

### If the sum is too large

Move `right` backward:

```java
right--;
```

Because the array is sorted, this decreases the sum.

### If the sum equals the target

Store the quadruplet and move both pointers while skipping duplicate values.

## Sorting

Sorting allows the two-pointer technique to work:

```text
[-2, -1, 0, 0, 1, 2]
```

For example:

```text
i = -2
j = -1

left = 0
right = 2
```

The sorted ordering lets us determine which pointer to move based on whether the current sum is smaller or larger than the target.

## Duplicate Handling

Duplicate `i` values are skipped:

```java
if (i > 0 && nums[i] == nums[i - 1]) {
    continue;
}
```

Duplicate `j` values are skipped:

```java
if (j > i + 1 && nums[j] == nums[j - 1]) {
    continue;
}
```

After finding a valid quadruplet, duplicate `left` and `right` values are skipped:

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

This prevents duplicate quadruplets from entering the result.

## Early Pruning

Because the array is sorted, many combinations can be eliminated without running the two-pointer search.

### Minimum possible sum for `i`

If the four smallest values available for the current `i` already exceed the target:

```java
if ((long) nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target) {
    break;
}
```

There is no reason to check later `i` values because they are even larger.

### Maximum possible sum for `i`

If even the four largest values available cannot reach the target:

```java
if ((long) nums[i] + nums[n - 3] + nums[n - 2] + nums[n - 1] < target) {
    continue;
}
```

The current `i` cannot produce a valid quadruplet.

### Minimum possible sum for `j`

The same principle is applied after fixing `j`:

```java
if ((long) nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target) {
    break;
}
```

### Maximum possible sum for `j`

```java
if ((long) nums[i] + nums[j] + nums[n - 2] + nums[n - 1] < target) {
    continue;
}
```

These bounds substantially reduce unnecessary pointer searches for many inputs.

## Integer Overflow

The input values can be as large as:

```text
10^9
```

Four values can therefore produce:

```text
4 × 10^9 = 4,000,000,000
```

This exceeds Java's `int` maximum:

```text
2,147,483,647
```

Therefore, the calculation uses `long`:

```java
long sum = (long) nums[i]
         + nums[j]
         + nums[left]
         + nums[right];
```

The pruning calculations also cast the first operand to `long`:

```java
(long) nums[i] + nums[j] + ...
```

This prevents overflow before the comparison is performed.

## Algorithm

```text
1. If nums contains fewer than four elements, return an empty result.
2. Sort nums.
3. Iterate through each possible first value i.
4. Skip duplicate i values.
5. Use minimum/maximum bounds to prune impossible i values.
6. Iterate through each possible second value j.
7. Skip duplicate j values.
8. Use minimum/maximum bounds to prune impossible j values.
9. Set:
      left = j + 1
      right = n - 1
10. While left < right:
      - Calculate the four-number sum.
      - If sum < target, move left.
      - If sum > target, move right.
      - If sum == target:
          - Add the quadruplet.
          - Skip duplicate left values.
          - Skip duplicate right values.
11. Return the result.
```

## Complexity

Let `n` be the number of elements.

### Time Complexity

Sorting requires:

```text
O(n log n)
```

The nested search requires:

```text
O(n³)
```

Therefore:

```text
Overall: O(n³)
```

The `O(n³)` search dominates the sorting cost.

### Space Complexity

The algorithm uses constant auxiliary space aside from the returned result:

```text
O(1) auxiliary space
```

The result itself requires:

```text
O(k)
```

where `k` is the number of unique quadruplets.

Therefore:

```text
Auxiliary Space: O(1)
Output Space:    O(k)
```

## Java Solution

```java
class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;

        if (n < 4) {
            return result;
        }

        Arrays.sort(nums);

        for (int i = 0; i < n - 3; i++) {
            // Skip duplicate first values.
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            // Minimum possible sum is already greater than target.
            if ((long) nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target) {
                break;
            }

            // Maximum possible sum is still smaller than target.
            if ((long) nums[i] + nums[n - 3] + nums[n - 2] + nums[n - 1] < target) {
                continue;
            }

            for (int j = i + 1; j < n - 2; j++) {
                // Skip duplicate second values.
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }

                // Minimum possible sum is already greater than target.
                if ((long) nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target) {
                    break;
                }

                // Maximum possible sum is still smaller than target.
                if ((long) nums[i] + nums[j] + nums[n - 2] + nums[n - 1] < target) {
                    continue;
                }

                int left = j + 1;
                int right = n - 1;

                while (left < right) {
                    long sum = (long) nums[i]
                             + nums[j]
                             + nums[left]
                             + nums[right];

                    if (sum < target) {
                        left++;
                    } else if (sum > target) {
                        right--;
                    } else {
                        result.add(Arrays.asList(
                            nums[i],
                            nums[j],
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
        }

        return result;
    }
}
```

## Key Takeaways

The optimized solution combines several techniques:

* **Sorting** enables ordered searching.
* **Two fixed indices** reduce the problem to a 2Sum search.
* **Two pointers** eliminate one nested loop.
* **Duplicate skipping** guarantees unique quadruplets.
* **Early pruning** eliminates impossible searches.
* **`long` arithmetic** prevents integer overflow.
* **Primitive array operations** keep auxiliary memory low.

Final complexity:

```text
Time:            O(n³)
Auxiliary Space: O(1)
Output Space:    O(k)
```

The `O(n³)` time complexity is the standard optimal asymptotic approach for the general 4Sum problem using sorting and two pointers.
