LeetCode #3418 — Maximum Amount of Money Robot Can Earn


Problem

You are given an m x n grid where a robot starts at the top-left corner:

(0, 0)

and must reach the bottom-right corner:

(m - 1, n - 1)

The robot can only move:

Right
Down

Each cell contains a value:

If coins[i][j] >= 0, the robot gains that many coins.
If coins[i][j] < 0, a robber steals |coins[i][j]| coins.
The robot can neutralize robbers in at most 2 cells, preventing those losses.

The final amount of money may be negative.

Return the maximum amount of money the robot can earn.


Examples
Example 1
Input:
coins = [
    [0,  1, -1],
    [1, -2,  3],
    [2, -3,  4]
]

Output:
8

One optimal path is:

(0,0) → (0,1) → (1,1) → (1,2) → (2,2)

The robot neutralizes the robber at -2.

The total becomes:

0 + 1 + 0 + 3 + 4 = 8

Example 2
Input:
coins = [
    [10,10,10],
    [10,10,10]
]

Output:
40

There are no robbers, so the robot simply collects every value along the optimal path.

Example 3
Input:
coins = [
    [1,7,28,19,10]
]

Output:
1 + 7 + 28 + 19 + 10
= 65

Since all values are positive, no neutralizations are necessary.


Approach

This problem is solved using Dynamic Programming.

The important part is that the robot has a limited resource:

2 neutralizations

Therefore, reaching the same cell can have different values depending on how many neutralizations have already been used.

We maintain three DP states:

dp0 → maximum profit using 0 neutralizations
dp1 → maximum profit using 1 neutralization
dp2 → maximum profit using 2 neutralizations


DP State

For each cell (i, j):

dp0[j]

represents the maximum amount obtainable when reaching the current cell after using 0 neutralizations.

dp1[j]

represents the maximum amount after using 1 neutralization.

dp2[j]

represents the maximum amount after using 2 neutralizations.

The robot can arrive at a cell from either:

Top    → (i - 1, j)
Left   → (i, j - 1)


Normal Transition

If the current cell contains:

coins[i][j] = value

and we do not neutralize it, then:

newState = previousState + value

For example:

dp0 = max(top0, left0) + value

Similarly:

dp1 = max(top1, left1) + value
dp2 = max(top2, left2) + value
Neutralization Transition

If:

value < 0

the robot has another option.

Instead of losing the value, it can neutralize the robber.

For example, if the robot previously used zero neutralizations:

dp0

it can neutralize the current robber and transition into:

dp1

without adding the negative value.

Therefore:

dp1 = max(
    normal dp1 transition,
    best previous dp0
)

Likewise, if one neutralization has already been used:

dp2 = max(
    normal dp2 transition,
    best previous dp1
)


Example of Neutralization

Suppose:

current cell = -5

Without neutralization:

profit = previous - 5

With neutralization:

profit = previous

The neutralization effectively changes:

-5

into:

0

for that cell.


Why Three States Are Enough

The robot can neutralize robbers at most twice.

Therefore, there is no reason to track:

3 neutralizations
4 neutralizations
5 neutralizations
...

Only three states are possible:

0 neutralizations
1 neutralization
2 neutralizations

This keeps the DP state very small.


Space Optimization

A straightforward implementation could use:

dp[m][n][3]

This requires:

O(m × n)

memory.

However, each cell only depends on:

Top
Left

Therefore, we do not need the entire 2D DP table.

Instead, we maintain three 1D arrays:

dp0[n]
dp1[n]
dp2[n]

These arrays represent the current row while retaining the previous-row values.

This reduces memory from:

O(m × n)

to:

O(n)
Rolling Array Technique

Before processing:

(i, j)

dp[j] represents the value from the cell directly above.

Meanwhile:

dp[j - 1]

represents the cell directly to the left.

Therefore:

dp[j]       → Top
dp[j - 1]   → Left

This allows the entire 2D DP table to be compressed into three arrays.


Algorithm

1. Create three arrays:

dp0
dp1
dp2
2. Initialize all unreachable states to a very small negative value.
3. Initialize (0,0).
4. Iterate through every cell.
5. Find the best path from the top and left.
6. Apply the normal transition.
7. If the cell contains a robber:
8. Try neutralizing it using the first available neutralization.
9. Try using the second available neutralization.
10. Continue until reaching the bottom-right cell.

Return the maximum of:

dp0
dp1
dp2


Implementation
import java.util.Arrays;

