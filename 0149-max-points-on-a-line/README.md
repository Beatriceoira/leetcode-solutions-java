LeetCode #149 — Max Points on a Line


Problem

Given an array of points where:

points[i] = [xi, yi]

represents a point on the X-Y plane, return the maximum number of points that lie on the same straight line.


Examples
Example 1
Input:
points = [[1,1],[2,2],[3,3]]

Output:
3

All three points lie on the same line:

(1,1)
   \
   (2,2)
      \
      (3,3)
Example 2
Input:
points = [
    [1,1],
    [3,2],
    [5,3],
    [4,1],
    [2,3],
    [1,4]
]

Output:
4

The maximum number of points that lie on the same straight line is 4.


Approach

The solution uses a combination of:

- Pairwise slope calculation
- GCD normalization
- Hashing
- Primitive long encoding
- Custom hash table
- Early termination

For every point, we treat it as an anchor point and calculate the slope between it and every other point.

Points producing the same normalized slope must lie on the same line passing through the anchor point.


1. Choose an Anchor Point

For every point:

P[i]

we calculate its slope to every point after it.

For example:

points = [[1,1],[2,2],[3,3]]

Using [1,1] as the anchor:

[1,1] → [2,2]
[1,1] → [3,3]

Both have the same slope:

1

Therefore, all three points lie on the same line.


2. Calculate the Slope

For two points:

(x1, y1)
(x2, y2)

the slope is:

dy / dx

where:

dx = x2 - x1
dy = y2 - y1

For example:

(1,1)
(3,3)

gives:

dx = 3 - 1 = 2
dy = 3 - 1 = 2

So:

2 / 2

can be reduced to:

1 / 1


3. Normalize Using GCD

Different pairs of points can produce mathematically identical slopes.

For example:

2 / 2
4 / 4
6 / 6

all represent:

1 / 1

Therefore, the solution divides both dx and dy by their GCD.

int g = gcd(dx, dy);

dx /= g;
dy /= g;

This ensures equivalent slopes have the same representation.


4. Normalize the Sign

The following slopes are mathematically identical:

-1 / -2
1 / 2

To prevent them from being treated as different slopes, the implementation ensures that dx is positive whenever possible.

if (dx < 0) {
    dx = -dx;
    dy = -dy;
}

Therefore:

-1 / -2

becomes:

1 / 2


5. Handle Vertical Lines

Vertical lines have:

dx = 0

which would normally result in division by zero.

Instead, all vertical slopes are represented as:

0 / 1
if (dx == 0) {
    dy = 1;
}

For example:

[2,1]
[2,5]
[2,10]

all produce:

0 / 1

and therefore belong to the same slope group.


6. Handle Horizontal Lines

Horizontal lines have:

dy = 0

They are normalized to:

1 / 0
else if (dy == 0) {
    dx = 1;
}

For example:

[1,5]
[4,5]
[8,5]

all become:

1 / 0


7. Encode the Slope as a long

Instead of storing slopes as strings such as:

"1/2"

the implementation packs dx and dy into a single primitive long.

long key = ((long) dx << 32)
         | (dy & 0xffffffffL);

The 64-bit value contains:

┌───────────────┬───────────────┐
│      dx       │      dy       │
│   32 bits     │   32 bits     │
└───────────────┴───────────────┘

This avoids creating a new String for every pair of points.


8. Custom Primitive Hash Table

A normal implementation might use:

HashMap<Long, Integer>

However, Java's HashMap requires boxing:

long → Long
int  → Integer

and introduces additional object and memory overhead.

This implementation instead uses a custom:

long → int

hash table.

It stores:

long[] keys;
int[] values;
boolean[] used;

This avoids:

Long objects
Integer objects
Map.Entry objects
String objects
Additional boxing/unboxing

This is particularly useful inside the nested O(n²) loop.


9. Count Points With the Same Slope

For each anchor point, the hash table stores how many other points have the same normalized slope.

For example:

Anchor:
[1,1]

Slopes:
1/1
1/1
1/1
1/2

The map effectively stores:

1/1 → 3
1/2 → 1

The largest value represents the greatest number of other points sharing a line with the anchor.

The anchor itself is then added:

int total = localBest + 1;


10. Early Termination

The implementation also performs an early-exit optimization:

if (best >= n - i) {
    break;
}

If the current answer is already at least as large as the number of points that remain available, no later anchor can produce a better result.

For example, if:

best = 250

and there are only:

250

points remaining that could potentially form a better line, the answer cannot exceed 250.

Therefore, the remaining iterations can be skipped.

Algorithm

For every point i:

1. Create a new slope hash table.
2. Treat points[i] as the anchor.
3. Calculate (dx, dy) for every later point.
4. Normalize the slope using GCD.
5. Normalize vertical, horizontal, and negative slopes.
6. Encode the normalized slope into a long.
7. Increment the slope's frequency.
8. Track the largest frequency for this anchor.
9. Add 1 for the anchor point.
10. Update the global maximum.
11. Stop early if the answer cannot be improved.


