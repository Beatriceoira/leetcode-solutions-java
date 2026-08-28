LeetCode #5 — Longest Palindromic Substring

This repository contains multiple Java solutions for LeetCode Problem #5: Longest Palindromic Substring, ranging from the interview-friendly approach to the optimal linear-time solution.

Problem Statement

Given a string `s`, return the **longest palindromic substring** in `s`.

A palindrome is a string that reads the same forward and backward.

Example 1

Input

```text
s = "babad"
```

Output

```text
"bab"
```

Explanation

`"aba"` is also a valid answer.

Example 2

Input

```text
s = "cbbd"
```

Output

```text
"bb"
```

Constraints

```text
1 <= s.length <= 1000
s consists only of digits and English letters
```

---

Solutions Included

1. Expand Around Center(solution.java)

Idea

Every palindrome has a center.

For each character in the string:

* Treat it as the center of an odd-length palindrome.
* Treat the gap after it as the center of an even-length palindrome.
* Expand outward while both sides match.

Example

```text
b a b
  ↑
center
```

Expand outward:

```text
b a b
↑   ↑
```

Palindrome found:

```text
bab
```

Complexity

| Metric | Complexity |
| ------ | ---------- |
| Time   | O(n²)      |
| Space  | O(1)       |

Advantages

* Very easy to understand
* Common interview solution
* Constant extra space

Disadvantages

* Not optimal for large inputs
* May repeatedly check the same characters

---

2. Manacher's Algorithm (String-Based)

Idea

Manacher's Algorithm transforms the string so that odd-length and even-length palindromes can be handled uniformly.

Example:

```text
"abba"
```

becomes:

```text
^#a#b#b#a#$
```

The inserted separators (`#`) allow every palindrome to be treated as an odd-length palindrome.

Key Concepts

Radius Array

```text
p[i]
```

stores the radius of the palindrome centered at position `i`.

Mirror Position

If a palindrome centered at `center` extends to `right`:

```text
mirror = 2 * center - i
```

Previously computed information can be reused.

This avoids repeating expansions and makes the algorithm linear.

Complexity

| Metric | Complexity |
| ------ | ---------- |
| Time   | O(n)       |
| Space  | O(n)       |

Advantages

* Optimal asymptotic time complexity
* Reuses previously computed palindrome information

Disadvantages

* More difficult to understand
* More difficult to implement correctly

---

3. Optimized Manacher's Algorithm (Char Array)

Improvements

Compared to the String-based version:

* Uses `char[]` instead of repeatedly calling `String.charAt()`
* Avoids unnecessary String lookups during expansion
* Faster in practice on LeetCode
* Maintains the same O(n) complexity

Transformation

```text
Original:
babad

Transformed:
^#b#a#b#a#d#$
```

Expansion

Instead of checking:

```java
transformed.charAt(...)
```

the algorithm accesses:

```java
t[index]
```

directly from a character array.

This reduces overhead inside the hottest loop of the algorithm.

Complexity

| Metric | Complexity |
| ------ | ---------- |
| Time   | O(n)       |
| Space  | O(n)       |

Advantages

* Optimal complexity
* Faster practical performance
* Cleaner memory access pattern

---

Why Manacher's Algorithm Works

A naive approach repeatedly expands around every center:

```text
Center 1 → Expand
Center 2 → Expand
Center 3 → Expand
Center 4 → Expand
...
```

This causes repeated work.

Manacher's Algorithm stores information about the current longest palindrome:

```text
center
   ↓
# # a # b # a # # #
      <----->
         right
```

When another center falls inside this range, information from its mirrored position can be reused.

```text
mirror = 2 * center - i
```

Instead of expanding from scratch, the algorithm starts with already-known palindrome information.

This is the reason the algorithm achieves:

```text
O(n)
```

instead of:

```text
O(n²)
```

---

Complexity Comparison

| Approach                       | Time  | Space |
| ------------------------------ | ----- | ----- |
| Brute Force                    | O(n³) | O(1)  |
| Dynamic Programming            | O(n²) | O(n²) |
| Expand Around Center           | O(n²) | O(1)  |
| Manacher's Algorithm           | O(n)  | O(n)  |
| Optimized Manacher's Algorithm | O(n)  | O(n)  |

---
In my view:

Which Solution Should You Use?

Interviews

Use:

```text
Expand Around Center
```

Reason:

* Easier to explain
* Easier to implement correctly
* Commonly accepted by interviewers

Competitive Programming / LeetCode Optimization

Use:

```text
Manacher's Algorithm
```

Reason:

* Optimal O(n) runtime
* Demonstrates advanced algorithm knowledge
* Excellent performance on large inputs

Portfolio / GitHub Projects

Include both:

```text
Solution.java
└── Expand Around Center (O(n²))

ManachersSolution.java
└── Manacher's Algorithm (O(n))

OptimizedManachersSolution.java
└── Manacher's Algorithm using char[]
```

This demonstrates understanding of both practical and optimal approaches.

---

Key Concepts

* Palindromes
* String Manipulation
* Two Pointers
* Expand Around Center
* Dynamic Programming
* Manacher's Algorithm
* Mirror Positions
* Radius Arrays
* Linear-Time String Algorithms

---

LeetCode

Problem Link:

https://leetcode.com/problems/longest-palindromic-substring/

---

Language

* Java
