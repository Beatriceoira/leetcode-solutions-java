class Solution {
    public boolean isValid(String s) {
        int n = s.length();

        // Odd-length strings can never be valid.
        if ((n & 1) != 0) {
            return false;
        }

        char[] stack = new char[n];
        int top = 0;

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);

            if (c == '(' || c == '[' || c == '{') {
                stack[top++] = c;
            } else {
                if (top == 0) {
                    return false;
                }

                char open = stack[--top];

                if ((c == ')' && open != '(') ||
                    (c == ']' && open != '[') ||
                    (c == '}' && open != '{')) {
                    return false;
                }
            }
        }

        return top == 0;
    }
}