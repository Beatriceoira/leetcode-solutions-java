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
                    dy = 1;
                } else if (dy == 0) {
                    dx = 1;
                } else {
                    int g = gcd(dx, dy);

                    dx /= g;
                    dy /= g;

                    if (dx < 0) {
                        dx = -dx;
                        dy = -dy;
                    }
                }

                long key = ((long) dx << 32) | (dy & 0xffffffffL);

                int count = slopes.increment(key);

                if (count > localBest) {
                    localBest = count;
                }
            }

            int total = localBest + 1;

            if (total > best) {
                best = total;
            }
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
            int t = a % b;
            a = b;
            b = t;
        }

        return a;
    }

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
