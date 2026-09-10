# LeetCode #2265 — Count Nodes Equal to Average of Subtree

## Problem

Given the root of a binary tree, return the number of nodes where the value of the node is equal to the average of the values in its subtree.

The average is calculated as:

```text
sum of subtree values / number of nodes in subtree
```

and is rounded down to the nearest integer.

A node's subtree includes the node itself and all of its descendants.

### Examples

```text
Input: root = [4,8,5,0,1,null,6]

Output: 5
```

Explanation:

- Node `4`: `(4 + 8 + 5 + 0 + 1 + 6) / 6 = 4`
- Node `5`: `(5 + 6) / 2 = 5`
- Node `0`: `0 / 1 = 0`
- Node `1`: `1 / 1 = 1`
- Node `6`: `6 / 1 = 6`

Therefore, the answer is `5`.

```text
Input: root = [1]

Output: 1
```

The only node has an average of `1`, which equals its value.

## Approach

A postorder DFS is used so that each node can obtain information about its children before calculating its own subtree.

For every node, we need two pieces of information:
1. The sum of all values in its subtree.
2. The number of nodes in its subtree.

Normally, these two values could be returned using an `int[]`, such as:

```text
[sum, size]
```

However, creating an array for every recursive call introduces unnecessary object allocations.

Instead, this solution packs both values into a single `long`.

### Packing the Values

The `long` is divided into two 32-bit portions:

```text
63                       32 31                        0
+-------------------------+----------------------------+
|       subtree sum       |       subtree size         |
+-------------------------+----------------------------+
```

The subtree sum is stored in the upper 32 bits, while the subtree size is stored in the lower 32 bits.

The values are packed using:

```java
((long) sum << 32) | (size & 0xFFFFFFFFL)
```

### Extracting the Values

The subtree size is extracted using:

```java
(int) packed
```

The subtree sum is extracted using:

```java
(int) (packed >>> 32)
```

This allows the recursive function to return both values without allocating an array or helper object.

## Algorithm

For every node:

1. Recursively calculate the packed subtree information for the left child.
2. Recursively calculate the packed subtree information for the right child.
3. Extract the left and right subtree sizes.
4. Extract the left and right subtree sums.
5. Calculate:

```text
size = leftSize + rightSize + 1
sum  = leftSum + rightSum + node.val
```

6. Check whether:

```text
sum / size == node.val
```

7. If true, increment `count`.
8. Pack `sum` and `size` into a single `long`.
9. Return the packed value to the parent.

## Walkthrough

Consider:

```text
    5
     \
      6
```

### Node 6

Node `6` has no children.

```text
sum = 6
size = 1
```

It satisfies:

```text
6 / 1 = 6
```

so:

```text
count = 1
```

The values are then packed into a `long`.

### Node 5

The right subtree provides:

```text
sum = 6
size = 1
```

The left subtree is empty.

Therefore:

```text
sum = 5 + 6
    = 11

size = 1 + 1
     = 2
```

Average:

```text
11 / 2 = 5
```

Because integer division rounds down:

```text
5 == node.val
```

so:

```text
count = 2
```

## Correctness

For every node, the DFS first obtains the complete sum and size of both child subtrees.

The current node is then added:

```text
subtree sum = left sum + right sum + node value
subtree size = left size + right size + 1
```

Therefore, the calculated sum and size represent the entire subtree rooted at the current node.

The condition:

```text
sum / size == node.val
```

is exactly the definition of a node whose value equals the floor of the average of its subtree.

Since every node is visited exactly once, every qualifying node is counted exactly once.

Therefore, the algorithm correctly returns the number of nodes whose value equals their subtree average.

## Complete Implementation

```java
class Solution {
    private int count;

    public int averageOfSubtree(TreeNode root) {
        count = 0;
        dfs(root);
        return count;
    }

    private long dfs(TreeNode node) {
        if (node == null) return 0L;

        long left = dfs(node.left);
        long right = dfs(node.right);

        int leftSize = (int) left;
        int rightSize = (int) right;

        int size = leftSize + rightSize + 1;
        int sum = (int) (left >>> 32) + (int) (right >>> 32) + node.val;

        if (sum / size == node.val) {
            count++;
        }

        return ((long) sum << 32) | (size & 0xFFFFFFFFL);
    }
}
```

## Complexity

Let `n` be the number of nodes and `h` be the height of the tree.

### Time Complexity

```text
O(n)
```

Every node is visited exactly once, with constant-time arithmetic performed at each node.

### Space Complexity

```text
O(h)
```

The recursion stack contains at most one frame per level of the tree.

- Balanced tree: `O(log n)`
- Worst-case skewed tree: `O(n)`

No arrays, lists, maps, or helper objects are allocated for storing subtree information.

## Optimization

This implementation specifically optimizes the usual postorder solution by avoiding an `int[]` allocation for every node.

### Conventional approach

A typical implementation returns:

```java
return new int[]{sum, size};
```

This creates an array for each non-null node.

### This implementation

Instead, both values are encoded into one primitive:

```java
long
```

The upper 32 bits contain the sum:

```java
(long) sum << 32
```

The lower 32 bits contain the subtree size:

```java
size & 0xFFFFFFFFL
```

This eliminates per-node array allocations and reduces object/heap overhead.

## Why `int` Is Safe

The constraints specify:

```text
Number of nodes <= 1000
Node value <= 1000
```

Therefore, the maximum possible subtree sum is:

```text
1000 × 1000 = 1,000,000
```

which easily fits inside a Java `int`.

The subtree size is also at most:

```text
1000
```

so it safely fits in 32 bits as well.

The `long` is used primarily as a packing container, not because the problem requires 64-bit arithmetic for the sum.

## Key Concepts

- Binary tree traversal
- Postorder DFS
- Subtree aggregation
- Integer division
- Bit manipulation
- Bit packing
- Primitive data optimization
- Recursion
- Constant-time subtree calculations

## Language

Java

## LeetCode 

https://leetcode.com/problems/count-nodes-equal-to-average-of-subtree/description/?envType=daily-question&envId=2026-09-10