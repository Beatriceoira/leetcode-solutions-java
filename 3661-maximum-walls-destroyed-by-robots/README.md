LeetCode #3661 — Maximum Walls Destroyed by Robots


Problem

You are given an endless straight line containing robots and walls.

Three arrays describe the environment:

robots[i]   = position of the ith robot
distance[i] = maximum distance the ith robot's bullet can travel
walls[j]    = position of the jth wall

Each robot has exactly one bullet and can fire:

Left

or:

Right

up to its maximum distance.

A bullet destroys every wall in its path within its range.

However, robots are obstacles. If a bullet encounters another robot before reaching a wall, the bullet immediately stops.

Return the maximum number of unique walls that can be destroyed.

Important Rules
Each Robot Fires At Most Once

Every robot has one bullet and can choose only one direction:

Left

or:

Right
Robots Block Bullets

Suppose two robots are located at:

2 ---------------- 10
R                     R

A bullet fired right from robot 2 cannot pass robot 10.

Likewise, a bullet fired left from robot 10 cannot pass robot 2.

This means neighboring robots determine the maximum useful firing interval.

Walls and Robots Can Share a Position

A wall may occupy the same coordinate as a robot.

That wall can still be destroyed by the robot located there.

Therefore, the robot's own position is included in its firing range.


Examples
Example 1
Input:
robots = [4]
distance = [3]
walls = [1,10]

Output:
1

The robot at 4 fires left:

[1,4]

and destroys the wall at:

1

The wall at 10 is too far away.

Therefore:

Output = 1

Example 2
Input:
robots = [10,2]
distance = [5,1]
walls = [5,2,7]

Output:
3

Sort the robots by position:

robot   distance
2       1
10      5

Robot 2 fires left:

[1,2]

and destroys:

2

Robot 10 fires left:

[5,10]

and destroys:

5, 7

Total:

1 + 2 = 3

Example 3
Input:
robots = [1,2]
distance = [100,1]
walls = [10]

Output:
0

Robot 1 has enough distance to reach wall 10.

However, firing right would encounter robot 2 first:

1 → 2 → 10
    robot

The bullet stops at robot 2.

Robot 2 cannot reach wall 10 because its distance is only 1.

Therefore:

Output = 0
Approach

The solution combines:

- Sorting
- Binary Search
- Interval Counting
- Dynamic Programming
- Overlap Handling

The main idea is to sort the robots by position.

After sorting, every robot has at most two relevant neighboring robots:

Previous Robot ← Current Robot → Next Robot

These neighboring robots determine how far a bullet can actually travel.

Step 1 — Sort the Robots

The input arrays do not necessarily provide robots in positional order.

For example:

robots = [10,2]
distance = [5,1]

We need to keep each distance attached to its robot.

We create:

[value, distance]

pairs:

[10,5]
[2,1]

Then sort by position:

[2,1]
[10,5]

This lets us process robots from left to right.

Step 2 — Calculate the Left Firing Interval

For a robot at position:

x

with shooting distance:

d

its theoretical left range is:

[x - d, x]

However, if there is a previous robot, that robot blocks the bullet.

Therefore, the actual range becomes:

[max(x - d, previousRobot + 1), x]

The +1 is important because the bullet cannot pass through the previous robot.

Step 3 — Calculate the Right Firing Interval

Similarly, the theoretical right range is:

[x, x + d]

But the next robot blocks the shot.

Therefore:

[x, min(x + d, nextRobot - 1)]

The -1 ensures the bullet does not pass through the next robot.

Why +1 and -1 Matter

Suppose robots are at:

2 ---------------- 10

A bullet fired from 2 to the right cannot reach beyond robot 10.

The valid wall positions are therefore:

2 <= wall < 10

which can be represented as:

[2, 9]

Similarly, a bullet fired left from 10 has:

2 < wall <= 10

or:

[3, 10]

This correctly models robots as blocking obstacles while still allowing walls to exist at robot positions.

Step 4 — Count Reachable Walls

The walls are sorted.

For each firing interval, we use binary search to count how many walls fall inside it.

Two binary-search functions are used:

lowerBound
upperBound
lowerBound

Returns the first position where:

value >= target
upperBound

Returns the first position where:

value > target

Therefore, the number of walls in:

[left, right]

is:

upperBound(right) - lowerBound(left)

This avoids scanning all walls for every robot.

Step 5 — The Overlap Problem

The main difficulty is that two robots can destroy the same wall.

The only relevant overlap occurs when:

Robot A → fires RIGHT

Robot B ← fires LEFT

For example:

A ---------------- B
→ → → → → → → → ← ←

Both bullets can potentially destroy walls between the two robots.

If we simply add both counts:

wallsFromA + wallsFromB

some walls may be counted twice.

Therefore, the DP must subtract their overlap.

