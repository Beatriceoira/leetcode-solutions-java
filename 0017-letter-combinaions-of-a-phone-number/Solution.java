import java.util.ArrayList;
import java.util.List;

class Solution {
    private static final String[] MAP = {
        "", "", "abc", "def", "ghi",
        "jkl", "mno", "pqrs", "tuv", "wxyz"
    };

    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();

        if (digits.isEmpty()) {
            return result;
        }

        char[] current = new char[digits.length()];
        backtrack(digits, 0, current, result);

        return result;
    }

    private void backtrack(
        String digits,
        int index,
        char[] current,
        List<String> result
    ) {
        if (index == digits.length()) {
            result.add(new String(current));
            return;
        }

        String letters = MAP[digits.charAt(index) - '0'];

        for (int i = 0; i < letters.length(); i++) {
            current[index] = letters.charAt(i);
            backtrack(digits, index + 1, current, result);
        }
    }
}