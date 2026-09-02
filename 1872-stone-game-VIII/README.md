# LeetCode 1872 — Stone Game VIII

## Problem

Alice and Bob play a game with `n` stones arranged in a row.

Alice goes first.

While more than one stone remains, the current player:

1. Chooses an integer `x > 1`.
2. Removes the leftmost `x` stones.
3. Adds the sum of those removed stones to their score.
4. Places a new stone with that same sum on the left side.

The game continues until only one stone remains.

Alice wants to **maximize**:

```text
Alice's score - Bob's score
```

while Bob wants to **minimize** it.

Given the array `stones`, return the score difference when both players play optimally.

## Examples

### Example 1

```text
Input:
stones = [-1, 2, -3, 4, -5]

Output:
5
```

Alice removes the first four stones:

```text
-1 + 2 - 3 + 4 = 2
```

The row becomes:

```text
[2, -5]
```

Bob must remove both stones:

```text
2 + (-5) = -3
```

Therefore:

```text
Alice - Bob
= 2 - (-3)
= 5
```

### Example 2

```text
Input:
stones = [7, -6, 5, 10, 5, -2, -6]

Output:
13
```

Alice removes all stones:

```text
7 - 6 + 5 + 10 + 5 - 2 - 6 = 13
```

Bob makes no move.

Therefore:

```text
13 - 0 = 13
```

### Example 3

```text
Input:
stones = [-10, -12]

Output:
-22
```

Alice must remove both stones:

```text
-10 + (-12) = -22
```

Therefore:

```text
Alice - Bob = -22
```

# Approach

The key observation is that every move effectively works with a **prefix sum**.

Suppose:

```text
prefix[i] = stones[0] + stones[1] + ... + stones[i]
```

If a player takes the first `i + 1` stones, the newly created stone has value:

```text
prefix[i]
```

After that move, the remaining game depends only on the resulting prefix sum and the remaining suffix.

This allows the game to be transformed into a dynamic programming recurrence.

# Prefix Sum Transformation

Instead of explicitly simulating the game, we process possible prefix sums from right to left.

Initially, calculate the sum of all stones:

```java
int sum = 0;

for (int stone : stones) {
    sum += stone;
}
```

This gives:

```text
sum = prefix[n - 1]
```

The initial best score difference is:

```java
int maxDiff = sum;
```

This corresponds to Alice taking all stones immediately.


# Dynamic Programming Recurrence

For each possible split, the current player can obtain the current prefix sum, while the opponent can achieve `maxDiff` from the remaining game.

Therefore, the score difference for the current state can be represented as:

```text
current difference = prefixSum - previous difference
```

The transition becomes:

```java
maxDiff = Math.max(maxDiff, sum - maxDiff);
```

This is the central recurrence of the solution.

The loop processes the states from right to left:

```java
for (int i = stones.length - 1; i > 1; i--) {
    sum -= stones[i];
    maxDiff = Math.max(maxDiff, sum - maxDiff);
}
```

# Why Iterate Backwards?

The recurrence depends on the result of a state representing a larger prefix.

Therefore, we start with the complete prefix:

```text
prefix[n - 1]
```

and progressively remove the rightmost stone:

```text
prefix[n - 1]
prefix[n - 2]
prefix[n - 3]
...
prefix[2]
```

This lets the algorithm calculate the optimal score difference without storing a complete DP array.

# Space Optimization

A standard dynamic programming implementation could use an array:

```text
dp[i]
```

to store the best score difference for every prefix.

However, each state only needs the previously calculated optimal value.

Therefore, the entire DP array can be compressed into:

```java
int maxDiff
```

Likewise, instead of storing every prefix sum, the current prefix sum is maintained using:

```java
int sum
```

When moving to the next prefix:

```java
sum -= stones[i];
```

This reduces the auxiliary space from:

```text
O(n)
```

to:

```text
O(1)
```


# Complete Implementation

```java
class Solution {
    public int stoneGameVIII(int[] stones) {
        int sum = 0;

        for (int stone : stones) {
            sum += stone;
        }

        int maxDiff = sum;

        for (int i = stones.length - 1; i > 1; i--) {
            sum -= stones[i];
            maxDiff = Math.max(maxDiff, sum - maxDiff);
        }

        return maxDiff;
    }
}
```


# Implementation Walkthrough

### Step 1 — Calculate the Total Sum

```java
int sum = 0;

for (int stone : stones) {
    sum += stone;
}
```

This calculates the sum of all stones.

For:

```text
[-1, 2, -3, 4, -5]
```

we obtain:

```text
sum = -3
```

### Step 2 — Initialize the Best Difference

```java
int maxDiff = sum;
```

Taking the entire array is always a valid first move.

Therefore, the total sum is an initial candidate answer.


### Step 3 — Process Prefixes Backwards

```java
for (int i = stones.length - 1; i > 1; i--) {
    sum -= stones[i];
```

Removing `stones[i]` from the running sum produces the next smaller prefix sum.

For example:

```text
[-1, 2, -3, 4, -5]

Total:
-3

Remove -5:
2

Remove 4:
-2

...
```


### Step 4 — Apply the Optimal-Play Recurrence

```java
maxDiff = Math.max(maxDiff, sum - maxDiff);
```

The current player obtains the current prefix sum.

The opponent's optimal response is represented by the previous `maxDiff`.

Therefore:

```text
current score difference
= current prefix sum - opponent's best difference
```

Taking the maximum corresponds to Alice choosing the move that maximizes the final score difference.


# Complexity

Let:

```text
n = stones.length
```

### Time Complexity

```text
O(n)
```

The array is traversed twice:

1. Once to calculate the total sum.
2. Once from right to left for the DP transitions.

Therefore:

```text
O(n) + O(n) = O(n)
```



### Space Complexity

```text
O(1)
```

Only two integer variables are used for the dynamic programming state:

```java
int sum;
int maxDiff;
```

No DP array, recursion stack, collections, or auxiliary data structures are required.


# Optimization

This implementation is highly optimized for the given constraints.

### 1. No DP Array

Instead of:

```text
dp[0 ... n-1]
```

the previous result is compressed into:

```java
int maxDiff;
```

### 2. Running Prefix Sum

Instead of constructing a separate prefix-sum array, the solution updates:

```java
sum -= stones[i];
```

This avoids `O(n)` additional memory.

### 3. Single Primitive State

The entire dynamic programming process only requires:

```text
sum
maxDiff
```

### 4. No Game Simulation

The solution does not construct new stone arrays or simulate turns.

Instead, it directly evaluates the optimal score difference mathematically.


# Key Concepts

- Dynamic Programming
- Prefix Sums
- Game Theory
- Optimal Play
- Minimax Recurrence
- Bottom-Up DP
- Space Optimization
- Running Sum
- State Compression


## Key Insight

The critical observation is that the game can be reduced to choosing **prefix sums**.

Rather than simulating Alice and Bob's turns, we maintain the optimal score difference for each possible prefix and use:

```java
sum - maxDiff
```

to represent the opponent's optimal response.

Because only the previous DP result is required, the entire DP table can be compressed into a single variable.

This produces an efficient:

```text
Time:  O(n)
Space: O(1)
```

solution suitable for:

```text
n ≤ 100,000
```


## Language

Java


## LeetCode

Problem #1872 — Stone Game VIII(https://leetcode.com/problems/stone-game-viii/description/?envType=daily-question&envId=2026-09-01)
