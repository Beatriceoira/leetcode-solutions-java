class Solution {
    private final List<String> result = new ArrayList<>();

    public List<String> removeInvalidParentheses(String s) {
        remove(s, 0, 0, '(', ')');
        return result;
    }
    private void remove(
        String s,
        int scanStart,
        int removeStart,
        char open,
        char close
    ) {
        int balance = 0;

        for (int i = scanStart; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == open) {
                balance++;
            } else if (c == close) {
                balance--;
            }

            if (balance >= 0) {
                continue;
            }
            for (int j = removeStart; j <= i; j++) {
                if (s.charAt(j) == close &&
                    (j == removeStart || s.charAt(j - 1) != close)) {

                    String next =
                        s.substring(0, j) + s.substring(j + 1);

                    remove(
                        next,
                        i,
                        j,
                        open,
                        close
                    );
                }
            }

            return;
        }
        String reversed = new StringBuilder(s).reverse().toString();
        if (open == '(') {
            remove(reversed, 0, 0, ')', '(');
        } else {
            result.add(reversed);
        }
    }
}
