# 14. Longest Common Prefix

[**LeetCode Problem**](https://leetcode.com/problems/longest-common-prefix/)

**Difficulty:** Easy
**Language:** Java
**Topic:** String

## Problem

Write a function to find the longest common prefix string amongst an array of strings.

If there is no common prefix, return an empty string `""`.

### Example 1

```text
Input: strs = ["flower","flow","flight"]
Output: "fl"
```

### Example 2

```text
Input: strs = ["dog","racecar","car"]
Output: ""
```

## Approach

Use the first string as the initial prefix.

1. Set `strs[0]` as the current prefix.
2. Compare the prefix with every remaining string using `startsWith()`.
3. If a string does not start with the current prefix, remove the last character from the prefix.
4. Continue shortening the prefix until the current string matches.
5. If the prefix becomes empty, return `""`.
6. After checking all strings, return the remaining prefix.

### Example

For:

```text
["flower", "flow", "flight"]
```

The process is:

```text
flower
  ↓
flow
  ↓
fl
```

`"fl"` is a prefix of every string, so it is the longest common prefix.

## Complexity

Let `S` be the total number of characters examined across the input strings.

* **Time:** `O(S)` approximately
* **Space:** `O(1)` auxiliary space

## Java Solution

```java
class Solution {
    public String longestCommonPrefix(String[] strs) {
        String prefix = strs[0];

        for (int i = 1; i < strs.length; i++) {
            while (!strs[i].startsWith(prefix)) {
                prefix = prefix.substring(0, prefix.length() - 1);

                if (prefix.isEmpty()) {
                    return "";
                }
            }
        }

        return prefix;
    }
}
```

## Key Takeaway

The solution avoids comparing every character across every string from scratch. Instead, it maintains a candidate prefix and progressively reduces it until it is valid for all strings.
