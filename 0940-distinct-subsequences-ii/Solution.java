class Solution {
    private static final int MOD = 1_000_000_007;

    public int distinctSubseqII(String s) {
        final int[] end = new int[26];
        int total = 0;

        for (int i = 0, n = s.length(); i < n; i++) {
            final int c = s.charAt(i) - 'a';

            final int add = total + 1 >= MOD
                    ? total + 1 - MOD
                    : total + 1;

            total -= end[c];

            if (total < 0) {
                total += MOD;
            }
            total += add;

            if (total >= MOD) {
                total -= MOD;
            }

            end[c] = add;
        }

        return total;
    }
}