class Solution {
    public int maximumAmount(int[][] coins) {
        int m = coins.length;
        int n = coins[0].length;

        final int NEG = Integer.MIN_VALUE / 4;

        // dp0 = 0 neutralizations used
        // dp1 = 1 neutralization used
        // dp2 = 2 neutralizations used
        int[] dp0 = new int[n];
        int[] dp1 = new int[n];
        int[] dp2 = new int[n];

        Arrays.fill(dp0, NEG);
        Arrays.fill(dp1, NEG);
        Arrays.fill(dp2, NEG);

        // Starting cell.
        dp0[0] = coins[0][0];

        // The starting cell can also be neutralized.
        if (coins[0][0] < 0) {
            dp1[0] = 0;
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                if (i == 0 && j == 0) {
                    continue;
                }

                int value = coins[i][j];

                // Values before overwriting dp[j].
                int fromTop0 = dp0[j];
                int fromTop1 = dp1[j];
                int fromTop2 = dp2[j];

                int fromLeft0 = j > 0 ? dp0[j - 1] : NEG;
                int fromLeft1 = j > 0 ? dp1[j - 1] : NEG;
                int fromLeft2 = j > 0 ? dp2[j - 1] : NEG;

                // Best normal transitions.
                int best0 = Math.max(fromTop0, fromLeft0);
                int best1 = Math.max(fromTop1, fromLeft1);
                int best2 = Math.max(fromTop2, fromLeft2);

                dp0[j] = best0 + value;
                dp1[j] = best1 + value;
                dp2[j] = best2 + value;

                // Try neutralizing the current robber.
                if (value < 0) {

                    // Use the first neutralization.
                    dp1[j] = Math.max(
                        dp1[j],
                        best0
                    );

                    // Use the second neutralization.
                    dp2[j] = Math.max(
                        dp2[j],
                        best1
                    );
                }
            }
        }

        return Math.max(
            dp0[n - 1],
            Math.max(dp1[n - 1], dp2[n - 1])
        );
    }
}
Example Walkthrough

Consider:

coins = [
    [0, 1, -1],
    [1,-2,  3],
    [2,-3,  4]
]

The robot wants to maximize:

profit

while using at most two neutralizations.

One optimal path is:

0 → 1 → -2 → 3 → 4

Without neutralization:

0 + 1 - 2 + 3 + 4
= 6

Neutralizing -2 gives:

0 + 1 + 0 + 3 + 4
= 8

Therefore:

Output = 8
Handling Negative Results

The answer is allowed to be negative.

For example:

coins = [
    [-5]
]

The robot can neutralize the robber:

-5 → 0

so the answer is:

0

If neutralization were unavailable, the result would be:

-5

The DP therefore must not initialize states to 0, because 0 could incorrectly represent an unreachable path.

Instead, unreachable states use:

Integer.MIN_VALUE / 4

as a negative sentinel.

Why Not Initialize DP to Zero?

This is an important detail.

Consider:

coins = [
    [-5, -5]
]

If every DP state started at:

0

the algorithm could accidentally treat unreachable states as valid paths.

That could produce an incorrect result.

Using:

final int NEG = Integer.MIN_VALUE / 4;

ensures that only actually reachable states participate in the maximum calculations.

Why Integer.MIN_VALUE / 4?

We eventually perform operations such as:

previous + value

If we used:

Integer.MIN_VALUE

directly, adding a negative number could cause integer overflow.

Using:

Integer.MIN_VALUE / 4

provides a sufficiently small sentinel while leaving plenty of room for arithmetic.

Complexity

Let:

m = number of rows
n = number of columns

Every cell is processed exactly once.

Time Complexity
O(m × n)

For the maximum constraints:

500 × 500 = 250,000 cells

so the algorithm performs only a linear number of operations relative to the grid size.

Space Complexity

The solution uses three arrays of length n:

dp0[n]
dp1[n]
dp2[n]

Therefore:

O(n)

auxiliary space.

Optimization

A conventional 3D DP solution:

dp[m][n][3]

would require:

O(m × n)

memory.

This implementation compresses the state into:

dp0[n]
dp1[n]
dp2[n]

resulting in:

Time:  O(m × n)
Space: O(n)

The time complexity is already asymptotically optimal because every cell can potentially affect the final answer.

Edge Cases
Single Positive Cell
Input:
coins = [[10]]

Output:
10
Single Negative Cell
Input:
coins = [[-10]]

Output:
0

The robot uses one neutralization.

More Than Two Robbers

The robot can only neutralize two of them.

For example:

[-10, -20, -30]

Only two negative cells can be neutralized.

The third must still subtract its value.

All Positive Values

No neutralizations are necessary.

The problem becomes a standard maximum-sum path DP.

All Negative Values

The algorithm must decide:

1. Which path to take.
2. Which two robbers to neutralize.

The three-state DP handles both decisions simultaneously.


Key Concepts
- Dynamic Programming
- Grid DP
- State Compression
- Rolling Arrays
- Maximum Path Sum
- State Transitions
- Greedy Resource Allocation
- Memory Optimization
- Negative Value Handling
- Sentinel Values


Language

Java

LeetCode

LeetCode #3418 — Maximum Amount of Money Robot Can Earn(https://leetcode.com/problems/maximum-amount-of-money-robot-can-earn/?envType=daily-question&envId=2026-08-29)