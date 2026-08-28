LeetCode #1 — Two Sum

A Java solution to LeetCode Problem #1: Two Sum, using a HashMap to achieve O(n) time complexity.

Problem

Given an array of integers `nums` and an integer `target`, return the indices of the two numbers that add up to `target`.

Each input is guaranteed to have exactly one solution, and the same element cannot be used twice.

Example

Input:

```text
nums = [2, 7, 11, 15]
target = 9
```

Output:

```text
[0, 1]
```

Because:

```text
nums[0] + nums[1] = 2 + 7 = 9
```

Approach

The solution uses a HashMap to store numbers that have already been visited along with their indices.

For each number:

1. Calculate the value needed to reach the target:
   `needed = target - nums[i]`
2. Check if `needed` already exists in the HashMap.
3. If it exists, return its stored index and the current index.
4. Otherwise, store the current number and its index.

Example Walkthrough

```text
nums = [2, 7, 11, 15]
target = 9

Current: 2
Needed: 7
7 not found → store 2 → index 0

Current: 7
Needed: 2
2 found → return [0, 1]
```

Solution

```java
import java.util.HashMap;

class Solution {
    public int[] twoSum(int[] nums, int target) {
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int needed = target - nums[i];

            if (map.containsKey(needed)) {
                return new int[] { map.get(needed), i };
            }

            map.put(nums[i], i);
        }

        return new int[] {};
    }
}
```

Complexity

| Metric | Complexity |
| ------ | ---------- |
| Time   |   O(n)     |
| Space  |   O(n)     |

The HashMap allows us to search for the required value in O(1) average time, avoiding the O(n²) nested-loop approach.

Key Concept

HashMap / Hash Table

This problem demonstrates how a HashMap can be used to efficiently find complementary values while iterating through an array.

LeetCode

[LeetCode #1 — Two Sum](https://leetcode.com/problems/two-sum/](https://leetcode.com/problems/two-sum/description/))

Language

* Java
* Java Collections Framework (`HashMap`)
