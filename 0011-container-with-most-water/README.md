LeetCode #11 — Container With Most Water
https://leetcode.com/problems/container-with-most-water/description/
Problem

You are given an integer array height of length n.

Each element represents a vertical line where:

line i = (i, 0) to (i, height[i])

Choose two lines that, together with the x-axis, form a container that holds the maximum possible amount of water.

The container cannot be slanted.

Return the maximum amount of water the container can store.


Example 1

Input:
height = [1,8,6,2,5,4,8,3,7]
Output:
49

The optimal container is formed by the lines at indices 1 and 8:

height[1] = 8
height[8] = 7

The width is:

8 - 1 = 7

The limiting height is:

min(8, 7) = 7

Therefore:

Area = 7 × 7 = 49


Example 2

Input:
height = [1,1]
Output:
1


Approach

A brute-force solution would examine every possible pair of lines, resulting in O(n²) time.

Instead, this solution uses the two-pointer technique.

Two pointers start at the widest possible container:

left  = 0
right = n - 1

For each pair:

area = width × limiting height

where:

width = right - left
limiting height = min(height[left], height[right])

After calculating the area, the pointer at the shorter line is moved inward.

The solution also skips consecutive lines that are no taller than the current limiting line, because they cannot produce a larger area after the width decreases.


Why Move the Shorter Pointer?

Suppose:

height[left] < height[right]

The current container is limited by height[left].

If we move right inward while keeping left:

* The width becomes smaller.
* The limiting height cannot become greater than height[left].

Therefore, no better container can be obtained by keeping left.

So we move:

left++

The same reasoning applies in reverse when the right line is shorter.


Optimization: Skip Useless Lines

Instead of moving the pointer only once:

left++;

the solution skips all consecutive heights that are not taller than the current height:

while (left < right && height[left] <= hLeft) {
    left++;
}

Similarly:

while (left < right && height[right] <= hRight) {
    right--;
}

This is safe because once the width decreases, a line with a height less than or equal to the current limiting height cannot produce a larger area.

For example, if the left pointer is currently at height 8:

8, 7, 6, 8
   ↑  ↑

Moving to 7 or 6 cannot improve the area because:

new width < old width
new height <= old height

Only a height greater than 8 could potentially make the reduced width worthwhile.


Algorithm

1. Initialize:

left = 0
right = n - 1
maxArea = 0

2. While left < right:
    - Store the two heights.
    - Calculate the current width.
    - Find the shorter height.
    - Calculate the container area.
    - Update maxArea if necessary.
3. If the left line is shorter:
    - Move left forward.
    - Skip every height less than or equal to the current left height.
4. Otherwise:
    - Move right backward.
    - Skip every height less than or equal to the current right height.
5. Return maxArea.


Walkthrough

For:

height = [1,8,6,2,5,4,8,3,7]

Initially:

left  = 0 → height = 1
right = 8 → height = 7

Area:

width = 8
height = min(1,7) = 1
area = 8 × 1 = 8

The left side is shorter, so we move left.

The optimization skips heights that are <= 1.


Eventually:

left  = 1 → height = 8
right = 8 → height = 7

Area:

width = 7
height = min(8,7) = 7
area = 7 × 7 = 49

So:

maxArea = 49

The algorithm continues checking possible candidates until the pointers meet.

Final result:

49


Correctness

For any two lines left and right, the amount of water they contain is:

(right - left) × min(height[left], height[right])

The container is always limited by its shorter line.

When the left line is shorter, moving the right pointer inward cannot produce a larger area while keeping the same left line because the width decreases and the limiting height remains bounded by the shorter left line.

Therefore, the left pointer can safely be advanced.

The same argument applies when the right line is shorter.

Furthermore, after moving a pointer, any line whose height is less than or equal to the previous height cannot produce a larger area because both its width and limiting height are no greater than before. Therefore, skipping those lines is safe.

By systematically eliminating only pairs that cannot improve the current maximum, the algorithm eventually considers the optimal pair.

Thus, maxArea is the maximum amount of water that can be contained.


Complete Implementation

class Solution {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;
        while (left < right) {
            int hLeft = height[left];
            int hRight = height[right];
            int area = (right - left) * Math.min(hLeft, hRight);
            if (area > maxArea) {
                maxArea = area;
            }
            if (hLeft < hRight) {
                while (left < right && height[left] <= hLeft) {
                    left++;
                }
            } else {
                while (left < right && height[right] <= hRight) {
                    right--;
                }
            }
        }
        return maxArea;
    }
}


Complexity

Time Complexity

O(n)

Although there are nested while loops, each pointer only moves from one end of the array toward the other.

A height skipped by either pointer is never processed again.

Therefore, the total number of pointer movements is linear:

O(n)

Space Complexity

O(1)

Only a constant number of integer variables are used.

No additional arrays, lists, or data structures are created.


Optimization

This implementation is optimized for both runtime and memory.

1. Two pointers

Instead of checking all n² pairs, the algorithm examines the array using two pointers:

int left = 0;
int right = height.length - 1;

This reduces the time complexity from O(n²) to O(n).

2. No extra data structures

The input array is processed in place.

Auxiliary space = O(1)

3. Cache heights

The current heights are stored locally:

int hLeft = height[left];
int hRight = height[right];

This avoids repeatedly accessing the array for the same values.

4. Skip dominated heights

The inner loops:

while (left < right && height[left] <= hLeft)

and:

while (left < right && height[right] <= hRight)

skip positions that cannot improve the current container.

5. No unnecessary method calls

The comparison is performed directly through:

Math.min(hLeft, hRight)

and the maximum is updated with a simple comparison.

6. Integer arithmetic is sufficient

Given:

n <= 100,000
height[i] <= 10,000

the maximum possible area is:

(100,000 - 1) × 10,000
= 999,990,000

which fits within Java’s int range.


Key Concepts

- Two Pointers
- Greedy Algorithm
- Array Traversal
- Area Maximization
- Dominated-State Elimination
- Constant Auxiliary Space
- Linear-Time Optimization

Language

Java