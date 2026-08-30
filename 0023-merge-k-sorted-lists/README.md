# LeetCode #23 — Merge k Sorted Lists

## Problem

You are given an array of `k` linked lists, where each linked list is sorted in ascending order.

Merge all the linked lists into **one sorted linked list** and return its head.

The solution must preserve the ordering of all nodes.


# Examples

## Example 1

```text
Input:
lists = [[1,4,5],[1,3,4],[2,6]]

Output:
[1,1,2,3,4,4,5,6]
```

The three sorted lists:

```text
[1,4,5]
[1,3,4]
[2,6]
```

are merged into:

```text
[1,1,2,3,4,4,5,6]
```


## Example 2

```text
Input:
lists = []

Output:
[]
```

There are no linked lists to merge, so the result is `null`.


## Example 3

```text
Input:
lists = [[]]

Output:
[]
```

The only list is empty, so the result is `null`.


# Approach

This solution uses **iterative divide and conquer**.

Instead of inserting every node into a priority queue, the lists are merged in pairs.

For example, given:

```text
[1] [2] [3] [4] [5] [6] [7] [8]
```

the first pass merges pairs:

```text
[1 + 2] [3 + 4] [5 + 6] [7 + 8]
```

The second pass merges those results:

```text
[1+2+3+4] [5+6+7+8]
```

The final pass produces:

```text
[1+2+3+4+5+6+7+8]
```

This creates a balanced merge structure similar to merge sort.



# Why Divide and Conquer?

A straightforward approach is to use a `PriorityQueue` containing the smallest current node from every list.

That produces:

```text
O(N log k)
```

time complexity, where `N` is the total number of nodes.

However, every node insertion and removal requires heap operations.

This implementation instead repeatedly merges two sorted lists.

Each merge is linear in the number of nodes being merged, and the number of merge levels is approximately:

```text
log₂(k)
```

Therefore:

```text
O(N log k)
```

time complexity is maintained while avoiding the priority queue.



# Merge Process

The main loop starts with:

```java
int interval = 1;
```

This means lists are initially merged one pair at a time.

```java
while (interval < len) {
    int step = interval * 2;

    for (int i = 0; i + interval < len; i += step) {
        lists[i] = mergeTwoLists(
            lists[i],
            lists[i + interval]
        );
    }

    interval = step;
}
```

The interval doubles after every pass.

For example, with eight lists:

```text
interval = 1

[0] + [1]
[2] + [3]
[4] + [5]
[6] + [7]
```

Then:

```text
interval = 2

[0] + [2]
[4] + [6]
```

Then:

```text
interval = 4

[0] + [4]
```

The final merged list is stored at:

```java
lists[0]
```



# Merging Two Lists

The helper method:

```java
mergeTwoLists(l1, l2)
```

merges two already sorted linked lists.

For example:

```text
l1: 1 → 4 → 5
l2: 1 → 3 → 4
```

The smallest available node is repeatedly selected:

```text
1
1 → 3
1 → 1 → 3
1 → 1 → 3 → 4
...
```

producing:

```text
1 → 1 → 3 → 4 → 4 → 5
```

The core comparison is:

```java
if (l1.val <= l2.val) {
    curr.next = l1;
    l1 = l1.next;
} else {
    curr.next = l2;
    l2 = l2.next;
}
```

Because both lists are already sorted, only the current nodes need to be compared.



# Dummy Node

The two-list merge uses a dummy node:

```java
ListNode dummy = new ListNode(0);
ListNode curr = dummy;
```

This simplifies constructing the resulting list because the first node can be attached using the same logic as every subsequent node.

After merging:

```java
return dummy.next;
```

The dummy node itself is not part of the result.



# Remaining Nodes

Once either `l1` or `l2` becomes `null`, the remaining portion of the other list is already sorted.

Therefore, it can be attached directly:

```java
curr.next = (l1 != null) ? l1 : l2;
```

There is no need to iterate through the remaining nodes.



# Complete Implementation

```java
class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists == null || lists.length == 0) {
            return null;
        }

        int len = lists.length;
        int interval = 1;

        while (interval < len) {
            int step = interval * 2;

            for (int i = 0; i + interval < len; i += step) {
                lists[i] = mergeTwoLists(
                    lists[i],
                    lists[i + interval]
                );
            }

            interval = step;
        }

        return lists[0];
    }

    private ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) return l2;
        if (l2 == null) return l1;

        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;

        while (l1 != null && l2 != null) {
            if (l1.val <= l2.val) {
                curr.next = l1;
                l1 = l1.next;
            } else {
                curr.next = l2;
                l2 = l2.next;
            }

            curr = curr.next;
        }

        curr.next = (l1 != null) ? l1 : l2;

        return dummy.next;
    }
}
```



# Example Walkthrough

Consider:

```text
lists = [
    [1,4,5],
    [1,3,4],
    [2,6]
]
```

Initially:

```text
[1,4,5] [1,3,4] [2,6]
```

### Pass 1

`interval = 1`

Merge:

```text
[1,4,5]
+
[1,3,4]
```

Result:

```text
[1,1,3,4,4,5]
```

The array becomes conceptually:

```text
[1,1,3,4,4,5] [1,3,4] [2,6]
```

The third list has no partner, so it remains unchanged.



### Pass 2

`interval = 2`

Merge:

```text
[1,1,3,4,4,5]
+
[2,6]
```

Result:

```text
[1,1,2,3,4,4,5,6]
```

The final result is:

```text
[1,1,2,3,4,4,5,6]
```



# In-Place List Reuse

The algorithm does not create new nodes for the merged lists.

Instead, it reuses the existing `ListNode` objects and changes their `next` references.

For example:

```text
1 → 4 → 5

1 → 3 → 4
```

are rearranged into:

```text
1 → 1 → 3 → 4 → 4 → 5
```

The nodes themselves are reused.

The only temporary node allocation occurs inside `mergeTwoLists()` for the dummy node.



# Complexity

Let:

```text
N = total number of nodes
k = number of linked lists
```

## Time Complexity

Each merge level processes all `N` nodes.

There are approximately:

```text
log₂(k)
```

levels.

Therefore:

```text
Time: O(N log k)
```



## Space Complexity

The algorithm uses constant auxiliary space outside of the linked-list nodes:

```text
Space: O(1)
```

No:

* `PriorityQueue`
* ArrayList
* HashMap
* recursion stack
* additional linked-list nodes for the actual result

are required.

The `lists` array itself is provided by the problem and is reused to store intermediate merged lists.



# Optimization

This implementation improves on the priority-queue approach by eliminating heap operations.

### Priority Queue

```text
Every node:
    poll minimum → O(log k)
    insert next node → O(log k)
```

Although the overall complexity remains:

```text
O(N log k)
```

the constant overhead can be significant.

### Divide and Conquer

This implementation performs:

```text
pairwise merge
      ↓
pairwise merge
      ↓
pairwise merge
      ↓
final list
```

Each level performs linear work across the nodes.

This provides:

```text
O(N log k) time
O(1) auxiliary space
```

while keeping the implementation relatively simple.



# Key Concepts

* Linked Lists
* Merge Sort
* Divide and Conquer
* Pairwise Merging
* Pointer Manipulation
* In-Place List Reuse
* Iterative Algorithms
* Dummy Nodes
* Time Complexity Optimization
* Space Complexity Optimization



# Language

**Java**



LeetCode

[LeetCode #23 — Merge k Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/description/)
