# LeetCode #2058 — Find the Minimum and Maximum Number of Nodes Between Critical Points

## Problem

Given the `head` of a linked list, identify all **critical points** and determine:

* The minimum distance between any two distinct critical points.
* The maximum distance between any two distinct critical points.

A node is a critical point if it is either:

* A local maximum — its value is strictly greater than both neighboring nodes.
* A local minimum — its value is strictly smaller than both neighboring nodes.

The first and last nodes cannot be critical points because each must have both a previous and next node.

If there are fewer than two critical points, return:


[-1, -1]




# Examples

## Example 1


Input:
head = [3,1]

Output:
[-1,-1]


There are only two nodes, so neither node has both a previous and next node.

Therefore, there are no critical points.



## Example 2


Input:
head = [5,3,1,2,5,1,2]

Output:
[1,3]


The critical points are:


Index:  0  1  2  3  4  5  6
Value:  5  3  1  2  5  1  2
            ↓     ↓  ↓


Critical points:


index 2 → local minimum
index 4 → local maximum
index 5 → local minimum


Distances:


4 - 2 = 2
5 - 4 = 1
5 - 2 = 3


Therefore:


Minimum distance = 1
Maximum distance = 3




## Example 3


Input:
head = [1,3,2,2,3,2,2,2,7]

Output:
[3,3]


The critical points occur at:


index 1
index 4


Therefore:


Minimum distance = 4 - 1 = 3
Maximum distance = 4 - 1 = 3




# Approach

The solution uses a **single traversal** of the linked list.

Instead of storing every critical point, only three pieces of information are needed:


firstIdx
prevIdx
minDistance


### `firstIdx`

Stores the position of the first critical point.

This is required to calculate the maximum distance.

### `prevIdx`

Stores the position of the most recently discovered critical point.

This allows the algorithm to calculate the distance to the current critical point.

### `minDistance`

Stores the smallest distance encountered between consecutive critical points.



# Detecting Critical Points

For every node that has both a previous and next node, the algorithm checks:


(currVal > prevVal && currVal > nextVal)


for a local maximum, or:


(currVal < prevVal && currVal < nextVal)


for a local minimum.

Combined:


