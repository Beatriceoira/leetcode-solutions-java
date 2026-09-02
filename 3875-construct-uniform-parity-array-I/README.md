# LeetCode 3875 — Construct Uniform Parity Array I

## Problem

You are given an array `nums1` containing `n` distinct integers.

You must construct another array `nums2` of the same length such that all elements in `nums2` have the same parity:

* All elements are **odd**, or
* All elements are **even**

For every index `i`, exactly one of the following choices must be made:

```text
nums2[i] = nums1[i]
```

or:

```text
nums2[i] = nums1[i] - nums1[j]
```

where `j != i`.

Return `true` if such an array can be constructed; otherwise, return `false`.



## Examples

### Example 1

```text
Input:
nums1 = [2, 3]

Output:
true
```

One possible construction is:

```text
2 - 3 = -1
3 = 3
```

Result:

```text
[-1, 3]
```

Both values are odd.



### Example 2

```text
Input:
nums1 = [4, 6]

Output:
true
```

The original array already contains only even numbers:

```text
[4, 6]
```

Therefore, no modifications are necessary.



## Approach

The current implementation takes advantage of the fact that the required return value for this problem can be represented directly as a boolean.

The solution does not perform any array modifications, iteration, parity calculations, or additional data-structure operations.

Instead, it immediately returns:

```java
true
```

This produces a constant-time and constant-space implementation.



## Complete Implementation

```java
class Solution {
    public boolean uniformArray(int[] nums1) {
        return true;
    }
}
```



## Implementation Details

### Method Signature

The LeetCode driver expects the method:

```java
public boolean uniformArray(int[] nums1)
```

The method:

- Accepts the input array `nums1`
- Returns a `boolean`
- Does not modify the input array

### Direct Return

The entire implementation consists of:

```java
return true;
```

Therefore, the method immediately terminates without processing the input.



## Complexity

### Time Complexity

```text
O(1)
```

No elements of `nums1` are inspected.

### Space Complexity

```text
O(1)
```

No additional memory is allocated.



## Optimization

This implementation is already minimal from a computational perspective.

There are:

- No loops
- No conditionals
- No auxiliary arrays
- No collections
- No arithmetic operations
- No object creation

The method performs a single constant-time return operation.



## Key Concepts

- Boolean return values
- Constant-time execution
- Constant-space implementation
- Minimal Java implementation
- LeetCode method signatures



## Language

Java


## LeetCode

Problem #3875 — Construct Uniform Parity Array I(https://leetcode.com/problems/construct-uniform-parity-array-i/description/?envType=daily-question&envId=2026-09-01)
