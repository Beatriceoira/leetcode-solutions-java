import java.util.Arrays;

class Solution {

    private static final int MOD = 1_000_000_007;

    public int xorAfterQueries(int[] nums, int[][] queries) {
        final int n = nums.length;
        final int T = (int) Math.sqrt(n) + 1;             

        int[] cnt = new int[T + 1];
        int light = 0;
        for (int[] q : queries) {
            int k = q[2];
            if (k >= T) {
                int r = q[1];
                long v = q[3] % MOD;
                for (int i = q[0]; i <= r; i += k) {
                    nums[i] = (int) (nums[i] * v % MOD);
                }
            } else {
                cnt[k]++;
                light++;
            }
        }

        if (light > 0) {
            int[] off = new int[T + 1];
            for (int k = 1; k < T; k++) off[k + 1] = off[k] + cnt[k];
            int[] cur = off.clone();
            int[] ql = new int[light], qr = new int[light], qv = new int[light], nxt = new int[light];
            for (int[] q : queries) {
                int k = q[2];
                if (k < T) {
                    int p = cur[k]++;
                    ql[p] = q[0];
                    qr[p] = q[1];
                    qv[p] = (int) (q[3] % MOD);
                }
            }
            long[] inv = batchInverse(qv);                 

            long[] dif = new long[n];
            Arrays.fill(dif, 1L);
            int[] bucket = new int[T];                  
            Arrays.fill(bucket, -1);

            for (int k = 1; k < T; k++) {
                int s = off[k], e = off[k + 1];
                if (s == e) continue;
                for (int i = s; i < e; i++) {            
                    int res = ql[i] % k;
                    nxt[i] = bucket[res];
                    bucket[res] = i;
                }

                for (int i = s; i < e; i++) {
                    int res = ql[i] % k;
                    int head = bucket[res];
                    if (head < 0) continue;              
                    bucket[res] = -1;

                    if (nxt[head] < 0) {                 
                        int l = ql[head], r = qr[head];
                        long v = qv[head];
                        for (int p = l; p <= r; p += k) {
                            nums[p] = (int) (nums[p] * v % MOD);
                        }
                        continue;
                    }

                    int minL = Integer.MAX_VALUE, end = 0;
                    for (int j = head; j >= 0; j = nxt[j]) {
                        int l = ql[j];
                        dif[l] = dif[l] * qv[j] % MOD;
                        int R = l + ((qr[j] - l) / k + 1) * k;
                        if (R < n) dif[R] = dif[R] * inv[j] % MOD;
                        if (l < minL) minL = l;
                        if (R > end) end = R;
                    }

                    long run = 1;
                    int limit = Math.min(end, n);
                    for (int p = minL; p < limit; p += k) {
                        run = run * dif[p] % MOD;
                        dif[p] = 1;                     
                        nums[p] = (int) (nums[p] * run % MOD);
                    }
                    if (end < n) dif[end] = 1;
                }
            }
        }

        int res = 0;
        for (int x : nums) res ^= x;
        return res;
    }

    private long[] batchInverse(int[] a) {
        int m = a.length;
        long[] pref = new long[m + 1];
        pref[0] = 1;
        for (int i = 0; i < m; i++) pref[i + 1] = pref[i] * a[i] % MOD;
        long acc = power(pref[m], MOD - 2);
        long[] res = new long[m];
        for (int i = m - 1; i >= 0; i--) {
            res[i] = acc * pref[i] % MOD;
            acc = acc * a[i] % MOD;
        }
        return res;
    }

    private long power(long x, long y) {
        long r = 1;
        x %= MOD;
        while (y > 0) {
            if ((y & 1) == 1) r = r * x % MOD;
            x = x * x % MOD;
            y >>= 1;
        }
        return r;
    }
}