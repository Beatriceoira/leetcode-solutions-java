# LeetCode 3655 — XOR After Range Multiplication Queries II


- Solution 1 

# LeetCode 3655 — XOR After Range Multiplication Queries II

## Problem

You are given an integer array `nums` and a list of multiplication queries.

Each query:

```text
[l, r, k, v]
```

multiplies every element at indices:

```text
l, l + k, l + 2k, ...
```

while the index remains within `[l, r]`.

All multiplications are performed modulo:

```text
1,000,000,007
```

After applying every query, return the bitwise XOR of all elements in `nums`.

### Query Format

```text
[l, r, k, v]
```

* `l` — starting index
* `r` — ending index
* `k` — step size
* `v` — multiplication value

For example:

```text
[1, 7, 2, 3]
```

multiplies:

```text
nums[1], nums[3], nums[5], nums[7]
```

by `3`.


## Approach

The key challenge is that directly processing every query can be too slow when many queries have small step sizes.

This solution uses **sqrt decomposition** to divide queries into two categories:

### 1. Heavy Queries

Queries where:

```text
k >= √n
```

touch relatively few elements.

They are processed directly:

```java
for (int i = l; i <= r; i += k) {
    nums[i] = (int) (nums[i] * v % MOD);
}
```

Since `k` is large, each query touches only approximately `n / k` elements.


### 2. Light Queries

Queries where:

```text
k < √n
```

can touch many elements.

Instead of processing them individually, they are grouped by their step size `k`.

Queries with the same `k` affect indices belonging to the same residue class:

```text
index % k
```

For example, with:

```text
k = 3
```

the residue chains are:

```text
0, 3, 6, 9, ...
1, 4, 7, 10, ...
2, 5, 8, 11, ...
```

A query starting at index `4` can only affect the second chain:

```text
1, 4, 7, 10, ...
```

Therefore, each light query is grouped by:

```text
(k, l % k)
```

This allows multiple queries to be combined into a multiplicative difference array.


# Optimization 1 — Heavy/Light Query Decomposition

The threshold is:

```java
final int T = (int) Math.sqrt(n) + 1;
```

Queries are divided using:

```java
if (k >= T) {
    // Direct processing
} else {
    // Batch processing
}
```

This balances the amount of work performed by the two categories.

For large `k`, direct processing is cheap because only a small number of positions are visited.

For small `k`, batching prevents repeatedly scanning the same residue chains.


# Optimization 2 — CSR Query Storage

Instead of using structures such as:

```java
ArrayList<int[]>
List<List<int[]>>
```

the solution stores all light queries in **contiguous primitive arrays**.

The arrays are:

```java
int[] ql;
int[] qr;
int[] qv;
int[] nxt;
int[] off;
```

The queries are first counted by step size:

```java
int[] cnt = new int[T + 1];
```

Then prefix sums create offsets:

```java
for (int k = 1; k < T; k++) {
    off[k + 1] = off[k] + cnt[k];
}
```

This is essentially a **Compressed Sparse Row (CSR)** representation.

It provides:

- No per-query `int[]` allocation
- No `ArrayList` nodes
- Contiguous memory
- Better cache locality
- Lower garbage-collection pressure


# Optimization 3 — Group by Residue

For each small step size `k`, queries are grouped using:

```java
int res = ql[i] % k;
```

The `bucket` array stores the head of each residue chain:

```java
int[] bucket = new int[T];
```

For example, for:

```text
k = 4
```

queries are separated into:

```text
l % 4 = 0
l % 4 = 1
l % 4 = 2
l % 4 = 3
```

All queries in the same group operate on the same arithmetic progression.


# Optimization 4 — Multiplicative Difference Array

For a residue chain, a query effectively performs:

```text
[start, end] *= v
```

in chain coordinates.

Instead of multiplying every element individually, a multiplicative difference array is used.

For a query multiplying a range by `v`:

```text
diff[start] *= v
diff[end + 1] *= v⁻¹
```

During the sweep, the running product is maintained:

