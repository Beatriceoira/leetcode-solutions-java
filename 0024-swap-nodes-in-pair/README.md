# 24. Swap Nodes in Pairs

Link - https://leetcode.com/problems/swap-nodes-in-pairs/description/

## Problem

Given the head of a linked list, swap every two adjacent nodes and return the new head.

The node values must **not** be modified. Only the links between the nodes may be changed.

### Example 1

```text
Input:  head = [1,2,3,4]
Output: [2,1,4,3]
```

### Example 2

```text
Input:  head = []
Output: []
```

### Example 3

```text
Input:  head = [1]
Output: [1]
```

### Example 4

```text
Input:  head = [1,2,3]
Output: [2,1,3]
```

## Approach

Use an **iterative pointer-based approach**.

For every pair of nodes:

```text
prev → first → second → next
```

The links are rearranged to:

```text
prev → second → first → next
```

The node values remain untouched.

A dummy node is placed before the original head so that the first pair can be swapped using exactly the same pointer operations as every subsequent pair.

## Algorithm

1. Create a dummy node pointing to `head`.
2. Set `prev` to the dummy node.
3. Continue while two nodes are available:

   * `first = prev.next`
   * `second = first.next`
4. Rearrange the links:

   * Connect `first` to the node after `second`.
   * Connect `second` to `first`.
   * Connect `prev` to `second`.
5. Move `prev` to `first`.
6. Return `dummy.next`.

## Example Walkthrough

Starting with:

```text
1 → 2 → 3 → 4
```

Initially:

```text
prev → 1 → 2 → 3 → 4
```

Swap the first pair:

```text
prev → 2 → 1 → 3 → 4
```

Move `prev` to `1`:

```text
2 → 1 → prev → 3 → 4
```

Swap the second pair:

```text
2 → 1 → 4 → 3
```

Final result:

```text
[2,1,4,3]
```

## Optimized Java Solution

```java
class Solution {
    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0, head);
        ListNode prev = dummy;

        while (prev.next != null && prev.next.next != null) {
            ListNode first = prev.next;
            ListNode second = first.next;

            first.next = second.next;
            second.next = first;
            prev.next = second;

            prev = first;
        }

        return dummy.next;
    }
}
```

## Why Use a Dummy Node?

Without a dummy node, the first pair requires special handling because the head itself changes.

For example:

```text
1 → 2 → 3 → 4
```

After swapping:

```text
2 → 1 → 3 → 4
```

The head changes from `1` to `2`.

The dummy node gives us:

```text
dummy → 1 → 2 → 3 → 4
```

Now the first swap is handled exactly like every other pair:

```text
dummy → 2 → 1 → 3 → 4
```

The new head is simply:

```java
dummy.next
```

## Why Iterative Instead of Recursive?

A recursive solution is also possible, but it uses the call stack.

### Recursive

```text
Time:  O(n)
Space: O(n)
```

because recursive calls can reach `O(n)` depth.

### Iterative

```text
Time:  O(n)
Space: O(1)
```

The iterative implementation only maintains a constant number of references.

For this problem, the iterative approach is therefore more memory-efficient.

## Complexity

Let `n` be the number of nodes.

### Time Complexity

```text
O(n)
```

Each node is processed a constant number of times.

### Auxiliary Space

```text
O(1)
```

Only a fixed number of pointers are used.

The dummy node is a constant-size allocation and does not depend on the input size.

## Important Constraint

The solution must not modify:

```java
node.val
```

Instead, it modifies only:

```java
node.next
```

For example:

```java
first.next = second.next;
second.next = first;
prev.next = second;
```

This preserves the original node values while changing their order.

## Key Takeaway

The core of the problem is **pointer manipulation**, not value swapping.

For every adjacent pair:

```text
A → B
```

change the links to:

```text
B → A
```

while preserving the connection to the remainder of the list.

The resulting solution is:

```text
Time:  O(n)
Space: O(1)
```

and satisfies the requirement to modify only the linked-list structure.
