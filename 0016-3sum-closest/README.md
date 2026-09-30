# 16. 3Sum Closest

## Link
https://leetcode.com/problems/3sum-closest/description/

## Problem

Given an integer array `nums` and an integer `target`, find three integers at distinct indices whose sum is closest to `target`.

Return the sum of those three integers.

Each input is guaranteed to have exactly one solution.

### Example 1

```text
Input:  nums = [-1,2,1,-4], target = 1
Output: 2
```

Explanation:

```text
-1 + 2 + 1 = 2
```

The sum `2` is closest to the target `1`.

### Example 2

```text
Input:  nums = [0,0,0], target = 1
Output: 0
```

## Approach

The brute-force approach checks every possible combination of three elements, resulting in `O(n³)` time.

A more efficient solution uses:

1. **Sorting**
2. **Two pointers**
3. **Early pruning**
4. **Duplicate skipping**

After sorting the array, fix one element at index `i` and search for the remaining two elements using `left` and `right` pointers.

```text
i → left → ... → right
```

For every combination:

* If the sum is smaller than `target`, move `left` forward.
* If the sum is larger than `target`, move `right` backward.
* If the sum equals `target`, return immediately.

Because the array is sorted, each pointer movement eliminates a range of impossible combinations.

## Optimization 1: Track the Closest Sum

Initialize:

```java
int closest = nums[0] + nums[1] + nums[2];
```

For every candidate sum:

```java
int difference = sum - target;
```

If its absolute difference from the target is smaller than the current best:

```java
if (Math.abs(difference) < Math.abs(closest - target)) {
    closest = sum;
}
```

update `closest`.

## Optimization 2: Skip Duplicate Fixed Values

If the same value has already been used as the fixed element:

```java
if (i > 0 && nums[i] == nums[i - 1]) {
    continue;
}
```

there is no need to repeat the same two-pointer search.

This reduces unnecessary work without affecting the result.

## Optimization 3: Minimum Sum Pruning

Because the array is sorted, the smallest possible sum for a particular `i` is:

```java
int minSum = nums[i] + nums[i + 1] + nums[i + 2];
```

If:

```text
minSum > target
```

then every other combination using this `i` will also be greater than the target.

We only need to check whether `minSum` itself improves the current answer, then stop processing larger `i` values.

## Optimization 4: Maximum Sum Pruning

The largest possible sum for a particular `i` is:

```java
int maxSum = nums[i] + nums[n - 2] + nums[n - 1];
```

If:

```text
maxSum < target
```

then every combination using this `i` is smaller than the target.

There is no reason to run the two-pointer search, so we move to the next `i`.

## Algorithm

1. Sort `nums`.
2. Initialize `closest` using the first three elements.
3. Iterate through each possible fixed index `i`.
4. Skip duplicate values for `i`.
5. Calculate the smallest possible sum for the current `i`.
6. If that minimum is already greater than the target:

   * Update `closest` if necessary.
   * Stop the outer loop.
7. Calculate the largest possible sum for the current `i`.
8. If that maximum is still smaller than the target:

   * Update `closest` if necessary.
   * Continue to the next `i`.
9. Set:

   * `left = i + 1`
   * `right = n - 1`
10. While `left < right`:

    * Calculate the current sum.
    * Update `closest` if this sum is closer.
    * Return immediately if the sum equals `target`.
    * Move `left` forward if the sum is too small.
    * Move `right` backward if the sum is too large.
11. Return `closest`.

## Implementation

```java
import java.util.Arrays;

class Solution {
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);

        int n = nums.length;
        int closest = nums[0] + nums[1] + nums[2];

        for (int i = 0; i < n - 2; i++) {

            // Skip duplicate fixed values.
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            // Smallest possible sum for this i.
            int minSum = nums[i] + nums[i + 1] + nums[i + 2];

            if (minSum > target) {
                if (minSum - target < Math.abs(closest - target)) {
                    closest = minSum;
                }
                break;
            }

            // Largest possible sum for this i.
            int maxSum = nums[i] + nums[n - 2] + nums[n - 1];

            if (maxSum < target) {
                if (target - maxSum < Math.abs(closest - target)) {
                    closest = maxSum;
                }
                continue;
            }

            int left = i + 1;
            int right = n - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];

                int difference = sum - target;
                int closestDifference = closest - target;

                if (Math.abs(difference) < Math.abs(closestDifference)) {
                    closest = sum;
                }

                // Exact match.
                if (difference == 0) {
                    return sum;
                }

                if (difference < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }

        return closest;
    }
}
```

## Correctness

Sorting allows the two-pointer technique to determine how to adjust the current sum.

For a fixed `i`:

* If `sum < target`, increasing `left` increases the sum.
* If `sum > target`, decreasing `right` decreases the sum.

Therefore, every pointer movement moves the search toward the target without needing to examine the eliminated combinations.

The pruning conditions are also safe:

* If the minimum possible sum is greater than the target, all later combinations are also greater.
* If the maximum possible sum is smaller than the target, all combinations for the current `i` are smaller.
* Duplicate fixed values cannot produce a different set of possible sums that would improve the result.

If an exact match is found, its difference from the target is zero, which is necessarily optimal.

## Complexity

Let `n` be the length of `nums`.

* **Sorting:** `O(n log n)`
* **Two-pointer search:** `O(n²)` worst case
* **Overall:** `O(n²)`
* **Auxiliary Space:** `O(log n)` due to the sorting implementation

The `O(n²)` time complexity is the standard optimal asymptotic approach for the general 3Sum Closest problem.

## Key Insight

The central optimization is converting the three-dimensional search:

```text
i × left × right
```

into a sorted two-pointer search:

```text
i
 └── left → ← right
```

Sorting provides enough ordering information to eliminate many possible triplets with each pointer movement, reducing the solution from **`O(n³)` to `O(n²)`**.
