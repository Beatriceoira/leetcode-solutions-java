# LeetCode #118 — Pascal's Triangle

## Problem

Given an integer `numRows`, return the first `numRows` of Pascal's Triangle.

In Pascal's Triangle, each number is the sum of the two numbers directly above it.

### Example 1

```text
Input:  numRows = 5

Output:
[
     [1],
    [1,1],
   [1,2,1],
  [1,3,3,1],
 [1,4,6,4,1]
]
```

### Example 2

```text
Input:  numRows = 1

Output:
[
    [1]
]
```


## Approach

The solution builds Pascal's Triangle row by row.

Each row:

- Always begins with `1`.
- Always ends with `1` (except the first row, which contains only `1`).
- Every middle element is calculated from the two adjacent elements in the previous row.

For example:

```text
Previous row:  [1, 3, 3, 1]

Current row:   [1, 4, 6, 4, 1]
                    ↑
                  3 + 3
```

The previous row is retrieved directly from the already-built `triangle`, avoiding the need for recursion or an additional copy of the triangle.

---

## Algorithm

1. Create an `ArrayList` named `triangle` with an initial capacity of `numRows`.
2. Iterate from `row = 0` to `numRows - 1`.
3. Create the current row with capacity `row + 1`.
4. Add `1` as the first element.
5. If this isn't the first row:

   - Retrieve the previous row.
   - Calculate each middle element using:

     ```text
     previous[i - 1] + previous[i]
     ```
   - Add `1` as the final element.
6. Add the completed row to `triangle`.
7. Return the complete triangle.

## Walkthrough

For:

```text
numRows = 5
```

### Row 0

```text
[1]
```

### Row 1

```text
[1, 1]
```

### Row 2

Previous:

```text
[1, 1]
```

Middle:

```text
1 + 1 = 2
```

Result:

```text
[1, 2, 1]
```

### Row 3

Previous:

```text
[1, 2, 1]
```

Middle values:

```text
1 + 2 = 3
2 + 1 = 3
```

Result:

```text
[1, 3, 3, 1]
```

### Row 4

Previous:

```text
[1, 3, 3, 1]
```

Middle values:

```text
1 + 3 = 4
3 + 3 = 6
3 + 1 = 4
```

Result:

```text
[1, 4, 6, 4, 1]
```

Final triangle:

```text
[
    [1],
    [1,1],
    [1,2,1],
    [1,3,3,1],
    [1,4,6,4,1]
]
```

---

## Correctness

For every row, the first and last elements are `1`, which is a defining property of Pascal's Triangle.

For every middle position `i`, the solution calculates:

```text
previous[i - 1] + previous[i]
```

These are exactly the two values directly above the current position in Pascal's Triangle.

Therefore, every generated row is correct, and since rows are generated sequentially from the first row through `numRows`, the returned structure contains exactly the first `numRows` rows of Pascal's Triangle.

## Complete Implementation

```java
import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> triangle = new ArrayList<>(numRows);

        for (int row = 0; row < numRows; row++) {
            List<Integer> current = new ArrayList<>(row + 1);
            current.add(1);

            if (row > 0) {
                List<Integer> previous = triangle.get(row - 1);

                for (int i = 1; i < row; i++) {
                    current.add(previous.get(i - 1) + previous.get(i));
                }

                current.add(1);
            }

            triangle.add(current);
        }

        return triangle;
    }
}
```

## Complexity

Let `n = numRows`.

### Time Complexity

```text
O(n²)
```

The triangle contains:

```text
1 + 2 + 3 + ... + n
```

elements, which is:

```text
n(n + 1) / 2
```

Therefore, generating the output requires `O(n²)` time.

### Space Complexity

```text
O(n²)
```

The returned triangle itself contains `O(n²)` elements.

### Auxiliary Space

```text
O(1)
```

No separate data structure is used to calculate the triangle. The `previous` reference only points to an existing row in the output.

## Optimization

The implementation is optimized around the required `List<List<Integer>>` return type.

### 1. Preallocate the outer list

```java
new ArrayList<>(numRows)
```

This prevents unnecessary resizing as rows are added.

### 2. Preallocate every row

```java
new ArrayList<>(row + 1)
```

Each row knows its exact required capacity.

### 3. Build incrementally

Only the previous row is needed to calculate the middle values of the current row.

### 4. No recursion

The solution avoids recursive calls and therefore avoids recursion-stack overhead.

### 5. No duplicate triangle

The previous row is accessed directly:

```java
List<Integer> previous = triangle.get(row - 1);
```

No additional copy of the triangle is created.

### 6. Output-optimal

Because the problem requires returning the complete triangle, `O(n²)` output space is unavoidable. The solution uses only the required output structure without maintaining another copy.


## Key Concepts

- Pascal's Triangle
- Dynamic construction
= ArrayList
- Nested Lists
= Previous-row dependency
- Incremental computation
- Output-space optimization

## Language

Java

## Leetcode
Sidenote: There's room for improvement for this code.

https://leetcode.com/problems/pascals-triangle/description/