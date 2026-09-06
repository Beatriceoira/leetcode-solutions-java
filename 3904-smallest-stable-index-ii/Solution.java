class Solution {
    public int firstStableIndex(int[] nums, int k) {
        final int n = nums.length;
        final int[] min = new int[n];
        int m = nums[n - 1];
        min[n - 1] = m;
        for (int i = n - 2; i >= 0; i--) {
            final int v = nums[i];
            if (v < m) m = v;
            min[i] = m;
        }
        int mx = 0;
        for (int i = 0; i < n; i++) {
            final int v = nums[i];
            if (v > mx) mx = v;

            if (mx <= min[i] + k) {
                return i;
            }
        }
        return -1;
    }
}