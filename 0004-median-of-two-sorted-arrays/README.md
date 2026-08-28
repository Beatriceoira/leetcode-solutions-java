LeetCode #4 — Median of Two Sorted Arrays

A Java solution to LeetCode Problem #4: Median of Two Sorted Arrays, using Binary Search and array partitioning to find the median in logarithmic time.


Problem

Given two sorted arrays nums1 and nums2, return the median of the two arrays.

The overall runtime complexity must be:

O(log(m + n))


Example 1

Input:

nums1 = [1,3]
nums2 = [2]

Output:

2. 00000

Explanation:

The combined sorted array would be:

[1,2,3]

The middle element is 2, so the median is 2.0.


Example 2

Input:

nums1 = [1,2]
nums2 = [3,4]

Output:

2. 50000

Explanation:

The combined sorted array would be:

[1,2,3,4]

There are two middle elements, 2 and 3.

Therefore:

(2 + 3) / 2 = 2.5


Approach

The solution uses Binary Search to find the correct partition between the left and right halves of the combined arrays.

To make the binary search efficient, the algorithm always searches the smaller array.


Partitioning

Instead of merging the arrays, we divide them into a left and right portion.

For example:

nums1 = [1,3]
nums2 = [2]

nums1: [1] | [3]
nums2: [2] | []

The goal is to create a partition where:

Maximum value on the left
        <=
Minimum value on the right

For both arrays:

maxLeft1 <= minRight2
maxLeft2 <= minRight1

When both conditions are satisfied, the correct partition has been found.


Odd Number of Elements

If the total number of elements is odd, the median is the largest value on the left side:

median = max(maxLeft1, maxLeft2)


Even Number of Elements

If the total number of elements is even, the median is the average of:

The largest value on the left
The smallest value on the right
median =
(max(maxLeft1, maxLeft2) + min(minRight1, minRight2)) / 2


Solution

class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {

        // Always binary search the smaller array
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }

        int m = nums1.length;
        int n = nums2.length;

        int left = 0;
        int right = m;

        while (left <= right) {

            int partition1 = (left + right) / 2;

            int partition2 = (m + n + 1) / 2 - partition1;

            int maxLeft1 = (partition1 == 0)
                    ? Integer.MIN_VALUE
                    : nums1[partition1 - 1];

            int minRight1 = (partition1 == m)
                    ? Integer.MAX_VALUE
                    : nums1[partition1];

            int maxLeft2 = (partition2 == 0)
                    ? Integer.MIN_VALUE
                    : nums2[partition2 - 1];

            int minRight2 = (partition2 == n)
                    ? Integer.MAX_VALUE
                    : nums2[partition2];

            if (maxLeft1 <= minRight2 &&
                maxLeft2 <= minRight1) {

                if ((m + n) % 2 == 1) {
                    return Math.max(maxLeft1, maxLeft2);
                }

                return (Math.max(maxLeft1, maxLeft2)
                        + Math.min(minRight1, minRight2)) / 2.0;
            }

            if (maxLeft1 > minRight2) {
                right = partition1 - 1;
            } else {
                left = partition1 + 1;
            }
        }

        return 0.0;
    }
}

Complexity

Let m and n be the lengths of the two arrays.

Metric	  Complexity
Time	    O(log(min(m, n)))
Space	       O(1)

The algorithm performs binary search only on the smaller array and does not create a merged array.


Key Concepts
- Binary Search
- Array Partitioning
- Sorted Arrays
- Divide and Conquer
- Median
- Two-Array Problems
- LeetCode


LeetCode #4 — Median of Two Sorted Arrays

Language
- Java