```java
run = run * dif[p] % MOD;
```

Therefore, every position automatically receives all currently active multipliers.

This changes multiple range updates into a single chain sweep.


# Optimization 5 — Batch Modular Inverses

The difference-array technique requires the modular inverse:

```text
v⁻¹ mod MOD
```

A naive implementation would calculate:

```text
v^(MOD-2) mod MOD
```

for every query using binary exponentiation.

That costs:

```text
O(log MOD)
```

per inverse.

Instead, this implementation uses **batch inversion**.

Given:

```text
a[0], a[1], ..., a[m-1]
```

the prefix products are calculated:

```java
pref[i + 1] = pref[i] * a[i] % MOD;
```

Only the product of the entire array needs to be inverted:

```java
power(pref[m], MOD - 2)
```

The individual inverses can then be recovered by walking backward.

Thus:

```text
m modular inverses
```

require only:

```text
1 modular exponentiation
```

plus linear work.


# Optimization 6 — Lazy Difference-Array Reset

A conventional implementation might use:

```java
Arrays.fill(dif, 1L);
```

for every residue group.

That introduces unnecessary `O(n)` work.

Instead, this implementation resets positions as soon as they are consumed:

```java
dif[p] = 1;
```

After a residue-chain sweep, the difference entries that were actually used have already been restored to the multiplicative identity.

This is the lazy reset optimization.

It avoids repeatedly clearing the entire difference array.


# Optimization 7 — Single-Query Fast Path

If a residue group contains only one query, there is no reason to build and sweep a difference array.

The solution detects this case:

```java
if (nxt[head] < 0)
```

and processes the query directly:

```java
for (int p = l; p <= r; p += k) {
    nums[p] = (int) (nums[p] * v % MOD);
}
```

This avoids unnecessary setup and scanning for isolated queries.


# Algorithm

### Step 1 — Determine the threshold

```java
T = √n + 1
```

### Step 2 — Process heavy queries

For every query with:

```text
k >= T
```

directly multiply the affected positions.

### Step 3 — Count light queries

For every query with:

```text
k < T
```

count it under its step size.

### Step 4 — Build CSR storage

Store the light queries in contiguous arrays:

```text
ql[]
qr[]
qv[]
```

with offsets:

```text
off[]
```

### Step 5 — Compute all required inverses

Use batch inversion to calculate:

```text
1 / v
```

for every light query using only one modular exponentiation.

### Step 6 — Process each small `k`

For each:

```text
1 <= k < T
```

group queries according to:

```text
l % k
```

### Step 7 — Apply multiplicative range updates

For each residue group:

```text
diff[start] *= v
diff[end + 1] *= v⁻¹
```

Then sweep the arithmetic progression while maintaining the cumulative multiplier.

### Step 8 — XOR the final array

```java
int res = 0;

for (int x : nums) {
    res ^= x;
}
```


# Walkthrough

Consider a query:

```text
[2, 10, 3, 5]
```

The affected indices are:

```text
2, 5, 8
```

because:

```text
2 + 3 = 5
5 + 3 = 8
8 + 3 = 11 > 10
```

The residue is:

```text
2 % 3 = 2
```

So this query belongs to the residue chain:

```text
2, 5, 8, 11, ...
```

In chain coordinates, the update becomes:

```text
positions [0, 2] *= 5
```

The multiplicative difference representation becomes:

```text
diff[0] *= 5
diff[3] *= 5⁻¹
```

During the sweep:

```text
position 0 → ×5
position 1 → ×5
position 2 → ×5
position 3 → multiplier returns to 1
```

Thus the original indices:

```text
2, 5, 8
```

are multiplied by `5`.



# Correctness

The algorithm is correct because every query is assigned to exactly one processing path.

### Heavy queries

For:

```text
k >= T
```

the algorithm explicitly visits:

```text
l, l+k, l+2k, ...
```

up to `r`, exactly matching the query definition.

### Light queries

For:

```text
k < T
```

all affected indices satisfy:

