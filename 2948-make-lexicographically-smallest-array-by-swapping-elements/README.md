LeetCode #2948 — Make Lexicographically Smallest Array by Swapping Elements

Problem

You are given a 0-indexed array of positive integers nums and a positive integer limit.

In one operation, you can choose two indices i and j and swap:

nums[i]
nums[j]

if:

|nums[i] - nums[j]| <= limit

You may perform the operation any number of times.

Return the lexicographically smallest array that can be obtained.

An array is lexicographically smaller than another array if, at the first position where they differ, it contains the smaller value.


Examples
Example 1
Input:
nums = [1,5,3,9,8]
limit = 2

Output:
[1,3,5,8,9]

The values 1, 3, and 5 can be rearranged among their original positions because their adjacent differences are within the limit.

Similarly, 8 and 9 can be swapped.

Example 2
Input:
nums = [1,7,6,18,2,1]
limit = 3

Output:
[1,6,7,18,1,2]

The values 1, 2, and 1 form one connected group, while 6 and 7 form another group.

Each group can be rearranged among its corresponding indices.

Example 3
Input:
nums = [1,7,28,19,10]
limit = 3

Output:
[1,7,28,19,10]

No two values can be connected through valid swaps, so the array cannot be changed.


Key Insight

The most important observation is that direct swaps are not the only possible swaps.

Values can be connected through a chain of valid swaps.

For example:

values = [1,3,5]
limit = 2

Although:

|1 - 5| = 4

so 1 and 5 cannot directly swap, we can perform:

1 ↔ 3
3 ↔ 5

because:

|1 - 3| = 2
|3 - 5| = 2

Therefore, all three values effectively belong to the same connected component.

Within a connected component, the values can be rearranged freely among their original indices.


Approach

The solution uses:

- Sorting
- Connected components
- Original index tracking
- Greedy assignment
- Lexicographical ordering

The algorithm has three main steps:

1. Sort values while keeping their original indices.
2. Group values whose adjacent differences are <= limit.
3. For each group:
   - Sort its original indices.
   - Assign the smallest values to the smallest indices.

Step 1 — Store Values and Original Indices

We create pairs:

[value, originalIndex]

For:

nums = [1,5,3,9,8]

we initially have:

[1,0]
[5,1]
[3,2]
[9,3]
[8,4]

After sorting by value:

value   index
  1       0
  3       2
  5       1
  8       4
  9       3

The original positions are preserved.

Step 2 — Find Connected Components

Because the values are sorted, we only need to compare adjacent values.

For:

1, 3, 5, 8, 9

with:

limit = 2

we calculate:

3 - 1 = 2  <= 2
5 - 3 = 2  <= 2
8 - 5 = 3  >  2
9 - 8 = 1  <= 2

Therefore, we have:

Component 1:
[1,3,5]

Component 2:
[8,9]

The components are independent because there is no valid chain connecting them.


Why Adjacent Differences Are Enough

Suppose the sorted values are:

a <= b <= c

If:

b - a <= limit

and:

c - b <= limit

then a, b, and c are connected:

a ↔ b ↔ c

It does not matter that:

c - a > limit

because a can reach c through b.

Therefore, after sorting, a gap greater than limit creates a boundary between independent components.

Step 3 — Sort the Original Indices

Consider Component 1:

values:
[1,3,5]

original indices:
[0,2,1]

Sort the indices:

[0,1,2]

Now we have:

smallest index → smallest value

which produces:

index 0 → 1
index 1 → 3
index 2 → 5

giving:

[1,3,5]


Why This Produces the Lexicographically Smallest Array

Lexicographical order prioritizes the earliest positions.

Therefore, whenever a group of values can be freely rearranged, the smallest possible value should be placed at the smallest available index.

For example:

values:
[3,5,8]

indices:
[2,0,1]

Sort the indices:

[0,1,2]

Then assign:

index 0 → 3
index 1 → 5
index 2 → 8

This minimizes the earliest possible position, then the next position, and so on.


