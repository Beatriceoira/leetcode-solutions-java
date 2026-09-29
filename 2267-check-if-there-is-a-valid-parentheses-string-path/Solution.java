class Solution {

    public boolean hasValidPath(char[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int len = m + n - 1;

        if ((len & 1) != 0) {
            return false;
        }

        if (grid[0][0] == ')' || grid[m - 1][n - 1] == '(') {
            return false;
        }

        int words = (len >>> 6) + 1;
        long[] dp = new long[n * words];

        // '(' -> balance 1
        dp[0] = 2L;

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {

                if (r == 0 && c == 0) {
                    continue;
                }

                int base = c * words;

                // Merge from left.
                if (c > 0) {
                    int left = base - words;

                    for (int w = 0; w < words; w++) {
                        dp[base + w] |= dp[left + w];
                    }
                }

                if (grid[r][c] == '(') {

                    long carry = 0;

                    for (int w = 0; w < words; w++) {
                        int i = base + w;
                        long x = dp[i];

                        dp[i] = (x << 1) | carry;
                        carry = x >>> 63;
                    }

                } else {

                    long carry = 0;

                    for (int w = words - 1; w >= 0; w--) {
                        int i = base + w;
                        long x = dp[i];

                        dp[i] = (x >>> 1) | carry;
                        carry = x << 63;
                    }
                }
            }
        }

        return (dp[(n - 1) * words] & 1L) != 0;
    }
}