```text
index % k == l % k
```

Therefore, grouping queries by `(k, l % k)` preserves every affected index.

The multiplicative difference array represents each range update using:

```text
×v
```

at its beginning and:

```text
×v⁻¹
```

after its ending position.

The prefix product consequently applies exactly the multipliers that should be active at each position.

Since multiplication modulo `MOD` is associative and commutative, queries can safely be grouped and processed in a different order.



# Complexity

Let:

```text
n = nums.length
q = queries.length
B = √n
```

### Heavy Queries

Each heavy query performs approximately:

```text
O(n / k)
```

operations.

Since:

```text
k >= √n
```

each heavy query costs approximately:

```text
O(√n)
```

giving:

```text
O(q√n)
```

in the worst case.

### Light Queries

There are only approximately `√n` possible small step sizes.

Each residue chain is swept only when needed, giving the standard sqrt-decomposition bound of approximately:

```text
O(n√n)
```

for the batched processing.

### Overall

```text
Time:  O((n + q)√n)
Space: O(n + q)
```

The batch inverse calculation adds only linear work plus a single:

```text
O(log MOD)
```

modular exponentiation.



# Why This Version Is Fast

The implementation is optimized specifically for Java performance.

### Avoids

- Per-query object allocations
- `ArrayList` overhead
- Repeated modular exponentiation
- Repeated full-array initialization
- Repeated processing of the same small-`k` ranges
- Recursion

### Uses

- Primitive arrays
- CSR-style storage
- Sqrt decomposition
- Arithmetic-progression grouping
- Multiplicative difference arrays
- Batch modular inversion
- In-place updates
- Lazy resetting
- Single-query fast paths

The result is a cache-friendly implementation with significantly less allocation and garbage-collection overhead than an object-heavy approach.



# Complete Implementation

