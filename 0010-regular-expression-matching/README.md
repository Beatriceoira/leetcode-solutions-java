LeetCode #10 — Regular Expression Matching


Problem

Given an input string s and a pattern p, implement regular expression matching with support for:

. — Matches any single character.
* — Matches zero or more occurrences of the preceding element.

The match must cover the entire input string, not just a portion of it.


Examples
Example 1
Input:
s = "aa"
p = "a"

Output:
false

"a" only matches one a, while the input contains two characters.

Example 2
Input:
s = "aa"
p = "a*"

Output:
true

a* can match zero or more a characters:

a* → ""
a* → "a"
a* → "aa"
a* → "aaa"
...

Therefore, "aa" matches "a*".

Example 3
Input:
s = "ab"
p = ".*"

Output:
true

. matches any character, while * allows it to repeat:

.* → "ab"


Approach

This solution uses Dynamic Programming (DP) with a one-dimensional array.

Instead of storing every state in a 2D table, we reuse the previous row and store only the information necessary for the current iteration.


DP Definition
dp[j]

represents whether the current prefix of s matches the first j characters of p.

For example:

s = "aa"
p = "a*"

The algorithm evaluates progressively larger prefixes.


Character Matching

For a normal character, there are two possibilities.

Exact Match
s[i] == p[j]
Wildcard Match
p[j] == '.'

Therefore:

dp[j] = diagonal &&
        (pattern == '.' || pattern == current);

diagonal represents the previous row's dp[j - 1].


Handling *

The most important part of the algorithm is handling *.

Suppose:

p = "a*"

The * can represent:

Zero occurrences
a* → ""

We effectively remove both a and * from consideration:

dp[j] = dp[j - 2];
One or more occurrences

If the preceding character matches the current input character, * can consume another character.

The previous row's value is preserved in:

boolean above = dp[j];

Then:

dp[j] |= above;

This allows the * to continue matching characters.


Why above Is Necessary

Because this is a 1D DP array, the values are updated in place.

Before modifying:

dp[j]

we save its previous-row value:

boolean above = dp[j];

This is important because:

previous dp[j]

and:

current dp[j]

represent different states.

Without saving the previous value, the algorithm could accidentally use an already-updated state and produce incorrect results.


Empty String Handling

An empty string can match patterns such as:

a*
a*b*
a*b*c*

because every * can represent zero occurrences.

The initialization handles this:

dp[0] = true;

for (int j = 2; j <= m; j++) {
    if (p.charAt(j - 1) == '*') {
        dp[j] = dp[j - 2];
    }
}

For example:

s = ""
p = "a*b*"

can match because:

a* → ""
b* → ""


Space Optimization

A traditional solution uses a 2D DP table:

dp[n + 1][m + 1]

which requires:

O(n × m)

space.

This implementation compresses the table into:

dp[m + 1]

and keeps track of the previous diagonal value using:

boolean diagonal

Therefore, the space complexity becomes:

O(m)

instead of:

O(n × m)


Algorithm
1. Create a DP array of size p.length() + 1.
2. Set dp[0] = true because an empty string matches an empty pattern.
3. Initialize patterns containing * that can match an empty string.
4. Process each character of s.
5. For normal characters:
- Match the exact character, or
- Match using ..
6. For *:
- Consider zero occurrences using dp[j - 2].
- Consider one or more occurrences using the previous-row state.
7. Return dp[p.length()].


Complexity

Let:

n = s.length()
m = p.length()
Time Complexity
O(n × m)

Every character in s is compared against every relevant position in p.

Space Complexity
O(m)

Only one DP row is stored.

Complexity Comparison
Approach	                              Time	       Space
Brute Force / Backtracking	            Exponential	 O(n + m)
2D Dynamic Programming	                O(n × m)   	 O(n × m)
Optimized 1D Dynamic Programming	      O(n × m)	   O(m)

Edge Cases
Exact Match
s = "abc"
p = "abc"

→ true

Different Lengths
s = "aa"
p = "a"

→ false

Zero Occurrences
s = "b"
p = "a*b"

→ true

a* matches zero characters.

Multiple Occurrences
s = "aaa"
p = "a*"

→ true

Wildcard
s = "ab"
p = ".*"

→ true

Complex Pattern
s = "aab"
p = "c*a*b"

→ true

Because:

c* → ""
a* → "aa"
b  → "b"

giving:

"aab"


Key Concepts
- Dynamic Programming
- String Matching
- Regular Expressions
- Wildcard Matching
- State Compression
- 1D DP
- Space Optimization
- Greedy vs. DP
- Prefix Matching


Language

Java


LeetCode

LeetCode #10 — Regular Expression Matching(https://leetcode.com/problems/regular-expression-matching/description/)
