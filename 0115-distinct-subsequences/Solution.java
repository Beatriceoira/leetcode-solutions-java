class Solution {
    public int numDistinct(String s, String t) {
        final int m = s.length();
        final int n = t.length();
        if (n > m) return 0;
        final char[] target = t.toCharArray();
        final long[] dp = new long[n + 1];
        dp[0] = 1;
        for (int i = 0; i < m; i++) {
            final char c = s.charAt(i);
            final int limit = Math.min(i + 1, n);
            for (int j = limit; j > 0; j--) {
                if (c == target[j - 1]) {
                    dp[j] += dp[j - 1];
                }
            }
        }
        return (int) dp[n];
    }
}