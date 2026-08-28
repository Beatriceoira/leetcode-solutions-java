class Solution {
    public String longestPalindrome(String s) {
        if (s.length() < 2) {
            return s;
        }

        char[] chars = new char[s.length() * 2 + 3];

        chars[0] = '^';
        chars[chars.length - 1] = '$';

        int index = 1;

        for (int i = 0; i < s.length(); i++) {
            chars[index++] = '#';
            chars[index++] = s.charAt(i);
        }

        chars[index] = '#';

        String transformed = new String(chars);

        int[] p = new int[transformed.length()];

        int center = 0;
        int right = 0;

        int maxLength = 0;
        int maxCenter = 0;

        for (int i = 1; i < transformed.length() - 1; i++) {

            int mirror = 2 * center - i;

            if (i < right) {
                p[i] = Math.min(right - i, p[mirror]);
            }

            while (transformed.charAt(i + 1 + p[i])
                    == transformed.charAt(i - 1 - p[i])) {
                p[i]++;
            }

            if (i + p[i] > right) {
                center = i;
                right = i + p[i];
            }

            if (p[i] > maxLength) {
                maxLength = p[i];
                maxCenter = i;
            }
        }

        int start = (maxCenter - maxLength) / 2;

        return s.substring(start, start + maxLength);
    }
}
