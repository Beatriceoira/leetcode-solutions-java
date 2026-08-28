LeetCode #19 — Remove Nth Node From End of List


Problem

Given the head of a linked list, remove the nth node from the end of the list and return the head of the modified list.

Example 1
Input:
head = [1,2,3,4,5]
n = 2

Output:
[1,2,3,5]

The 2nd node from the end is 4, so it is removed.

Example 2
Input:
head = [1]
n = 1

Output:
[]

The only node is the 1st node from the end, so it is removed.

Example 3
Input:
head = [1,2]
n = 1

Output:
[1]

The 1st node from the end is 2, so it is removed.


Approach

The solution uses the two-pointer technique with a dummy node.

Two pointers are maintained:

slow
fast

The fast pointer is moved n positions ahead of slow.

This creates a fixed gap of n nodes between the two pointers.

Then both pointers move forward at the same speed until fast reaches the end of the list.

At that point, slow will be positioned immediately before the node that needs to be removed.


Why Use Two Pointers?

Consider:

head = [1,2,3,4,5]
n = 2

We want to remove:

4

because it is the 2nd node from the end:

5 → 1st from end
4 → 2nd from end
3 → 3rd from end

After moving fast two positions ahead, we maintain this gap while moving both pointers:

slow → 3
fast → 5

When fast reaches the end:

slow
 ↓
3 → 4 → 5
    ↑
  remove

Therefore:

slow.next = slow.next.next;

skips node 4.

The resulting list is:

1 → 2 → 3 → 5


Dummy Node

A dummy node is placed before the head:

dummy → 1 → 2 → 3 → 4 → 5

This makes the algorithm work uniformly even when the head itself needs to be removed.

For example:

head = [1]
n = 1

Without a dummy node, removing the head requires a special case.

With the dummy node:

dummy → 1
   ↑
 slow

The algorithm can simply perform:

slow.next = slow.next.next;

resulting in:

dummy → null

and finally:

return dummy.next;

returns:

[]


Algorithm
1. Create a dummy node pointing to head.
2. Set both slow and fast to the dummy node.
3. Move fast forward n positions.
4. Move both pointers forward until fast.next is null.
5. At this point, slow is immediately before the node to remove.
6. Skip the target node using:
slow.next = slow.next.next;
7. Return dummy.next.


Implementation
class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode dummy = new ListNode(0, head);

        ListNode slow = dummy;
        ListNode fast = dummy;

        // Create a gap of n nodes.
        for (int i = 0; i < n; i++) {
            fast = fast.next;
        }

        // Move both pointers until fast reaches the end.
        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }

        // Remove the nth node from the end.
        slow.next = slow.next.next;

        return dummy.next;
    }
}


Complexity

Let n represent the number of nodes in the linked list.

Time Complexity
O(n)

The list is traversed at most once.

Space Complexity
O(1)

Only a few pointers are used regardless of the size of the linked list.


Why This Is Optimal

A linked list does not provide direct access to its elements, so finding the nth node from the end requires traversal.

The two-pointer approach achieves:

Time:  O(n)
Space: O(1)

which is optimal for this problem.

A two-pass solution could first determine the list length and then locate the target node, but the two-pointer approach accomplishes the task in a single traversal.

Edge Cases
Removing the Only Node
Input:
head = [1]
n = 1

Output:
[]

The dummy node allows the head to be removed without a special case.

Removing the Head
Input:
head = [1,2]
n = 2

Output:
[2]
Removing the Tail
Input:
head = [1,2,3]
n = 1

Output:
[1,2]
Removing a Middle Node
Input:
head = [1,2,3,4,5]
n = 2

Output:
[1,2,3,5]


Key Concepts
- Linked Lists
- Two-Pointer Technique
- Fast and Slow Pointers
- Dummy Nodes
- In-Place Modification
- One-Pass Algorithms
- Pointer Manipulation


Constraints
1 <= number of nodes <= 30
0 <= Node.val <= 100
1 <= n <= number of nodes


Language

Java



LeetCode

LeetCode #19 — Remove Nth Node From End of List (https://leetcode.com/problems/remove-nth-node-from-end-of-list/description/)
