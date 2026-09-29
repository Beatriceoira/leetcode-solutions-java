class Solution {
    public int romanToInt(String s) {
        int result = 0;

        for (int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case 'I':
                    result += (i + 1 < s.length() &&
                               (s.charAt(i + 1) == 'V' || s.charAt(i + 1) == 'X')) ? -1 : 1;
                    break;
                case 'V':
                    result += 5;
                    break;
                case 'X':
                    result += (i + 1 < s.length() &&
                               (s.charAt(i + 1) == 'L' || s.charAt(i + 1) == 'C')) ? -10 : 10;
                    break;
                case 'L':
                    result += 50;
                    break;
                case 'C':
                    result += (i + 1 < s.length() &&
                               (s.charAt(i + 1) == 'D' || s.charAt(i + 1) == 'M')) ? -100 : 100;
                    break;
                case 'D':
                    result += 500;
                    break;
                default:
                    result += 1000;
            }
        }

        return result;
    }
}