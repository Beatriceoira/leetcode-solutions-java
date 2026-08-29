import java.util.Arrays;

class Solution {
    public int maximumAmount(int[][] coins) {
        int m = coins.length;
        int n = coins[0].length;

        final int NEG = Integer.MIN_VALUE / 4;

        int[] dp0 = new int[n];
        int[] dp1 = new int[n];
        int[] dp2 = new int[n];

        Arrays.fill(dp0, NEG);
        Arrays.fill(dp1, NEG);
        Arrays.fill(dp2, NEG);

        dp0[0] = coins[0][0];

        if (coins[0][0] < 0) {
            dp1[0] = 0;
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                if (i == 0 && j == 0) {
                    continue;
                }

                int value = coins[i][j];

                int fromTop0 = dp0[j];
                int fromTop1 = dp1[j];
                int fromTop2 = dp2[j];

                int fromLeft0 = j > 0 ? dp0[j - 1] : NEG;
                int fromLeft1 = j > 0 ? dp1[j - 1] : NEG;
                int fromLeft2 = j > 0 ? dp2[j - 1] : NEG;

                int best0 = Math.max(fromTop0, fromLeft0);
                int best1 = Math.max(fromTop1, fromLeft1);
                int best2 = Math.max(fromTop2, fromLeft2);

                dp0[j] = best0 + value;
                dp1[j] = best1 + value;
                dp2[j] = best2 + value;

                if (value < 0) {
                    dp1[j] = Math.max(
                        dp1[j],
                        Math.max(fromTop0, fromLeft0)
                    );

                    dp2[j] = Math.max(
                        dp2[j],
                        Math.max(fromTop1, fromLeft1)
                    );
                }
            }
        }

        return Math.max(dp0[n - 1],
               Math.max(dp1[n - 1], dp2[n - 1]));
    }
}