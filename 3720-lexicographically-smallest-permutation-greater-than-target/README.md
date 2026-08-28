LeetCode #3720 — Lexicographically Smallest Permutation Greater Than Target

A Java solution to LeetCode Problem #3720: Lexicographically Smallest Permutation Greater Than Target, using character frequency counting, greedy construction, and backtracking.

Problem

Given two strings s and target of equal length, return the lexicographically smallest permutation of s that is strictly greater than target.

If no permutation of s is lexicographically strictly greater than target, return an empty string.

A string a is lexicographically greater than a string b when, at the first position where they differ, a contains a character that appears later in the alphabet.

Examples
Example 1

Input:

s = "abc"
target = "bba"

Output:

"bca"

Explanation:

The permutations of "abc" in lexicographical order include:

abc
acb
bac
bca
cab
cba

"bca" is the smallest permutation that is strictly greater than "bba".

Example 2

Input:

s = "leet"
target = "code"

Output:

"eelt"

"eelt" is the smallest permutation of "leet" that is lexicographically greater than "code".

Example 3

Input:

s = "baba"
target = "bbaa"

Output:

""

No permutation of "baba" is strictly greater than "bbaa".

Approach

The solution uses a frequency array to keep track of the characters available in s.

Because s contains only lowercase English letters, an array of 26 elements is sufficient.

count[0]  → 'a'
count[1]  → 'b'
count[2]  → 'c'
...
count[25] → 'z'

The algorithm tries to construct the answer so that it remains equal to target for as long as possible.

1. Match the Target

Starting from the left, use the same character as target[i] whenever that character is available.

For example:

s      = "abc"
target = "bba"

The first character can be matched:

b

The remaining characters are:

a, c

At the next position, another b is unavailable.

2. Try a Greater Character

When the target character cannot be used, try to find the smallest available character greater than it.

For example:

target character = 'b'

available = a, c

The smallest character greater than b is c.

The result becomes:

bc

Since the result is now already greater than the target, the remaining characters should be placed in ascending order:

bca

3. Backtracking

There are cases where the current position cannot be made greater.

When this happens, the algorithm backtracks to a previous position that was successfully matched with the target.

It then tries to replace that character with the smallest available character that is greater.

This is important because we want the smallest possible permutation greater than the target.

For example, if we have matched:

target = "bba"
result = "bba"

we cannot return it because the result must be strictly greater.

We therefore start from the rightmost position and try to increase it.

If that is impossible, we move further left.

4. Fill Remaining Characters

Once a position has been increased, the result is guaranteed to be greater than the target.

The remaining characters are then placed in ascending order.

This produces the smallest possible result.

Solution
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
Complexity

Let n be the length of s and target.

There are only 26 possible lowercase English letters, so searching through the alphabet takes at most 26 operations per position.

Metric	Complexity
Time	O(26n) → O(n)
Space	O(n)

The frequency array uses constant space:

O(26) = O(1)

The result array requires O(n) space.

Key Concepts
- Lexicographical Ordering
- Permutations
- Frequency Counting
- Greedy Algorithms
- Backtracking
- Character Arrays
- String Manipulation
- LeetCode

LeetCode #3720 — Lexicographically Smallest Permutation Greater Than Target(https://leetcode.com/problems/lexicographically-smallest-permutation-greater-than-target/?envType=daily-question&envId=2026-08-27)

Language
Java
Character Frequency Array