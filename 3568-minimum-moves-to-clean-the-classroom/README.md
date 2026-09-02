# LeetCode 3568 — Minimum Moves to Clean the Classroom

## Problem

You are given a classroom represented by an `m × n` grid containing:

* `S` — Starting position
* `L` — Litter that must be collected
* `R` — Recharge station that restores energy to the initial value
* `X` — Blocked cell
* `.` — Empty cell

You start at `S` with a given amount of energy.

Moving to an adjacent cell costs **1 energy**. You cannot move when your energy would become negative.

When entering an `R` cell, your energy is restored to its original value.

The objective is to collect **all litter** using the minimum possible number of moves.

Return:

- The minimum number of moves required, or
- `-1` if it is impossible.



## Example

### Example 1

```text
Input:
classroom = [
    "S..",
    ".L.",
    "..R"
]
energy = 3

Output:
2
```

The robot can reach the litter in two moves.



### Example 2

```text
Input:
classroom = [
    "SXL",
    ".R.",
    "..."
]
energy = 2

Output:
-1
```

The blocked cell and limited energy prevent reaching the litter.



## Approach

The problem is a **shortest-path problem with additional state**.

A normal BFS using only:

```text
(row, column)
```

is insufficient because reaching the same cell with different:

* remaining energy
* collected litter

can lead to completely different future possibilities.

Therefore, the BFS state is conceptually:

```text
(position, collected litter, remaining energy)
```

However, storing every possible energy value would consume a large amount of memory.

The optimized solution uses **energy dominance**.

For each:

```text
(position, litter mask)
```

we only store the **maximum amount of energy** with which that state has been reached.

If we later reach the same position with the same litter mask but less or equal energy, that state can be discarded.



# 1. Encode Litter With a Bitmask

There can be at most 10 litter cells.

Each litter cell receives one bit:

```text
Litter 0 → 0000000001
Litter 1 → 0000000010
Litter 2 → 0000000100
...
```

For example, if litter `0`, `2`, and `4` have been collected:

```text
0000010101
```

When entering a litter cell:

```java
nextMask = mask | litterBit[nextPos];
```

Once:

```java
mask == fullMask
```

all litter has been collected.

Since there are at most 10 litter cells:

```text
2^10 = 1024
```

possible masks.



# 2. Store the Maximum Energy Per State

Instead of using:

```java
visited[row][column][energy][mask]
```

the solution maintains:

```java
bestEnergy[position * maskCount + mask]
```

This stores the greatest amount of energy seen for that exact:

```text
position + litter mask
```

combination.

For example:

```text
Position = 15
Mask = 010101
Energy = 20
```

Later we reach:

```text
Position = 15
Mask = 010101
Energy = 12
```

The second state is useless because the first state has more energy and can perform every action available to the second state.

Therefore:

```java
if (nextEnergy <= bestEnergy[index]) {
    continue;
}
```

prunes the dominated state.



# 3. Breadth-First Search

BFS is used because every movement costs exactly:

```text
1 move
```

Therefore, BFS explores states in increasing order of distance.

The search proceeds layer by layer:

```text
Move 0
    ↓
Move 1
    ↓
Move 2
    ↓
Move 3
    ↓
...
```

The first time all litter has been collected, the current move count is guaranteed to be minimal.



# 4. Recharge Stations

Moving onto an `R` cell still costs one move.

The energy is then restored:

```java
int nextEnergy = remainingEnergy - 1;

if (nextEnergy < 0) {
    continue;
}

if (reset[nextPos]) {
    nextEnergy = energy;
}
```

The order is important.

The move consumes energy first, then the recharge station restores it.



# 5. Flatten the Grid

Instead of repeatedly working with:

```text
row
column
```

the grid position is represented by a single integer:

```java
int pos = r * n + c;
```

For example, for a grid with 5 columns:

```text
(0,0) → 0
(0,1) → 1
(0,2) → 2
...
(1,0) → 5
(1,1) → 6
```

The row and column can be recovered with:

```java
int r = pos / n;
int c = pos - r * n;
```

This reduces array nesting and improves memory locality.



# 6. Primitive BFS Queue

A normal Java queue containing arrays such as:

```java
Queue<int[]>
```

creates many objects.

Instead, the solution stores encoded states inside:

```java
int[] queue
```

This reduces object allocation and garbage-collection overhead.

The queue automatically grows when necessary:

```java
if (tail == queue.length) {
    queue = Arrays.copyOf(queue, queue.length * 2);
}
```

This also avoids the fixed-capacity problem where the queue could exceed:

```text
cells × masks
```

because the same `(position, mask)` can be discovered multiple times with progressively better energy.



# Complete Implementation