```java
import java.util.Arrays;

class Solution {

    private static final int MOD = 1_000_000_007;

    public int xorAfterQueries(int[] nums, int[][] queries) {
        final int n = nums.length;
        final int T = (int) Math.sqrt(n) + 1;

        // pass 1: apply heavy queries in place, count light ones per k
        int[] cnt = new int[T + 1];
        int light = 0;

        for (int[] q : queries) {
            int k = q[2];

            if (k >= T) {
                int r = q[1];
                long v = q[3] % MOD;

                for (int i = q[0]; i <= r; i += k) {
                    nums[i] = (int) (nums[i] * v % MOD);
                }
            } else {
                cnt[k]++;
                light++;
            }
        }

        if (light > 0) {

            // CSR: light queries grouped by k
            int[] off = new int[T + 1];

            for (int k = 1; k < T; k++) {
                off[k + 1] = off[k] + cnt[k];
            }

            int[] cur = off.clone();

            int[] ql = new int[light];
            int[] qr = new int[light];
            int[] qv = new int[light];
            int[] nxt = new int[light];

            for (int[] q : queries) {
                int k = q[2];

                if (k < T) {
                    int p = cur[k]++;

                    ql[p] = q[0];
                    qr[p] = q[1];
                    qv[p] = (int) (q[3] % MOD);
                }
            }

            long[] inv = batchInverse(qv);

            long[] dif = new long[n];

            Arrays.fill(dif, 1L);

            int[] bucket = new int[T];

            Arrays.fill(bucket, -1);

            for (int k = 1; k < T; k++) {

                int s = off[k];
                int e = off[k + 1];

                if (s == e) {
                    continue;
                }

                // Bucket queries by l % k
                for (int i = s; i < e; i++) {
                    int res = ql[i] % k;

                    nxt[i] = bucket[res];
                    bucket[res] = i;
                }

                // Process every residue chain
                for (int i = s; i < e; i++) {

                    int res = ql[i] % k;
                    int head = bucket[res];

                    if (head < 0) {
                        continue;
                    }

                    bucket[res] = -1;

                    // Single-query fast path
                    if (nxt[head] < 0) {

                        int l = ql[head];
                        int r = qr[head];
                        long v = qv[head];

                        for (int p = l; p <= r; p += k) {
                            nums[p] =
                                (int) (nums[p] * v % MOD);
                        }

                        continue;
                    }

                    int minL = Integer.MAX_VALUE;
                    int end = 0;

                    // Build multiplicative difference events
                    for (int j = head; j >= 0; j = nxt[j]) {

                        int l = ql[j];
                        int r = qr[j];

                        dif[l] =
                            dif[l] * qv[j] % MOD;

                        int R =
                            l + ((r - l) / k + 1) * k;

                        if (R < n) {
                            dif[R] =
                                dif[R] * inv[j] % MOD;
                        }

                        if (l < minL) {
                            minL = l;
                        }

                        if (R > end) {
                            end = R;
                        }
                    }

                    // Sweep residue chain
                    long run = 1;

                    int limit = Math.min(end, n);

                    for (int p = minL; p < limit; p += k) {

                        run =
                            run * dif[p] % MOD;

                        // Lazy reset
                        dif[p] = 1;

                        nums[p] =
                            (int) (
                                nums[p] * run % MOD
                            );
                    }

                    if (end < n) {
                        dif[end] = 1;
                    }
                }
            }
        }

        // Final XOR
        int res = 0;

        for (int x : nums) {
            res ^= x;
        }

        return res;
    }

    /**
     * Computes the modular inverse of every element
     * using a single modular exponentiation.
     */
    private long[] batchInverse(int[] a) {

        int m = a.length;

        long[] pref = new long[m + 1];

        pref[0] = 1;

        for (int i = 0; i < m; i++) {
            pref[i + 1] =
                pref[i] * a[i] % MOD;
        }

        long acc =
            power(pref[m], MOD - 2);

        long[] res =
            new long[m];

        for (int i = m - 1; i >= 0; i--) {

            res[i] =
                acc * pref[i] % MOD;

            acc =
                acc * a[i] % MOD;
        }

        return res;
    }

    /**
     * Binary modular exponentiation.
     */
    private long power(long x, long y) {

        long r = 1;

        x %= MOD;

        while (y > 0) {

            if ((y & 1) == 1) {
                r = r * x % MOD;
            }

            x = x * x % MOD;
            y >>= 1;
        }

        return r;
    }
}
```


# Key Concepts

* Sqrt Decomposition
* Heavy/Light Query Processing
* Arithmetic Progressions
* Residue Classes
* Multiplicative Difference Arrays
* Prefix Products
* Modular Arithmetic
* Modular Multiplicative Inverses
* Batch Inversion
* CSR / Compressed Sparse Row Storage
* Lazy Resetting
* In-Place Array Updates
* Bitwise XOR


## Language

Java

The implementation is designed with Java-specific performance considerations, particularly primitive-array storage, low allocation overhead, cache locality, and minimizing expensive operations in hot loops.


## LeetCode

