# LeetCode #2091 — Removing Minimum and Maximum From Array


Problem

You are given a 0-indexed array of distinct integers `nums`.

The array contains:

* One element with the minimum value.
* One element with the maximum value.

A deletion can only remove an element from either:

```text
Front
```

or:

```text
Back
```

The goal is to remove both the minimum and maximum elements using the minimum possible number of deletions.

Return the minimum number of deletions required.





Examples

Example 1

```text
Input:
nums = [2,10,7,5,4,1,8,6]

Output:
5
```

The minimum is:

```text
1 at index 5
```

The maximum is:

```text
10 at index 1
```

One optimal strategy is:

```text
2 deletions from the front
3 deletions from the back
```

Total:

```text
2 + 3 = 5
```

Example 2

```text
Input:
nums = [0,-4,19,1,8,-2,-3,5]

Output:
3
```

The minimum is at index `1`.

The maximum is at index `2`.

Both can be removed by deleting the first three elements:

```text
0
-4
19
```

Therefore:

```text
Output = 3
```



Example 3

```text
Input:
nums = [101]

Output:
1
```

There is only one element.

It is simultaneously the minimum and maximum.

Therefore, only one deletion is required.

Approach

The key observation is that we **do not need to actually modify the array**.

We only need to find:

```text
minIndex
maxIndex
```

Once we know their positions, there are onlythree possible strategies.



Strategy 1 — Remove Both From the Front

Suppose:

```text
minIndex = 1
maxIndex = 5
```

To remove both from the front, we must delete everything through the farther element:

```text
max(minIndex, maxIndex) + 1
```

Therefore:

```text
front = max(minIndex, maxIndex) + 1
```



Strategy 2 — Remove Both From the Back

To remove both from the back, we again need to reach the farther element from the back.

For an array of length `n`:

```text
back = n - min(minIndex, maxIndex)
```

For example:

```text
n = 8

minIndex = 1
maxIndex = 5
```

The closer element to the back is index `5`.

Therefore:

```text
8 - 1? 
```

More precisely, because the smaller index is farther from the back:

```text
n - min(1,5)
= 8 - 1
= 7
```

So seven deletions would be required.



Strategy 3 — Remove One From Each Side

This is often the optimal solution.

We can remove:

```text
one element from the front
```

to reach one target, and:

```text
elements from the back
```

to reach the other.

There are two possible arrangements.

Minimum From Front, Maximum From Back

```text
(minIndex + 1)
+
(n - maxIndex)
```

Maximum From Front, Minimum From Back

```text
(maxIndex + 1)
+
(n - minIndex)
```

We take the smaller of the two.



Formula

Let:

```text
a = min(minIndex, maxIndex)
b = max(minIndex, maxIndex)
```

Then the three possible deletion counts are:

```text
Front:
b + 1
```

```text
Back:
n - a
```

```text
Both sides:
(a + 1) + (n - b)
```

Therefore:

```text
answer = min(
    b + 1,
    n - a,
    a + 1 + n - b
)
```



Algorithm

1. Initialize the minimum value to `Integer.MAX_VALUE`.
2. Initialize the maximum value to `Integer.MIN_VALUE`.
3. Iterate through the array once.
4. Record the indices of the minimum and maximum values.
5. Determine:

   * The larger index.
   * The smaller index.
6. Calculate:

   * Deleting from the front.
   * Deleting from the back.
   * Deleting from both sides.
7. Return the smallest value.



Implementation


class Solution {
    public int minimumDeletions(int[] nums) {
        int n = nums.length;

        int minIndex = 0;
        int maxIndex = 0;

        for (int i = 1; i < n; i++) {
            if (nums[i] < nums[minIndex]) {
                minIndex = i;
            }

            if (nums[i] > nums[maxIndex]) {
                maxIndex = i;
            }
        }

        int left = Math.min(minIndex, maxIndex);
        int right = Math.max(minIndex, maxIndex);

        // Remove both from the front.
        int removeFront = right + 1;

        // Remove both from the back.
        int removeBack = n - left;

        // Remove one from each side.
        int removeBoth =
                (left + 1) + (n - right);

        return Math.min(
                removeFront,
                Math.min(removeBack, removeBoth)
        );
    }
}





Example Walkthrough

Consider:

```text
nums = [2,10,7,5,4,1,8,6]
```

The minimum is:

```text
1
```

at:

```text
index = 5
```

The maximum is:

```text
10
```

at:

```text
index = 1
```

Therefore:

```text
left = 1
right = 5
n = 8
```




Option 1 — Front

Remove everything through index `5`:

```text
5 + 1 = 6
```

So:

```text
removeFront = 6
```




Option 2 — Back

Remove everything from index `7` down through index `1`:

```text
8 - 1 = 7
```

So:

```text
removeBack = 7
```




Option 3 — Both Sides

Remove the maximum from the front:

```text
1 + 1 = 2
```

Remove the minimum from the back:

```text
8 - 5 = 3
```

Total:

```text
2 + 3 = 5
```

Therefore:

```text
answer = min(6, 7, 5)
       = 5
```



Why This Works

Only the positions of the minimum and maximum matter.

Because deletions are restricted to the two ends of the array, every valid solution must fall into one of these categories:

```text
┌───────────────────────────────┐
│ 1. Delete from the front      │
│ 2. Delete from the back       │
│ 3. Delete from both sides     │
└───────────────────────────────┘
```

There is no fourth possibility.

For each strategy, we calculate the exact number of deletions required and choose the minimum.




Complexity

Let:

```text
n = nums.length
```


Time Complexity

We scan the array once:

```text
O(n)
```

All other operations are constant time.

Therefore:

```text
Time: O(n)
```




Space Complexity

Only a few integer variables are used:

```text
Space: O(1)
```

No additional arrays, lists, maps, or sorting are necessary.



Optimization

This solution is already **asymptotically optimal**.

The array must be examined at least once to determine which elements are the minimum and maximum.

Therefore, we cannot improve the worst-case time complexity below:

```text
O(n)
```

The implementation also uses:

* No sorting
* No extra arrays
* No `ArrayList`
* No `HashMap`
* No array modification
* One traversal
* Constant auxiliary space

This makes it both time-optimal and space-optimal for the problem constraints.



Key Concepts

* Array Traversal
* Index Tracking
* Minimum/Maximum Search
* Greedy Reasoning
* Case Analysis
* Constant Space Optimization
* Time Complexity Analysis



Language

Java



LeetCode

[LeetCode #2091 — Removing Minimum and Maximum From Array](https://leetcode.com/problems/removing-minimum-and-maximum-from-array/?envType=daily-question&envId=2026-08-30)
