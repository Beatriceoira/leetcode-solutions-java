class Solution {
    public int countCommas(int n) {
        if (n < 1_000) return 0;
        if (n < 1_000_000) return n - 999;
        if (n < 1_000_000_000) return 2 * n - 1_000_998;
        return 3L * n - 1_000_000_997 > Integer.MAX_VALUE 
            ? (int)(3L * n - 1_000_000_997) 
            : (int)(3L * n - 1_000_000_997);
    }
}