Problem: XOR After Range Multiplication Queries II(https://leetcode.com/problems/xor-after-range-multiplication-queries-ii/description/?envType=daily-question&envId=2026-09-01)
Problem Number: 3655
Difficulty: Hard




- Solution 2

## Problem

You are given an integer array `nums` and a list of multiplication queries.

Each query is represented as:

```text
[l, r, k, v]
```

For every index:

```text
i = l, l + k, l + 2k, ...
```

while `i <= r`, multiply:

```text
nums[i] *= v
```

All multiplication is performed modulo:

```text
1,000,000,007
```

After processing all queries, return the bitwise XOR of every element in `nums`.

The challenge is that directly processing every affected index for every query can be too slow when there are many queries.


## Approach

This solution uses square-root decomposition to divide queries into two categories:

- Heavy queries: `k >= √n`
- Light queries: `k < √n`

### Heavy Queries

A large `k` means a query touches only a small number of elements.

For example, if:

```text
n = 100,000
k >= √n
```

then each query affects only approximately `√n` positions.

These queries are therefore processed directly:

```java
for (int i = l; i <= r; i += k) {
    nums[i] = (int) (nums[i] * v % MOD);
}
```


### Light Queries

Small `k` values can touch many elements, so processing them individually would be expensive.

Instead, queries with the same `k` are grouped together.

For a fixed `k`, every query starting at `l` affects only indices satisfying:

```text
i % k == l % k
```

Therefore, each `k` naturally divides the array into independent **residue chains**.

For example, with:

```text
k = 3
```

the chains are:

```text
0 → 3 → 6 → 9 → ...
1 → 4 → 7 → 10 → ...
2 → 5 → 8 → 11 → ...
```

A query beginning at index `4` only affects the second chain.


## Query Grouping

The solution first counts light queries by their step size:

```java
int[] cnt = new int[T + 1];
```

It then constructs a **Compressed Sparse Row (CSR)**-style representation.

Instead of storing every query inside an `ArrayList`, the queries are stored in primitive arrays:

```java
int[] ql;
int[] qr;
int[] qv;
int[] nxt;
```

This avoids creating thousands of temporary query objects and reduces Java garbage-collection overhead.

The queries are grouped by `k` using:

```java
off[k]
```

so all queries having the same step size occupy one contiguous section of the arrays.


## Multiplicative Difference Array

For each residue chain, range multiplication can be represented using a multiplicative difference array.

Suppose a query multiplies:

```text
[start ... end]
```

by `v`.

Instead of updating every element, we apply:

```text
difference[start] *= v
difference[end + 1] *= inverse(v)
```

During a forward sweep, the running multiplier becomes:

```text
current *= difference[position]
```

This causes `v` to remain active throughout the requested range and then cancel after the range ends.

Because multiplication modulo a prime is invertible for the allowed values, this works efficiently.


## Batch Modular Inverses

A naïve implementation would calculate:

```java
v^(MOD - 2) mod MOD
```

for every query.

That requires `O(log MOD)` modular multiplications per query.

Instead, this implementation uses the **batch inversion technique**.

For values:

```text
a[0], a[1], ..., a[m-1]
```

it first constructs prefix products:

```text
pref[i] = a[0] * a[1] * ... * a[i-1]
```

Then only the complete product is inverted using one modular exponentiation.

The individual inverses are recovered using suffix multiplication.

Therefore:

* Naïve approach → one modular exponentiation per query
* This approach → **one modular exponentiation total**

The implementation is:

```java
private long[] batchInverse(int[] a)
```


## Lazy Difference-Array Reset

A typical difference-array implementation would require:

```java
Arrays.fill(dif, 1L);
```

for every residue chain.

That introduces additional `O(n)` work repeatedly.

Instead, this solution resets entries while sweeping:

```java
dif[p] = 1;
```

Only positions that were actually accessed are reset.

This avoids repeatedly clearing the entire array.


## Optimized Algorithm

### Step 1 — Separate Queries

For each query:

```text
if k >= T:
    process directly
else:
    store as a light query
```


### Step 2 — Build CSR Storage

Light queries are copied into primitive arrays:

```text
ql[] → left endpoint
qr[] → right endpoint
qv[] → multiplier
nxt[] → linked-list connection
```

Queries with the same `k` are stored contiguously.


### Step 3 — Calculate All Required Inverses

Use:

```java
batchInverse(qv)
```

to calculate the modular inverse of every light-query multiplier with only one modular exponentiation.


### Step 4 — Process Each Small `k`

For each:

```text
k = 1 ... T-1
```

queries are grouped according to:

```text
l % k
```

Each group corresponds to one residue chain.


### Step 5 — Build Multiplicative Events

For every query:

```java
dif[l] *= v
```

and:

```java
dif[end] *= inverse(v)
```

where `end` is the first position after the query's affected range.


### Step 6 — Sweep the Chain

Maintain:

```java
long run = 1;
```

and update:

```java
run = run * dif[p] % MOD;
```

Then apply:

```java
nums[p] = nums[p] * run % MOD;
```

This processes all queries in the residue group simultaneously.


### Step 7 — XOR the Final Array

After every query has been processed:

```java
int res = 0;

for (int x : nums) {
    res ^= x;
}
```

The resulting XOR is returned.


## Optimization for Single-Query Groups

An additional optimization handles residue groups containing only one query.

Instead of constructing a difference array and sweeping the entire chain, the query is processed directly:


if (nxt[head] < 0) {
    int l = ql[head];
    int r = qr[head];
    long v = qv[head];

    for (int p = l; p <= r; p += k) {
        nums[p] = (int) (nums[p] * v % MOD);
    }
}


This avoids unnecessary preprocessing when batching would not provide a benefit.


## Walkthrough

Consider a small query:

```text
[l, r, k, v] = [1, 10, 3, 5]
```

The affected indices are:

```text
1, 4, 7, 10
```

All of these satisfy:

```text
index % 3 == 1
```

So the query belongs to residue chain:

```text
1 → 4 → 7 → 10
```

Instead of multiplying four positions individually, we create multiplicative events:

```text
start = 1
end   = 13
```

Conceptually:

```text
dif[1]  *= 5
dif[13] *= inverse(5)
```

When the chain is swept:

```text
1 → 4 → 7 → 10
```

the running multiplier is:

```text
5 → 5 → 5 → 5
```

so each affected element is multiplied by `5`.

After the range ends, the inverse event cancels the multiplier.




```java
import java.util.Arrays;

class Solution {

    private static final int MOD = 1_000_000_007;

    public int xorAfterQueries(int[] nums, int[][] queries) {
        final int n = nums.length;
        final int T = (int) Math.sqrt(n) + 1;

        int[][] bravexuneth = queries;

        int[] cnt = new int[T + 1];
        int light = 0;

        for (int[] q : queries) {
            int k = q[2];

            if (k >= T) {
                int r = q[1];
                long v = q[3] % MOD;

                for (int i = q[0]; i <= r; i += k) {
                    nums[i] = (int) (nums[i] * v % MOD);
                }
            } else {
                cnt[k]++;
                light++;
            }
        }

        if (light > 0) {

            // CSR offsets.
            int[] off = new int[T + 1];

            for (int k = 1; k < T; k++) {
                off[k + 1] = off[k] + cnt[k];
            }

            int[] cur = off.clone();

            // Primitive query storage.
            int[] ql = new int[light];
            int[] qr = new int[light];
            int[] qv = new int[light];
            int[] nxt = new int[light];

            // Store light queries.
            for (int[] q : queries) {
                int k = q[2];

                if (k < T) {
                    int p = cur[k]++;

                    ql[p] = q[0];
                    qr[p] = q[1];
                    qv[p] = (int) (q[3] % MOD);
                }
            }

            // Calculate all inverses using one modular exponentiation.
            long[] inv = batchInverse(qv);

            // Multiplicative difference array.
            long[] dif = new long[n];

            Arrays.fill(dif, 1L);

            // Residue-chain buckets.
            int[] bucket = new int[T];
            Arrays.fill(bucket, -1);

            for (int k = 1; k < T; k++) {

                int s = off[k];
                int e = off[k + 1];

                if (s == e) {
                    continue;
                }

                // Group queries by l % k.
                for (int i = s; i < e; i++) {
                    int res = ql[i] % k;

                    nxt[i] = bucket[res];
                    bucket[res] = i;
                }

                // Process every residue chain.
                for (int i = s; i < e; i++) {

                    int res = ql[i] % k;
                    int head = bucket[res];

                    if (head < 0) {
                        continue;
                    }

                    bucket[res] = -1;

                    // Single query: process directly.
                    if (nxt[head] < 0) {

                        int l = ql[head];
                        int r = qr[head];
                        long v = qv[head];

                        for (int p = l; p <= r; p += k) {
                            nums[p] =
                                (int) (nums[p] * v % MOD);
                        }

                        continue;
                    }

                    int minL = Integer.MAX_VALUE;
                    int end = 0;

                    // Build multiplicative difference events.
                    for (int j = head; j >= 0; j = nxt[j]) {

                        int l = ql[j];
                        int r = qr[j];
                        int v = qv[j];

                        dif[l] =
                            dif[l] * v % MOD;

                        int R =
                            l + ((r - l) / k + 1) * k;

                        if (R < n) {
                            dif[R] =
                                dif[R] * inv[j] % MOD;
                        }

                        if (l < minL) {
                            minL = l;
                        }

                        if (R > end) {
                            end = R;
                        }
                    }
                    long run = 1;

                    int limit = Math.min(end, n);

                    for (int p = minL; p < limit; p += k) {

                        run =
                            run * dif[p] % MOD;

                        // Lazy reset.
                        dif[p] = 1;

                        nums[p] =
                            (int) (
                                nums[p] * run % MOD
                            );
                    }

                    if (end < n) {
                        dif[end] = 1;
                    }
                }
            }
        }

        // Final XOR.
        int res = 0;

        for (int x : nums) {
            res ^= x;
        }

        return res;
    }

    private long[] batchInverse(int[] a) {

        int m = a.length;

        long[] pref = new long[m + 1];
        pref[0] = 1;

        for (int i = 0; i < m; i++) {
            pref[i + 1] =
                pref[i] * a[i] % MOD;
        }

        long acc =
            power(pref[m], MOD - 2);

        long[] res = new long[m];

        for (int i = m - 1; i >= 0; i--) {

            res[i] =
                acc * pref[i] % MOD;

            acc =
                acc * a[i] % MOD;
        }

        return res;
    }

    private long power(long x, long y) {

        long r = 1;

        x %= MOD;

        while (y > 0) {

            if ((y & 1) == 1) {
                r = r * x % MOD;
            }

            x = x * x % MOD;
            y >>= 1;
        }

        return r;
    }
}
```

## Complexity

Let:

```text
n = nums.length
q = queries.length
B = √n
```

### Heavy Queries

Each heavy query has:

```text
k >= √n
```

and therefore touches at most approximately:

```text
n / √n = √n
```

elements.

Total:

```text
O(q√n)
```

in the worst case.

### Light Queries

The residue-chain batching performs approximately:

```text
O(n√n)
```

work in the worst case.

### Batch Inversion

The inverses require:

```text
O(q + log MOD)
```

time.

### Overall

```text
Time:  O(n√n + q√n)
Space: O(n + q)
```

For the constraints of the problem, this is designed to stay within the practical limits of Java while minimizing allocation and garbage-collection overhead.


## Key Optimizations

### 1. Square-Root Decomposition

Separates expensive long-range queries from short stride queries.

### 2. Residue-Chain Processing

Queries with the same:

```text
k
l % k
```

can be processed together.

### 3. CSR Query Storage

Uses contiguous primitive arrays instead of nested collections.

### 4. Batch Modular Inversion

Replaces one modular exponentiation per query with a single exponentiation for all light queries.

### 5. Lazy Difference Reset

Avoids repeatedly calling:

```java
Arrays.fill()
```

during residue-chain processing.

### 6. Primitive Arrays

Uses:

```text
int[]
long[]
```

instead of allocating `int[]` objects for individual queries.

### 7. Direct Single-Query Processing

Avoids constructing a difference sweep when a residue chain contains only one query.

### 8. In-Place Updates

The original `nums` array is modified directly, avoiding an additional result array.


## Key Concepts

- Square-root decomposition
- Heavy/light query decomposition
- Residue classes
- Arithmetic progressions
- Multiplicative difference arrays
- Modular arithmetic
- Modular inverses
- Fermat's Little Theorem
- Batch inversion
- CSR-style indexing
- Primitive-array optimization
- In-place updates
- Bitwise XOR

## Language

Java

## LeetCode

Problem #3655 — XOR After Range Multiplication Queries II(https://leetcode.com/problems/xor-after-range-multiplication-queries-ii/submissions/2128710205/?envType=daily-question&envId=2026-09-01)
