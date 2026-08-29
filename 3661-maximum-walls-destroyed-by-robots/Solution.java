import java.util.Arrays;

class Solution {
    public int maxWalls(int[] robots, int[] distance, int[] walls) {
        int n = robots.length;

        int[][] r = new int[n][2];

        for (int i = 0; i < n; i++) {
            r[i][0] = robots[i];
            r[i][1] = distance[i];
        }

        Arrays.sort(r, (a, b) -> Integer.compare(a[0], b[0]));
        Arrays.sort(walls);

        int[] left = new int[n];
        int[] right = new int[n];

        for (int i = 0; i < n; i++) {
            int pos = r[i][0];
            int d = r[i][1];

            int leftStart = pos - d;

            if (i > 0) {
                leftStart = Math.max(leftStart, r[i - 1][0] + 1);
            }

            int leftEnd = pos;

            left[i] =
                    upperBound(walls, leftEnd)
                    - lowerBound(walls, leftStart);

            int rightStart = pos;
            int rightEnd = pos + d;

            if (i < n - 1) {
                rightEnd = Math.min(rightEnd, r[i + 1][0] - 1);
            }

            right[i] =
                    upperBound(walls, rightEnd)
                    - lowerBound(walls, rightStart);
        }

        int dpLeft = left[0];
        int dpRight = right[0];

        for (int i = 1; i < n; i++) {

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

        start = Math.max(start, leftRobot + 1);
        end = Math.min(end, rightRobot - 1);

        if (start > end) {
            return 0;
        }

        return upperBound(walls, end)
                - lowerBound(walls, start);
    }

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