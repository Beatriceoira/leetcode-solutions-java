LeetCode #2 — Add Two Numbers

A Java solution to LeetCode Problem #2: Add Two Numbers, using linked-list traversal and carry handling to add two numbers represented as linked lists.

Problem

You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order, with each node containing a single digit.

Add the two numbers and return the sum as a linked list.

Example 1

Input:

l1 = [2,4,3]
l2 = [5,6,4]

Output:

[7,0,8]

Explanation:

The linked lists represent:

342 + 465 = 807

Since the digits are stored in reverse order, the result is:

[7,0,8]
Example 2

Input:

l1 = [0]
l2 = [0]

Output:

[0]
Example 3

Input:

l1 = [9,9,9,9,9,9,9]
l2 = [9,9,9,9]

Output:

[8,9,9,9,0,0,0,1]
Approach

The solution traverses both linked lists simultaneously, adding the corresponding digits.

A carry variable is used whenever the sum of two digits is greater than or equal to 10.

For each position:

1. Start with the current carry.
2. Add the current digit from l1, if it exists.
3. Add the current digit from l2, if it exists.
4. Calculate the new digit using sum % 10.
5. Calculate the new carry using sum / 10.
6. Create a new node containing the resulting digit.
7. Move to the next nodes.

Example Walkthrough
l1 = [2,4,3]
l2 = [5,6,4]

2 + 5 = 7
→ digit = 7
→ carry = 0

4 + 6 = 10
→ digit = 0
→ carry = 1

3 + 4 + 1 = 8
→ digit = 8
→ carry = 0

Result:

[7,0,8]
Solution
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;

        int carry = 0;

        while (l1 != null || l2 != null || carry != 0) {
            int sum = carry;

            if (l1 != null) {
                sum += l1.val;
                l1 = l1.next;
            }

            if (l2 != null) {
                sum += l2.val;
                l2 = l2.next;
            }

            carry = sum / 10;
            int digit = sum % 10;

            current.next = new ListNode(digit);
            current = current.next;
        }

        return dummy.next;
    }
}
Complexity

Let n be the length of the longer linked list.

Metric	Complexity
Time	     O(n)
Space    	 O(n)

Each node is visited once, resulting in O(n) time complexity. The result linked list requires O(n) additional space.

Key Concepts
- Singly Linked Lists
- Linked List Traversal
- Carry Handling
- Mathematical Simulation
- Dummy Nodes

LeetCode
LeetCode #2 — Add Two Numbers(https://leetcode.com/problems/add-two-numbers/description/)

Language
- Java
- Java Linked List (ListNode)