```java
import java.util.*;

class Solution {
    public int minMoves(String[] classroom, int energy) {
        int m = classroom.length;
        int n = classroom[0].length();
        int cells = m * n;

        int start = 0;
        int litterCount = 0;

        int[] litterBit = new int[cells];
        boolean[] reset = new boolean[cells];
        boolean[] blocked = new boolean[cells];

        // Preprocess grid
        for (int r = 0; r < m; r++) {
            String row = classroom[r];

            for (int c = 0; c < n; c++) {
                int pos = r * n + c;
                char ch = row.charAt(c);

                if (ch == 'S') {
                    start = pos;
                } else if (ch == 'L') {
                    litterBit[pos] = 1 << litterCount++;
                } else if (ch == 'R') {
                    reset[pos] = true;
                } else if (ch == 'X') {
                    blocked[pos] = true;
                }
            }
        }

        if (litterCount == 0) {
            return 0;
        }

        int maskCount = 1 << litterCount;
        int fullMask = maskCount - 1;

        /*
         * Stores the maximum energy reached for each
         * position + litter mask combination.
         */
        int[] bestEnergy = new int[cells * maskCount];
        Arrays.fill(bestEnergy, -1);

        int energyStates = energy + 1;

        /*
         * State encoding:
         *
         * ((position * maskCount + mask) * energyStates)
         * + remainingEnergy
         */
        int initialState =
                (start * maskCount) * energyStates + energy;

        int[] queue = new int[1024];

        int head = 0;
        int tail = 0;

        queue[tail++] = initialState;
        bestEnergy[start * maskCount] = energy;

        int moves = 0;

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        while (head < tail) {
            int levelEnd = tail;

            while (head < levelEnd) {
                int state = queue[head++];

                int remainingEnergy = state % energyStates;
                int encoded = state / energyStates;

                int mask = encoded % maskCount;
                int pos = encoded / maskCount;

                if (mask == fullMask) {
                    return moves;
                }

                int r = pos / n;
                int c = pos - r * n;

                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d];
                    int nc = c + dc[d];

                    if (nr < 0 || nr >= m ||
                        nc < 0 || nc >= n) {
                        continue;
                    }

                    int nextPos = nr * n + nc;

                    if (blocked[nextPos]) {
                        continue;
                    }

                    int nextEnergy = remainingEnergy - 1;

                    if (nextEnergy < 0) {
                        continue;
                    }

                    // Recharge after entering R
                    if (reset[nextPos]) {
                        nextEnergy = energy;
                    }

                    int nextMask = mask | litterBit[nextPos];

                    // All litter collected
                    if (nextMask == fullMask) {
                        return moves + 1;
                    }

                    int index = nextPos * maskCount + nextMask;

                    /*
                     * Dominance pruning:
                     *
                     * If this state was already reached with
                     * equal or greater energy, it cannot improve
                     * the answer.
                     */
                    if (nextEnergy <= bestEnergy[index]) {
                        continue;
                    }

                    bestEnergy[index] = nextEnergy;

                    int nextState =
                            index * energyStates + nextEnergy;

                    // Expand queue when necessary
                    if (tail == queue.length) {
                        queue = Arrays.copyOf(
                                queue,
                                queue.length * 2
                        );
                    }

                    queue[tail++] = nextState;
                }
            }

            moves++;
        }

        return -1;
    }
}
```



# Complexity

Let:

* `M` = number of rows
* `N` = number of columns
* `L` = number of litter cells
* `E` = initial energy

with:

```text
L ≤ 10
E ≤ 50
```

### Time Complexity

Worst case:

```text
O(M × N × E × 2^L)
```

Each state has at most four possible movements.

The energy-dominance optimization eliminates states that cannot improve upon an already discovered state.



### Space Complexity

The main state structure is:

```text
O(M × N × 2^L)
```

because we store only the maximum energy for each position/mask combination.

This is significantly smaller than explicitly storing:

```text
O(M × N × E × 2^L)
```

visited states.

Additional space is used by the BFS queue and preprocessing arrays.



# Why This Version Is Optimized

The implementation combines several optimizations:

### 1. BFS

Guarantees the minimum number of moves.

### 2. Bitmask

Represents up to 10 litter cells using a single integer.

### 3. Energy Dominance

Eliminates states where the same position and litter collection status have already been reached with more energy.

### 4. Flattened Arrays

Uses:

```java
int[]
boolean[]
```

instead of heavily nested multidimensional arrays.

### 5. Primitive Queue

Uses:

```java
int[]
```

instead of `Queue<int[]>` or boxed `Integer` objects.

### 6. Preprocessing

Litter bits, recharge cells, and blocked cells are precomputed before BFS.

### 7. Early Exit

The algorithm immediately returns when the final litter is collected:

```java
if (nextMask == fullMask) {
    return moves + 1;
}
```

This avoids unnecessary exploration after reaching the objective.



# Key Concepts

- Breadth-First Search (BFS)
- Bitmasking
- State-Space Search
- Dominance Pruning
- Shortest Path
- Dynamic State Representation
- Grid Traversal
- Memory Optimization
- Primitive Arrays
- State Encoding



# Important Optimization Insight

The most important optimization is recognizing that:

```text
(position, mask, 20 energy)
```

always dominates:

```text
(position, mask, 10 energy)
```

because both states have exactly the same future objectives, but the first state has more resources available.

Therefore, we don't need to remember every energy value independently.

Instead:

```text
bestEnergy[position][mask]
```

is sufficient.

This dramatically reduces the memory footprint compared with a traditional four-dimensional visited array.



## Language

Java



## LeetCode

Problem #3568 — Minimum Moves to Clean the Classroom(https://leetcode.com/problems/minimum-moves-to-clean-the-classroom/description/?envType=daily-question&envId=2026-09-01)