Algorithm
1. Create a [value, originalIndex] pair for every element.
2. Sort the pairs by value.
3. Iterate through the sorted pairs.
4. Find connected components by checking adjacent value differences.
5. For each component:
- Extract its original indices.
- Sort those indices.
- Assign the sorted component values to the sorted indices.
6. Return the modified array.


Implementation
import java.util.Arrays;

class Solution {
    public int[] lexicographicallySmallestArray(int[] nums, int limit) {
        int n = nums.length;

        // Store [value, original index].
        int[][] pairs = new int[n][2];

        for (int i = 0; i < n; i++) {
            pairs[i][0] = nums[i];
            pairs[i][1] = i;
        }

        // Sort by value.
        Arrays.sort(pairs, (a, b) -> Integer.compare(a[0], b[0]));

        int start = 0;

        while (start < n) {
            int end = start;

            // Find the connected component.
            while (end + 1 < n &&
                   (long) pairs[end + 1][0] - pairs[end][0] <= limit) {
                end++;
            }

            int size = end - start + 1;

            // Extract original indices.
            int[] indices = new int[size];

            for (int i = 0; i < size; i++) {
                indices[i] = pairs[start + i][1];
            }

            // Smallest index gets smallest value.
            Arrays.sort(indices);

            for (int i = 0; i < size; i++) {
                nums[indices[i]] = pairs[start + i][0];
            }

            start = end + 1;
        }

        return nums;
    }
}
Example Walkthrough

Consider:

nums = [1,5,3,9,8]
limit = 2

After sorting:

value   index
  1       0
  3       2
  5       1
  8       4
  9       3
Component 1

Values:

[1,3,5]

Indices:

[0,2,1]

Sort indices:

[0,1,2]

Assign:

0 → 1
1 → 3
2 → 5

Result:

[1,3,5,_,_]
Component 2

Values:

[8,9]

Indices:

[4,3]

Sort indices:

[3,4]

Assign:

3 → 8
4 → 9

Final result:

[1,3,5,8,9]
Complexity

Let n be the length of nums.

Sorting Values
O(n log n)
Finding Components
O(n)
Sorting Component Indices

Across all components, the total cost is at most:

O(n log n)

Therefore:

Time:  O(n log n)
Space: O(n)

This is suitable for:

n <= 100,000
Optimization Details
Long Arithmetic

The implementation uses:

(long) pairs[end + 1][0] - pairs[end][0]

instead of directly subtracting two int values.

This makes the difference calculation robust against integer overflow if the input range is expanded.

No Graph Construction

A naive solution might attempt to construct a graph:

Point 1 ↔ Point 2
Point 2 ↔ Point 3
...

This would require potentially O(n²) comparisons and significantly more memory.

Instead, sorting lets us identify connected components using only adjacent values.

No Explicit Union-Find

A Disjoint Set Union (DSU/Union-Find) solution is possible, but unnecessary here.

Once the values are sorted, every gap greater than limit naturally separates components.

Therefore, the component boundaries can be found with a simple linear scan.

Edge Cases
Single Element
Input:
nums = [5]
limit = 1

Output:
[5]

There is nothing to swap.

No Valid Swaps
Input:
nums = [1,10,20]
limit = 2

Output:
[1,10,20]

Every value belongs to a separate component.

All Values Connected
Input:
nums = [5,1,3,2]
limit = 2

Sorted values:

1,2,3,5

Differences:

1
1
2

All values belong to the same component and can therefore be rearranged across all original positions.

Duplicate Values

Duplicate values naturally work with the grouping strategy.

For example:

nums = [3,1,3,2]
limit = 2

Sorted values:

1,2,3,3

All values belong to the same component.

Key Concepts
- Sorting
- Greedy Algorithms
- Connected Components
- Lexicographical Ordering
- Array Manipulation
- Index Tracking
- Sorting by Multiple Criteria
- Component Grouping
- Greedy Assignment

Language

Java

LeetCode

LeetCode #2948 — Make Lexicographically Smallest Array by Swapping Elements(https://leetcode.com/problems/make-lexicographically-smallest-array-by-swapping-elements/description/?envType=daily-question&envId=2026-08-29)
