# 26. Remove Duplicates from Sorted Array

Link - https://leetcode.com/problems/remove-duplicates-from-sorted-array/editorial/

## Problem

Given an integer array `nums` sorted in non-decreasing order, remove the duplicates **in-place** so that each unique element appears only once.

The relative order of the elements must be preserved.

Return `k`, the number of unique elements.

The first `k` elements of `nums` must contain the unique values in sorted order. Elements after index `k - 1` can be ignored.

## Approach

Since the array is already sorted, duplicate values are always adjacent.

Use a two-pointer technique:

* `i` scans through the array.
* `k` tracks the position where the next unique value should be written.
* `prev` stores the most recently accepted unique value.

When the current value differs from `prev`, it is a new unique element. Write it to `nums[k]`, increment `k`, and update `prev`.

### Example

```text
Input:
nums = [0,0,1,1,1,2,2,3,3,4]

Process:
0 → unique
0 → duplicate
1 → unique
1 → duplicate
1 → duplicate
2 → unique
2 → duplicate
3 → unique
3 → duplicate
4 → unique

Result:
nums = [0,1,2,3,4,_,_,_,_,_]
k = 5
```

## Java Solution

```java
class Solution {
    public int removeDuplicates(int[] nums) {
        int k = 1;
        int prev = nums[0];

        for (int i = 1; i < nums.length; i++) {
            int curr = nums[i];

            if (curr != prev) {
                nums[k++] = curr;
                prev = curr;
            }
        }

        return k;
    }
}
```

## Complexity Analysis

* **Time:** `O(n)`
* **Space:** `O(1)`

The array is traversed once, and no additional data structures are required.

## Key Insight

Because the input array is sorted, there is no need for a `HashSet` or another data structure to track duplicates.

Every value can be compared directly with the previously accepted unique value, allowing duplicates to be removed in-place using constant extra space.

## Edge Cases

### Single Element

```text
Input:
[1]

Output:
k = 1
nums = [1]
```

### All Elements Are Duplicates

```text
Input:
[2,2,2,2]

Output:
k = 1
nums = [2,_,_,_]
```

### No Duplicates

```text
Input:
[1,2,3,4]

Output:
k = 4
nums = [1,2,3,4]
```

## Complexity Summary

| Metric                | Complexity |
| --------------------- | ---------- |
| Time                  | `O(n)`     |
| Space                 | `O(1)`     |
| In-place              | Yes        |
| Extra Data Structures | None       |