if ((currVal > prevVal && currVal > nextVal) ||
    (currVal < prevVal && currVal < nextVal)) {


The comparisons are strict, meaning equal neighboring values do not produce a critical point.



# Tracking Critical Points

When a critical point is found, the algorithm checks whether it is the first one.


if (firstIdx == -1) {
    firstIdx = index;
}


If it is the first critical point, its index is saved.

Otherwise, the distance from the previous critical point is calculated:


int dist = index - prevIdx;


The minimum distance is then updated:


if (dist < minDistance) {
    minDistance = dist;
}


Finally:


prevIdx = index;


updates the most recently encountered critical point.



# Calculating Maximum Distance

The maximum distance does not require checking every pair of critical points.

Because the linked list positions are ordered, the maximum distance must be between:


first critical point
        ↓
last critical point


Therefore:


prevIdx - firstIdx


gives the maximum distance.

For example:


Critical points:

2 → 4 → 5



The maximum distance is:


5 - 2 = 3




# Why Only Consecutive Critical Points Are Needed for Minimum Distance

Suppose the critical points occur at:


2, 4, 5, 9


The distances between consecutive critical points are:


4 - 2 = 2
5 - 4 = 1
9 - 5 = 4


The smallest distance between **any** two critical points must be one of these consecutive differences.

There is no need to compare:


5 - 2
9 - 2
9 - 4


because those distances will always be greater than or equal to a distance between some consecutive critical points.

Therefore, storing only the previous critical point is sufficient.



# Single-Pass Traversal

The list is traversed once:

ListNode curr = head.next;

while (curr.next != null) {

This intentionally begins at the second node and stops before the last node.

That guarantees every examined node has:


previous node
current node
next node


available.

The previous value is stored in:


int prevVal = head.val;


and updated after every iteration:


prevVal = currVal;




# Edge Cases

## Fewer Than Three Nodes

A critical point requires a previous and next node.

Therefore:


if (head == null ||
    head.next == null ||
    head.next.next == null) {
    return new int[]{-1, -1};
}


handles lists that cannot contain a critical point.



## Fewer Than Two Critical Points

If only one critical point is found, there is no pair of critical points to compare.

The solution checks:


if (prevIdx == firstIdx) {
    return new int[]{-1, -1};
}


and returns:


[-1,-1]




## Equal Values

A critical point must be strictly greater or strictly smaller than both neighbors.

For example:


[1, 3, 3, 2]


The first `3` is not a local maximum because:


3 > 3


is false.

The implementation correctly handles this through the strict `>` and `<` comparisons.



# Complete Implementation


class Solution {
    public int[] nodesBetweenCriticalPoints(ListNode head) {
        if (head == null ||
            head.next == null ||
            head.next.next == null) {
            return new int[]{-1, -1};
        }

        int firstIdx = -1;
        int prevIdx = -1;
        int minDistance = Integer.MAX_VALUE;

        int index = 1;
        int prevVal = head.val;
        ListNode curr = head.next;

        while (curr.next != null) {
            int currVal = curr.val;
            int nextVal = curr.next.val;

            if ((currVal > prevVal && currVal > nextVal) ||
                (currVal < prevVal && currVal < nextVal)) {

                if (firstIdx == -1) {
                    firstIdx = index;
                } else {
                    int dist = index - prevIdx;

                    if (dist < minDistance) {
                        minDistance = dist;
                    }
                }

                prevIdx = index;
            }

            prevVal = currVal;
            curr = curr.next;
            index++;
        }

        if (prevIdx == firstIdx) {
            return new int[]{-1, -1};
        }

        return new int[]{
            minDistance,
            prevIdx - firstIdx
        };
    }
}




# Example Walkthrough

Consider:


head = [5,3,1,2,5,1,2]


The traversal examines:


Previous   Current   Next
   5         3        1
   3         1        2
   1         2        5
   2         5        1
   5         1        2


### Index 1


5 > 3 > 1


`3` is greater than `1`, but:


3 < 5


so it is not a critical point.

### Index 2


3 > 1 < 2


`1` is smaller than both neighbors.

Critical point:


index 2


This becomes:


firstIdx = 2
prevIdx = 2


### Index 4

2 < 5 > 1


`5` is greater than both neighbors.

Critical point:


index 4


Distance:


4 - 2 = 2


So:


minDistance = 2
prevIdx = 4


### Index 5


5 > 1 < 2

`1` is another critical point.

Distance:


5 - 4 = 1


Therefore:


minDistance = 1


The maximum distance is:


5 - 2 = 3


Final result:


[1,3]




# Complexity

Let:


n = number of nodes in the linked list


## Time Complexity

The linked list is traversed exactly once.


Time: O(n)




## Space Complexity

Only a constant number of variables are used.

The solution does not store the critical points or create additional data structures.

Space: O(1)



# Optimization

This implementation is already **asymptotically optimal**.

It achieves:


O(n) time
O(1) extra space


The solution does not need to:

* Store all critical point indices
* Convert the linked list into an array
* Perform multiple traversals
* Use a `List`
* Use a `HashMap`
* Use recursion

Instead, it maintains only the information necessary to determine the minimum and maximum distances.

The key optimization is recognizing that:


Minimum distance → consecutive critical points
Maximum distance → first and last critical points


This reduces the problem to a single linear traversal.



# Key Concepts

* Singly Linked Lists
* Linked List Traversal
* Local Minima
* Local Maxima
* Pointer Manipulation
* Single-Pass Algorithms
* Constant Extra Space
* Index Tracking
* Edge Case Handling
* Greedy Tracking



# Language

Java



## LeetCode

[LeetCode #2058 — Find the Minimum and Maximum Number of Nodes Between Critical Points](https://leetcode.com/problems/find-the-minimum-and-maximum-number-of-nodes-between-critical-points/description/?envType=daily-question&envId=2026-08-31)
