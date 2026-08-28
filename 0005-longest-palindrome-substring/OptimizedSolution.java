class Solution {
    public String longestPalindrome(String s) {
        int n = s.length();

        if (n < 2) {
            return s;
        }

        // ^ # a # b # a # d # $
        int length = 2 * n + 3;
        char[] t = new char[length];

        t[0] = '^';
        t[length - 1] = '$';

        int index = 1;

        for (int i = 0; i < n; i++) {
            t[index++] = '#';
            t[index++] = s.charAt(i);
        }

        t[index] = '#';

        int[] p = new int[length];

        int center = 0;
        int right = 0;

        int bestCenter = 0;
        int bestRadius = 0;

        for (int i = 1; i < length - 1; i++) {
            int mirror = 2 * center - i;

            if (i < right) {
                p[i] = Math.min(right - i, p[mirror]);
            }

            while (t[i + p[i] + 1] == t[i - p[i] - 1]) {
                p[i]++;
            }

            if (i + p[i] > right) {
                center = i;
                right = i + p[i];
            }

            if (p[i] > bestRadius) {
                bestRadius = p[i];
                bestCenter = i;
            }
        }

        int start = (bestCenter - bestRadius) / 2;

        return s.substring(start, start + bestRadius);
    }
}
