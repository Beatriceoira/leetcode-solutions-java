LeetCode #3 — Longest Substring Without Repeating Characters

A Java solution to LeetCode Problem #3: Longest Substring Without Repeating Characters, using the Sliding Window technique and a HashSet to efficiently find the longest substring without duplicate characters.

Problem

Given a string s, find the length of the longest substring without repeating characters.

A substring consists of characters that are contiguous within the original string.

Example 1

Input:

s = "abcabcbb"


Output:

3


Explanation:

The longest substring without repeating characters is "abc", which has a length of 3.

Other valid answers include "bca" and "cab".


Example 2

Input:

s = "bbbbb"


Output:

1


Explanation:

The longest substring without repeating characters is "b".


Example 3

Input:

s = "pwwkew"


Output:

3


Explanation:

The longest substring is "wke", which has a length of 3.

"pwke" is not a valid answer because it is a subsequence, not a substring.


Approach

This solution uses the Sliding Window technique with a HashSet.

Two pointers, left and right, are used to define the current substring window.

The HashSet stores the characters currently inside the window.

For each character:

1. Move the right pointer through the string.
2. Check if the current character already exists in the HashSet.
3. If it does, move the left pointer forward and remove characters until the duplicate is removed.
4. Add the current character to the HashSet.
5. Calculate the current window length.
6. Update the maximum length if the current window is larger.


Example Walkthrough

For:

s = "abcabcbb"

The window initially grows:

[a]       → length 1
[ab]      → length 2
[abc]     → length 3

The next character is a, which already exists in the window.

The left pointer moves forward until the duplicate is removed:

[bc]
  ↑

Then the new a is added:

[bca]     → length 3

The process continues until the end of the string.

The longest substring has a length of:

3

Solution
import java.util.HashSet;

class Solution {
    public int lengthOfLongestSubstring(String s) {
        HashSet<Character> set = new HashSet<>();

        int left = 0;
        int maxLength = 0;

        for (int right = 0; right < s.length(); right++) {
            while (set.contains(s.charAt(right))) {
                set.remove(s.charAt(left));
                left++;
            }

            set.add(s.charAt(right));

            maxLength = Math.max(maxLength, right - left + 1);
        }

        return maxLength;
    }
}
Complexity

Let n be the length of the string.

Metric	Complexity
Time	    O(n)
Space	    O(n)

Each character is added to and removed from the HashSet at most once, resulting in O(n) time complexity.

The HashSet may contain up to n characters, resulting in O(n) space complexity.

Key Concepts
- Sliding Window
- Two Pointers
- HashSet
- String Traversal
- Duplicate Detection
- Substrings
- LeetCode

LeetCode #3 — Longest Substring Without Repeating Characters

Language
Java
Java Collections Framework (HashSet)
