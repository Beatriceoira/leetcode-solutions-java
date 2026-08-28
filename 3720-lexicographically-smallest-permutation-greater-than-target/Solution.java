class Solution {
    public String lexGreaterPermutation(String s, String target) {
        int n = s.length();

        int[] count = new int[26];

        for (char c : s.toCharArray()) {
            count[c - 'a']++;
        }

        char[] result = new char[n];

        for (int i = 0; i < n; i++) {
            int current = target.charAt(i) - 'a';

            if (count[current] > 0) {
                result[i] = target.charAt(i);
                count[current]--;
                continue;
            }

            for (int c = current + 1; c < 26; c++) {
                if (count[c] > 0) {
                    result[i] = (char) ('a' + c);
                    count[c]--;

                    fillRemaining(result, i + 1, count);

                    return new String(result);
                }
            }

            for (int j = i - 1; j >= 0; j--) {
                int previous = result[j] - 'a';
                count[previous]++;
                for (int c = previous + 1; c < 26; c++) {
                    if (count[c] > 0) {
                        result[j] = (char) ('a' + c);
                        count[c]--;

                        fillRemaining(result, j + 1, count);

                        return new String(result);
                    }
                }
            }

            return "";
        }

        for (int i = n - 1; i >= 0; i--) {
            int current = result[i] - 'a';

            count[current]++;

            for (int c = current + 1; c < 26; c++) {
                if (count[c] > 0) {
                    result[i] = (char) ('a' + c);
                    count[c]--;

                    fillRemaining(result, i + 1, count);

                    return new String(result);
                }
            }
        }

        return "";
    }

    private void fillRemaining(char[] result, int start, int[] count) {
        int index = start;

        for (int c = 0; c < 26; c++) {
            while (count[c] > 0) {
                result[index++] = (char) ('a' + c);
                count[c]--;
            }
        }
    }
}