DP States

For each robot, we consider two possibilities:

LEFT
RIGHT

We maintain:

dpLeft

and:

dpRight

where:

dpLeft

is the maximum number of unique walls destroyed after processing the previous robots when the previous robot fires left.

And:

dpRight

is the maximum number of unique walls destroyed when the previous robot fires right.

DP Transition

For the current robot, there are two choices.

Current Robot Fires Left

If the previous robot fired left:

previous LEFT
current LEFT

their ranges do not overlap in the problematic region.

Therefore:

dpLeft + currentLeft

is valid.

If the previous robot fired right:

previous RIGHT
current LEFT

their ranges can overlap.

Therefore:

dpRight + currentLeft - overlap

is used.

So:

newLeft = max(
    dpLeft + currentLeft,
    dpRight + currentLeft - overlap
)
Current Robot Fires Right

If the current robot fires right, its useful interval extends toward the next robot.

It does not create the same type of overlap with the previous robot's left-facing interval.

Therefore:

newRight = max(
    dpLeft + currentRight,
    dpRight + currentRight
)
Overlap Calculation

For two neighboring robots:

leftRobot
rightRobot

the possible overlap is the intersection between:

leftRobot shooting RIGHT

and:

rightRobot shooting LEFT

The overlap boundaries are:

start = max(
    leftRobot,
    rightRobot - rightDistance
)

and:

end = min(
    leftRobot + leftDistance,
    rightRobot
)

Because the robots themselves block the bullets, only walls strictly between them are considered for the overlap:

leftRobot < wall < rightRobot

The overlap is then counted using binary search.

Algorithm
1. Combine each robot's position with its shooting distance.
2. Sort robots by position.
3. Sort all walls.
4. For every robot:
- Calculate its reachable left interval.
- Calculate its reachable right interval.
- Count the walls in each interval using binary search.
5. Initialize the DP using the first robot.
6. Process the remaining robots from left to right.
7. For each robot:
- Calculate the overlap with the previous robot.
- Compute the best result if firing left.
- Compute the best result if firing right.
8. Return the larger of the two final states.


Implementation
import java.util.Arrays;

class Solution {
    public int maxWalls(int[] robots, int[] distance, int[] walls) {
        int n = robots.length;

        // Store robot position and distance together.
        int[][] r = new int[n][2];

        for (int i = 0; i < n; i++) {
            r[i][0] = robots[i];
            r[i][1] = distance[i];
        }

        // Sort robots by position.
        Arrays.sort(r, (a, b) -> Integer.compare(a[0], b[0]));

        // Sort walls.
        Arrays.sort(walls);

        int[] left = new int[n];
        int[] right = new int[n];

        // Calculate reachable walls for each direction.
        for (int i = 0; i < n; i++) {
            int pos = r[i][0];
            int d = r[i][1];

            /*
             * LEFT
             *
             * The previous robot blocks the shot.
             */
            int leftStart = pos - d;

            if (i > 0) {
                leftStart = Math.max(
                    leftStart,
                    r[i - 1][0] + 1
                );
            }

            int leftEnd = pos;

            left[i] =
                upperBound(walls, leftEnd)
                - lowerBound(walls, leftStart);

            /*
             * RIGHT
             *
             * The next robot blocks the shot.
             */
            int rightStart = pos;
            int rightEnd = pos + d;

            if (i < n - 1) {
                rightEnd = Math.min(
                    rightEnd,
                    r[i + 1][0] - 1
                );
            }

            right[i] =
                upperBound(walls, rightEnd)
                - lowerBound(walls, rightStart);
        }

        /*
         * DP states:
         *
         * dpLeft:
         * Previous robot fired LEFT.
         *
         * dpRight:
         * Previous robot fired RIGHT.
         */
        int dpLeft = left[0];
        int dpRight = right[0];

        for (int i = 1; i < n; i++) {

            /*
             * The only possible duplicate-wall situation
             * between neighboring robots occurs when:
             *
             * previous robot fires RIGHT
             * current robot fires LEFT
             */
            int overlap = countOverlap(
                r[i - 1][0],
                r[i - 1][1],
                r[i][0],
                r[i][1],
                walls
            );

            int newLeft = Math.max(
                dpLeft + left[i],
                dpRight + left[i] - overlap
            );

            int newRight = Math.max(
                dpLeft + right[i],
                dpRight + right[i]
            );

            dpLeft = newLeft;
            dpRight = newRight;
        }

        return Math.max(dpLeft, dpRight);
    }

