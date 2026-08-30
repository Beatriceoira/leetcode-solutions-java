LeetCode #7 — Reverse Integer


Problem

Given a signed 32-bit integer `x`, return `x` with its digits reversed.

If reversing the digits causes the resulting value to exceed the signed 32-bit integer range:


[-2³¹, 2³¹ - 1]


return:


0


The solution must work without storing the value in a 64-bit integer.




Examples

Example 1


Input:
x = 123

Output:
321

The digits are reversed:

123 → 321


Example 2

Input:
x = -123

Output:
-321

The sign is preserved while the digits are reversed:

-123 → -321



Example 3

Input:
x = 120

Output:
21

The trailing zero becomes a leading zero after reversal and is therefore discarded:

120 → 021 → 21



Approach

The integer can be reversed one digit at a time.

For example:

123

First extract:

3

Then:

2

Then:

1

The reversed number is constructed using:

reversed = reversed * 10 + digit

The important part is checking for 32-bit integer overflow before performing the multiplication and addition.



Extracting Digits

The last digit of an integer can be obtained using:


digit = x % 10;

Then remove that digit using:

x /= 10;


For:

123

the process is:

x = 123
digit = 3

x = 12
digit = 2

x = 1
digit = 1

x = 0


Building the Reversed Number

Each extracted digit is appended to the result:

reversed = reversed * 10 + digit


For `123`:

0
↓
3
↓
32
↓
321




The Overflow Problem

Java's `int` is a signed 32-bit integer:

Integer.MIN_VALUE = -2,147,483,648
Integer.MAX_VALUE =  2,147,483,647


We cannot simply do:

reversed = reversed * 10 + digit;

because the multiplication could overflow before Java gives us a chance to check it.

For example:

reversed = 2,147,483,647


Multiplying by `10` would exceed the `int` range.



Overflow Detection

Before adding another digit, check whether:

reversed * 10 + digit

would exceed the valid range.

For positive overflow:

if (reversed > Integer.MAX_VALUE / 10 ||
    (reversed == Integer.MAX_VALUE / 10 && digit > 7)) {
    return 0;
}

Why `7`?

Because:

Integer.MAX_VALUE = 2,147,483,647


The last valid digit is `7`.

For negative overflow:

if (reversed < Integer.MIN_VALUE / 10 ||
    (reversed == Integer.MIN_VALUE / 10 && digit < -8)) {
    return 0;
}

Why `-8`?

Because:

Integer.MIN_VALUE = -2,147,483,648

The last valid digit is `-8`.



Important Detail About Negative Numbers

Java's remainder operator preserves the sign.

For example:

-123 % 10 = -3

Therefore the same algorithm works for positive and negative integers without separately reversing the sign.

For:

x = -123

the extracted digits are:

-3
-2
-1

and the result becomes:

-321


Algorithm

1. Initialize `reversed = 0`.
2. While `x != 0`:

   * Extract the last digit using `% 10`.
   * Check whether adding this digit would cause 32-bit overflow.
   * Append the digit to `reversed`.
   * Remove the last digit from `x` using `/= 10`.
3. Return `reversed`.
4. If an overflow would occur at any point, return `0`.


Implementation


class Solution {
    public int reverse(int x) {
        int reversed = 0;

        while (x != 0) {
            int digit = x % 10;

            // Check for positive overflow.
            if (reversed > Integer.MAX_VALUE / 10 ||
                (reversed == Integer.MAX_VALUE / 10 && digit > 7)) {
                return 0;
            }

            // Check for negative overflow.
            if (reversed < Integer.MIN_VALUE / 10 ||
                (reversed == Integer.MIN_VALUE / 10 && digit < -8)) {
                return 0;
            }

            reversed = reversed * 10 + digit;

            x /= 10;
        }

        return reversed;
    }
}


Example Walkthrough

Consider:

x = 123


Initially:

reversed = 0


First Iteration

digit = 123 % 10
      = 3


Build:

reversed = 0 * 10 + 3
         = 3


Remove the digit:

x = 12




Second Iteration

digit = 12 % 10
      = 2

Build:

reversed = 3 * 10 + 2
         = 32

Then:

x = 1


Third Iteration

digit = 1 % 10
      = 1


Build:

reversed = 32 * 10 + 1
         = 321


Then:

x = 0


The loop terminates.

Result:

321



Overflow Example

Consider:

x = 1534236469

Its reversed value would be:

9646324351


But:

9,646,324,351 > 2,147,483,647


so it cannot fit inside a Java `int`.

The algorithm detects this before the overflow occurs and returns:

0



Why We Don't Use `long`

A simpler implementation could use:

long reversed;


and check the result afterward.

However, the problem explicitly states that the environment does not allow storing 64-bit integers.

Therefore, this solution performs the overflow check while the value is still an `int`.

This makes the implementation compliant with the problem's restriction.


Complexity

A 32-bit integer contains at most 10 decimal digits.

Therefore, the algorithm performs at most 10 iterations.

Time Complexity

O(log₁₀ |x|)


For a 32-bit integer, this is effectively:

O(1)

Space Complexity

Only one additional integer is used:

reversed

Therefore:

Space: O(1)



Optimization

This solution is already asymptotically optimal.

Every digit that needs to be reversed must be examined, so the algorithm requires:

O(number of digits)


time.

It also uses:

O(1)


auxiliary space.

No:

* `String`
* `StringBuilder`
* character arrays
* `long`
* collections
* sorting
* recursion

are required.

The reversal is performed entirely with integer arithmetic.


Key Concepts

* Integer Manipulation
* Digit Extraction
* Modulo Operator
* Integer Division
* Overflow Detection
* 32-bit Signed Integers
* Constant Space
* Arithmetic Algorithms


Language

Java

LeetCode

[LeetCode #7 — Reverse Integer](https://leetcode.com/problems/reverse-integer/description/)