Implementation
class Solution {
    public int maxPoints(int[][] points) {
        final int n = points.length;

        if (n <= 2) {
            return n;
        }

        int best = 2;

        for (int i = 0; i < n - 1; i++) {
            LongIntMap slopes = new LongIntMap(n << 1);

            final int x = points[i][0];
            final int y = points[i][1];

            int localBest = 0;

            for (int j = i + 1; j < n; j++) {
                int dx = points[j][0] - x;
                int dy = points[j][1] - y;

                if (dx == 0) {
                    // Vertical line
                    dy = 1;
                } else if (dy == 0) {
                    // Horizontal line
                    dx = 1;
                } else {
                    int g = gcd(dx, dy);

                    dx /= g;
                    dy /= g;

                    // Normalize the sign.
                    if (dx < 0) {
                        dx = -dx;
                        dy = -dy;
                    }
                }

                long key = ((long) dx << 32)
                         | (dy & 0xffffffffL);

                int count = slopes.increment(key);

                if (count > localBest) {
                    localBest = count;
                }
            }

            int total = localBest + 1;

            if (total > best) {
                best = total;
            }

            // Early termination.
            if (best >= n - i) {
                break;
            }
        }

        return best;
    }

    private static int gcd(int a, int b) {
        if (a < 0) a = -a;
        if (b < 0) b = -b;

        while (b != 0) {
            int temp = a % b;
            a = b;
            b = temp;
        }

        return a;
    }

    /*
     * Primitive long -> int hash table.
     *
     * Avoids HashMap<Long, Integer> boxing overhead.
     */
    private static final class LongIntMap {

        private final long[] keys;
        private final int[] values;
        private final boolean[] used;
        private final int mask;

        LongIntMap(int expectedSize) {
            int capacity = 1;

            while (capacity < expectedSize) {
                capacity <<= 1;
            }

            keys = new long[capacity];
            values = new int[capacity];
            used = new boolean[capacity];

            mask = capacity - 1;
        }

        int increment(long key) {
            int index = hash(key) & mask;

            while (used[index]) {
                if (keys[index] == key) {
                    return ++values[index];
                }

                index = (index + 1) & mask;
            }

            used[index] = true;
            keys[index] = key;
            values[index] = 1;

            return 1;
        }

        private static int hash(long x) {
            x ^= x >>> 33;
            x *= 0xff51afd7ed558ccdL;
            x ^= x >>> 33;
            x *= 0xc4ceb9fe1a85ec53L;
            x ^= x >>> 33;

            return (int) (x ^ (x >>> 32));
        }
    }
}


Why This Version Is Optimized

The implementation avoids several sources of unnecessary overhead.

Instead of:
HashMap<String, Integer>

it uses:

primitive long → primitive int
Instead of creating:
"1/2"
"1/2"
"2/3"
"-1/4"
...

it directly encodes the slope.

Instead of relying on Java's object-based hash table, it uses:
long[]
int[]
boolean[]

It also:
- Handles horizontal lines without GCD.
- Handles vertical lines without GCD.
- Uses local variables for frequently accessed coordinates.
- Uses Euclidean GCD.
- Avoids unnecessary method calls inside the hottest parts where practical.
- Uses early termination when the answer cannot improve.


Complexity

Let n be the number of points.

Time Complexity
O(n² log C)

where C represents the coordinate difference.

Because the coordinates are bounded by [-10⁴, 10⁴], the GCD operation is very small in practice.

With the fixed coordinate constraints, this is effectively:

O(n²)
Space Complexity
O(n)

The hash table stores at most n - 1 slopes for each anchor point.

Why O(n²) Is Necessary

For arbitrary points, there is no meaningful general-purpose O(n) solution.

The algorithm needs to examine relationships between pairs of points:

Point A ↔ Point B
Point A ↔ Point C
Point B ↔ Point C
...

There are approximately:

n(n - 1) / 2

pairs.

Therefore, O(n²) is the appropriate asymptotic target.

The optimization here focuses on making each pairwise operation as inexpensive as possible rather than attempting to reduce the asymptotic complexity.

Edge Cases
One Point
Input:
[[1,1]]

Output:
1
Two Points
Input:
[[1,1],[2,2]]

Output:
2

Any two points always form a straight line.

Vertical Line
Input:
[[2,1],[2,2],[2,3]]

Output:
3
Horizontal Line
Input:
[[1,5],[2,5],[3,5]]

Output:
3
Negative Slope
Input:
[[1,5],[2,3],[3,1]]

Output:
3

The normalized slope is:

-2 / 1

for each pair relative to the appropriate anchor.

All Points on One Line
Input:
[[1,1],[2,2],[3,3],[4,4]]

Output:
4


Key Concepts
- Computational Geometry
- Slope Normalization
- Greatest Common Divisor
- Hash Tables
- Primitive Data Structures
- Two-Dimensional Arrays
- Pairwise Comparison
- Coordinate Geometry
- Performance Optimization
- Early Termination


Language

Java

LeetCode

LeetCode #149 — Max Points on a Line(https://leetcode.com/problems/max-points-on-a-line/description/)
