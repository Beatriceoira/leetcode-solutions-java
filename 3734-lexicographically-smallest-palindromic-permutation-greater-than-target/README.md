LeetCode #3734 — Lexicographically Smallest Palindromic Permutation Greater Than Target 

Problem

Given two strings s and target, both of length n, consisting of lowercase English letters, return the lexicographically smallest string that:

1. Is a permutation of s
2. Is a palindrome
3. Is strictly greater than target

If no such string exists, return an empty string.

Example 1
Input:
s = "baba"
target = "abba"

Output:
"baab"

Example 2
Input:
s = "baba"
target = "bbaa"

Output:
""

Example 3
Input:
s = "abc"
target = "abb"

Output:
""

Example 4
Input:
s = "aac"
target = "abb"

Output:
"aca"


Approach

The key observation is that a palindrome is completely determined by its left half and, for odd-length strings, its middle character.

For example:

"baab"

Left half:  "ba"
Middle:     ""
Right half: "ab"

The right half is always the reverse of the left half.

Therefore, instead of generating every permutation of s, we only need to construct the appropriate left half.


1. Count Character Frequencies

First, count how many times each letter appears in s.

int[] count = new int[26];

for (int i = 0; i < n; i++) {
    count[s.charAt(i) - 'a']++;
}

A palindrome can contain:

Zero characters with odd frequency when n is even.
Exactly one character with odd frequency when n is odd.

If more than one character has an odd frequency, a palindromic permutation is impossible.

s = "abc"

a → 1
b → 1
c → 1

3 odd frequencies → impossible

Therefore:

return ""

2. Build the Left-Half Frequency

Each character contributes half of its occurrences to the left side.

For example:

s = "baba"

a → 2
b → 2

The left half needs:

a → 1
b → 1

So:

halfCount[c] = count[c] / 2;

The middle character, if one exists, is the character whose frequency is odd.

3. Match the Target

We construct the left half while trying to match the corresponding characters in target.

For example:

target = "abba"
         ↓↓
left   = "ab"

As long as the required character is available, we use it.

This keeps the resulting palindrome as close as possible to target.

4. Make the Result Strictly Greater

If the constructed palindrome is equal to or smaller than target, we need to increase it.

We work from the rightmost possible position toward the beginning.

For example:

Current:

ab
 ↑

We try to replace the current character with the smallest available character that is strictly larger.

This produces the smallest possible palindrome that is still greater than target.

5. Fill the Remaining Characters Minimally

Once a position has been increased, all positions after it should contain the smallest possible characters.

For example:

Available:

a a b c

Choose a larger character at the current position.

Then fill the remaining positions:

a, a, b, ...

This guarantees the resulting palindrome is lexicographically smallest.

6. Construct the Palindrome

Once the optimal left half has been found:

LEFT + MIDDLE + reverse(LEFT)

For example:

Left:   "ba"
Middle: ""
Right:  "ab"

Result:
"baab"

For an odd-length example:

Left:   "ac"
Middle: "a"
Right:  "ca"

Result:
"aca"


Why This Works

Generating every permutation of s would be extremely inefficient.

For a string of length n, there can be up to:

n!

permutations.

Instead, the solution exploits the structure of palindromes.

Once the left half is chosen:

LEFT
  ↓
PALINDROME = LEFT + MIDDLE + reverse(LEFT)

the entire palindrome is determined.

To obtain the lexicographically smallest palindrome greater than target, we:

1. Match the target for as long as possible.
2. Find the rightmost position that can be increased.
3. Increase it by the smallest possible amount.
4. Fill everything after it with the smallest available characters.
5. Mirror the left half.

This is the same general principle used when finding the next lexicographical permutation, adapted to the palindrome constraint.

Complexity

The alphabet contains only 26 lowercase English letters.

Time Complexity
O(26n)

Since 26 is constant:

O(n)
Space Complexity
O(n)

The algorithm stores the left half and the resulting palindrome.

Edge Cases
No Palindromic Permutation
s = "abc"

Every character has an odd frequency.

Output:
""
Already Greater
s = "aac"
target = "abb"

The only palindrome is:

"aca"

Since:

"aca" > "abb"

the result is:

"aca"
No Greater Palindrome
s = "baba"
target = "bbaa"

The largest palindromic permutation is not greater than target.

Output:
""
Target Is Already a Valid Palindrome

The problem requires the result to be strictly greater, so returning a palindrome equal to target is not valid.

The algorithm therefore continues searching for the next possible palindrome.


Key Concepts
- Frequency Counting
- Greedy Algorithms
- Lexicographical Ordering
- Palindromes
- Backtracking
- String Construction
- Character Frequency Arrays
- Next Lexicographical Permutation


Constraints
1 <= s.length == target.length <= 300

s and target contain only lowercase English letters.


Language

Java

LeetCode

https://leetcode.com/problems/lexicographically-smallest-palindromic-permutation-greater-than-target/description/
