# LeetCode #25 — Reverse Nodes in k-Group

## Problem

Given the `head` of a singly linked list, reverse the nodes of the list **`k` at a time** and return the modified list.

If the number of remaining nodes is less than `k`, those nodes must remain in their original order.

The values stored inside the nodes **cannot be changed**. Only the links between nodes may be modified.


# Examples

## Example 1

```text
Input:
head = [1,2,3,4,5]
k = 2

Output:
[2,1,4,3,5]
```

The list is divided into groups of two:

```text
[1,2] [3,4] [5]
```

Each complete group is reversed:

```text
[2,1] [4,3] [5]
```

The final node remains unchanged because there are fewer than `k` nodes.


## Example 2

```text
Input:
head = [1,2,3,4,5]
k = 3

Output:
[3,2,1,4,5]
```

The groups are:

```text
[1,2,3] [4,5]
```

The first group contains three nodes and is reversed:

```text
[3,2,1] [4,5]
```

The remaining two nodes are left unchanged.


# Approach

The solution processes the linked list one group at a time.

For every group:

1. Find the `k`th node.
2. If fewer than `k` nodes remain, stop.
3. Save the node immediately after the group.
4. Reverse exactly `k` nodes.
5. Reconnect the reversed group to the previous part of the list.
6. Move to the next group.

The reversal is performed **in-place**, meaning no new list nodes are created.

# Dummy Node

A dummy node is placed before the original head:

```text
dummy → 1 → 2 → 3 → 4 → 5
```

This simplifies reconnecting the first reversed group.

After reversing the first group:

```text
dummy → 2 → 1 → 3 → 4 → 5
```

Without a dummy node, the first group would require special handling because its new head changes.


# Finding the kth Node

The helper method:

```java
private ListNode getKthNode(ListNode curr, int k)
```

moves exactly `k` positions forward.

```java
private ListNode getKthNode(ListNode curr, int k) {
    while (curr != null && k > 0) {
        curr = curr.next;
        k--;
    }

    return curr;
}
```

For example:

```text
1 → 2 → 3 → 4 → 5
```

with:

```text
k = 3
```

starting from the node before the group:

```text
dummy → 1 → 2 → 3
```

the method returns:

```text
3
```

This allows the algorithm to determine whether a complete group of `k` nodes exists.


# Detecting Incomplete Groups

The algorithm calls:

```java
ListNode kth = getKthNode(groupPrev, k);
```

If:

```java
kth == null
```

there aren't enough nodes remaining to form a complete group.

The algorithm then stops:

```java
if (kth == null) {
    break;
}
```

This ensures the leftover nodes remain untouched.


# Reversing a Group

Once the `k`th node has been found:

```java
ListNode groupNext = kth.next;
```

`groupNext` marks the first node **after** the current group.

The reversal starts with:

```java
ListNode prev = groupNext;
ListNode curr = groupPrev.next;
```

The group is then reversed:

```java
while (curr != groupNext) {
    ListNode tmp = curr.next;
    curr.next = prev;
    prev = curr;
    curr = tmp;
}
```

For example:

```text
1 → 2 → 3 → 4
```

with:

```text
k = 3
```

The group:

```text
1 → 2 → 3
```

becomes:

```text
3 → 2 → 1 → 4
```

# Reconnecting the Group

After reversal, the original first node becomes the group's tail.

Before reversal:

```text
groupPrev → 1 → 2 → 3 → 4
```

After reversal:

```text
groupPrev → 3 → 2 → 1 → 4
```

The solution reconnects the previous section using:

```java
ListNode tmp = groupPrev.next;
groupPrev.next = kth;
groupPrev = tmp;
```

Here:

```text
kth
```

is the new head of the reversed group.

The old group head:

```text
tmp
```

is now the tail, so it becomes the `groupPrev` node for the next iteration.


# Complete Implementation

```java
class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || k == 1) {
            return head;
        }

        ListNode dummy = new ListNode(0);
        dummy.next = head;

        ListNode groupPrev = dummy;

        while (true) {
            ListNode kth = getKthNode(groupPrev, k);

            if (kth == null) {
                break;
            }

            ListNode groupNext = kth.next;

            ListNode prev = groupNext;
            ListNode curr = groupPrev.next;

            while (curr != groupNext) {
                ListNode tmp = curr.next;
                curr.next = prev;
                prev = curr;
                curr = tmp;
            }

            ListNode tmp = groupPrev.next;

            groupPrev.next = kth;
            groupPrev = tmp;
        }

        return dummy.next;
    }

    private ListNode getKthNode(ListNode curr, int k) {
        while (curr != null && k > 0) {
            curr = curr.next;
            k--;
        }

        return curr;
    }
}
```



# Example Walkthrough

Consider:

```text
head = [1,2,3,4,5]
k = 2
```

Initial list:

```text
dummy → 1 → 2 → 3 → 4 → 5
```

### Group 1

Find the second node:

```text
1 → 2
```

Reverse:

```text
2 → 1
```

List becomes:

```text
dummy → 2 → 1 → 3 → 4 → 5
```

`1` becomes the previous group tail.


### Group 2

Find:

```text
3 → 4
```

Reverse:

```text
4 → 3
```

List becomes:

```text
dummy → 2 → 1 → 4 → 3 → 5
```


### Group 3

Only:

```text
5
```

remains.

Since:

```text
1 < k
```

the group is not reversed.

Final result:

```text
[2,1,4,3,5]
```


# Why This Works

Every complete group contains exactly `k` nodes.

The algorithm reverses only those complete groups and leaves any incomplete final group untouched.

For:

```text
n = 10
k = 3
```

the list is processed as:

```text
[1,2,3] [4,5,6] [7,8,9] [10]
```

The first three groups are reversed:

```text
[3,2,1] [6,5,4] [9,8,7] [10]
```

The final node remains unchanged.



# Complexity

Let:

```text
n = number of nodes
```

### Time Complexity

Each node is traversed a constant number of times:

* `getKthNode()` locates group boundaries.
* The reversal loop processes each node in the group once.

Therefore:

```text
Time: O(n)
```



### Space Complexity

The algorithm uses only a constant number of `ListNode` references:

```text
dummy
groupPrev
kth
groupNext
prev
curr
tmp
```

No array, stack, list, or recursion is used.

Therefore:

```text
Space: O(1)
```

This satisfies the problem's follow-up requirement.



# Optimization

This implementation is already **asymptotically optimal**.

The list must be traversed to determine the nodes that need to be reversed, so the lower bound is:

```text
O(n)
```

The solution also uses:

```text
O(1)
```

extra memory.

Additional optimizations such as eliminating the dummy node may reduce a tiny amount of runtime overhead, but they make the pointer logic more complicated and do not improve the asymptotic complexity.

The current implementation provides a strong balance of:

* `O(n)` runtime
* `O(1)` auxiliary space
* In-place node manipulation
* No recursion
* No collections
* No modification of node values
* Safe handling of incomplete final groups



# Key Concepts

* Singly Linked Lists
* Pointer Manipulation
* In-Place Reversal
* Linked List Grouping
* Dummy Nodes
* Iterative Algorithms
* Constant Extra Space
* Two-Pointer Technique
* Edge Case Handling



# Language

**Java**



## LeetCode

[LeetCode #25 — Reverse Nodes in k-Group](https://leetcode.com/problems/reverse-nodes-in-k-group/description/)