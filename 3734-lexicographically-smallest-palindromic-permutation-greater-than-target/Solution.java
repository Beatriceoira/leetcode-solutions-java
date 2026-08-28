class Solution {
    public String lexPalindromicPermutation(String s, String target) {
        int n = s.length();

        int[] count = new int[26];

        for (int i = 0; i < n; i++) {
            count[s.charAt(i) - 'a']++;
        }

        int odd = 0;
        int middle = -1;

        for (int c = 0; c < 26; c++) {
            if ((count[c] & 1) != 0) {
                odd++;
                middle = c;
            }
        }

        if (odd > 1) {
            return "";
        }

        int halfLength = n / 2;

        int[] halfCount = new int[26];

        for (int c = 0; c < 26; c++) {
            halfCount[c] = count[c] / 2;
        }

        char[] half = new char[halfLength];

        int matched = 0;

        for (; matched < halfLength; matched++) {
            int c = target.charAt(matched) - 'a';

            if (halfCount[c] == 0) {
                break;
            }

            half[matched] = target.charAt(matched);
            halfCount[c]--;
        }

        if (matched == halfLength) {

            String candidate = build(half, middle, n);

            if (candidate.compareTo(target) > 0) {
                return candidate;
            }

            for (int i = halfLength - 1; i >= 0; i--) {
                int old = half[i] - 'a';

                halfCount[old]++;

                for (int c = old + 1; c < 26; c++) {
                    if (halfCount[c] > 0) {
                        half[i] = (char) ('a' + c);
                        halfCount[c]--;

                        fill(half, i + 1, halfCount);

                        return build(half, middle, n);
                    }
                }
            }

            return "";
        }

        int targetChar = target.charAt(matched) - 'a';

        for (int c = targetChar + 1; c < 26; c++) {
            if (halfCount[c] > 0) {
                half[matched] = (char) ('a' + c);
                halfCount[c]--;

                fill(half, matched + 1, halfCount);

                return build(half, middle, n);
            }
        }

        for (int i = matched - 1; i >= 0; i--) {
            int old = half[i] - 'a';

            halfCount[old]++;

            for (int c = old + 1; c < 26; c++) {
                if (halfCount[c] > 0) {
                    half[i] = (char) ('a' + c);
                    halfCount[c]--;

                    fill(half, i + 1, halfCount);

                    return build(half, middle, n);
                }
            }
        }

        return "";
    }

    private void fill(char[] half, int start, int[] count) {
        int index = start;

        for (int c = 0; c < 26; c++) {
            while (count[c] > 0) {
                half[index++] = (char) ('a' + c);
                count[c]--;
            }
        }
    }

    private String build(char[] half, int middle, int n) {
        StringBuilder result = new StringBuilder(n);

        for (char c : half) {
            result.append(c);
        }

        if ((n & 1) == 1) {
            result.append((char) ('a' + middle));
        }

        for (int i = half.length - 1; i >= 0; i--) {
            result.append(half[i]);
        }

        return result.toString();
    }
}
