class Solution {
    public boolean isMatch(String s, String p) {
        int n = s.length();
        int m = p.length();
        boolean[] dp = new boolean[m + 1];

        dp[0] = true;
        
        for (int j = 2; j <= m; j++) {
            if (p.charAt(j - 1) == '*') {
                dp[j] = dp[j - 2];
            }
        }

        for (int i = 1; i <= n; i++) {
            boolean diagonal = dp[0];
             
              dp[0] = false;

            char current = s.charAt(i - 1);

            for (int j = 1; j <= m; j++) {
                boolean above = dp[j];

                char pattern = p.charAt(j - 1);

                if (pattern == '*') {
                    char previous = p.charAt(j - 2);

                    boolean matches =
                            previous == '.' ||
                            previous == current;

                    dp[j] = dp[j - 2];
                  
                    if (matches) {
                        dp[j] |= above;
                    }

                } else {
                    dp[j] = diagonal &&
                            (pattern == '.' || pattern == current);
                }

                diagonal = above;
            }
        }

        return dp[m];
    }
}