    /*
     * Count walls shared by:
     *
     * leftRobot shooting RIGHT
     * rightRobot shooting LEFT
     */
    private int countOverlap(
        int leftRobot,
        int leftDistance,
        int rightRobot,
        int rightDistance,
        int[] walls
    ) {
        int start = Math.max(
            leftRobot,
            rightRobot - rightDistance
        );

        int end = Math.min(
            leftRobot + leftDistance,
            rightRobot
        );

        // Only walls strictly between the robots
        // belong to the overlap.
        start = Math.max(
            start,
            leftRobot + 1
        );

        end = Math.min(
            end,
            rightRobot - 1
        );

        if (start > end) {
            return 0;
        }

        return upperBound(walls, end)
             - lowerBound(walls, start);
    }

    /*
     * First index where arr[index] >= target.
     */
    private int lowerBound(int[] arr, int target) {
        int lo = 0;
        int hi = arr.length;

        while (lo < hi) {
            int mid = lo + ((hi - lo) >>> 1);

            if (arr[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }

        return lo;
    }

    /*
     * First index where arr[index] > target.
     */
    private int upperBound(int[] arr, int target) {
        int lo = 0;
        int hi = arr.length;

        while (lo < hi) {
            int mid = lo + ((hi - lo) >>> 1);

            if (arr[mid] <= target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }

        return lo;
    }
}
Example Walkthrough

Consider:

robots = [10,2]
distance = [5,1]
walls = [5,2,7]

After sorting:

robot   distance
2       1
10      5

Walls:

[2,5,7]
Robot 2

Robot 2 has distance 1.

Shooting left:

[1,2]

The wall at:

2

is destroyed.

Therefore:

left[0] = 1

Shooting right:

[2,3]

No wall other than 2 is reachable.

Robot 10

Robot 10 has distance 5.

Shooting left:

[5,10]

Walls:

5
7

are destroyed.

Therefore:

left[1] = 2
Choose Both Left

The robots fire away from each other:

2 ←       →? 10

More precisely:

robot 2 ←
robot 10 ←

The ranges are:

robot 2:  [1,2]
robot 10: [5,10]

There is no overlap.

Total:

1 + 2 = 3

Therefore:

Output = 3
Edge Cases
One Robot
robots = [4]
distance = [3]
walls = [1,10]

The robot independently chooses the direction producing the most walls.

Robot Blocks Another Robot
robots = [1,2]
distance = [100,1]
walls = [10]

Although robot 1 has enough distance to reach 10, robot 2 blocks its rightward shot.

Therefore the wall cannot be destroyed.

Wall at Robot Position

A wall can share a position with a robot.

For example:

robot = 5
wall = 5

The wall is included in the robot's firing range.

Multiple Overlapping Ranges

Two neighboring robots can both reach the same walls when:

left robot →→→
          ←←← right robot

The DP explicitly subtracts these duplicated walls.

Why Sorting Is Necessary

The original robot order does not necessarily correspond to their physical positions.

For example:

robots = [10,2,7]

Physically:

2 → 7 → 10

Sorting lets us determine which robots block each other's bullets.

Without sorting, it would be difficult to determine the nearest robot in each direction.

Why Binary Search?

There can be up to:

100,000

walls.

Scanning every wall for every robot would result in:

O(n × m)

which can reach approximately:

10^10

operations.

Instead, sorting the walls once allows each interval to be counted in:

O(log m)

using binary search.

Complexity

Let:

n = number of robots
m = number of walls
Sorting

Robots:

O(n log n)

Walls:

O(m log m)
Interval Counting

Each robot performs a constant number of binary searches:

O(n log m)
Dynamic Programming

Each robot is processed once:

O(n)

Therefore, the total complexity is:

Time:  O(n log n + m log m + n log m)

which can be simplified to:

O((n + m) log(n + m))
Space

The solution stores:

robot pairs
left[]
right[]

Therefore:

Space: O(n)

excluding the input arrays.

Optimization Details
Sorted Robot Pairs

Robot positions and distances are stored together so sorting does not break their relationship.

Binary Search Instead of Wall Scanning

Each interval is counted directly using:

upperBound(walls, right)
- lowerBound(walls, left)

rather than iterating through every wall.

Rolling DP

Only two DP values are required:

dpLeft
dpRight

There is no need for a full dp[n][2] table.

Therefore the DP itself uses:

O(1)

additional memory.

Primitive Arrays

The implementation uses primitive Java arrays:

int[]
int[][]

rather than object-heavy collections, which helps reduce allocation and memory overhead for the maximum constraint of 100,000 elements.


Key Concepts
- Dynamic Programming
- Computational Geometry
- Interval Intersection
- Sorting
- Binary Search
- Greedy Interval Counting
- State Transitions
- Range Queries
- Obstacle Blocking
- Duplicate Counting
- Space Optimization


Language

Java


LeetCode

LeetCode #3661 — Maximum Walls Destroyed by Robots(https://leetcode.com/problems/maximum-walls-destroyed-by-robots/submissions/?envType=daily-question&envId=2026-08-29)