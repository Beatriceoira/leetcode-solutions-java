class Solution {
    public String reverseParentheses(String s) {
        int n = s.length();
        char[] chars = s.toCharArray();
        int[] pair = new int[n];

        int[] stack = new int[n];
        int top = -1;

        // Find matching parentheses.
        for (int i = 0; i < n; i++) {
            if (chars[i] == '(') {
                stack[++top] = i;
            } else if (chars[i] == ')') {
                int open = stack[top--];
                pair[open] = i;
                pair[i] = open;
            }
        }

        StringBuilder result = new StringBuilder(n);
        int i = 0;
        int direction = 1;

        while (i >= 0 && i < n) {
            if (chars[i] == '(' || chars[i] == ')') {
                i = pair[i];
                direction = -direction;
            } else {
                result.append(chars[i]);
            }

            i += direction;
        }

        return result.toString();
    }
}