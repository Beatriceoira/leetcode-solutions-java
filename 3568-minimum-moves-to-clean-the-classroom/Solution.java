import java.util.*;

class Solution {
    public int minMoves(String[] classroom, int energy) {
        int m = classroom.length;
        int n = classroom[0].length();
        int cells = m * n;

        int start = 0;
        int litterCount = 0;

        int[] litterBit = new int[cells];
        boolean[] reset = new boolean[cells];
        boolean[] blocked = new boolean[cells];

        for (int r = 0; r < m; r++) {
            String row = classroom[r];

            for (int c = 0; c < n; c++) {
                int pos = r * n + c;
                char ch = row.charAt(c);

                if (ch == 'S') {
                    start = pos;
                } else if (ch == 'L') {
                    litterBit[pos] = 1 << litterCount++;
                } else if (ch == 'R') {
                    reset[pos] = true;
                } else if (ch == 'X') {
                    blocked[pos] = true;
                }
            }
        }

        if (litterCount == 0) {
            return 0;
        }

        int maskCount = 1 << litterCount;
        int fullMask = maskCount - 1;

        int[] bestEnergy = new int[cells * maskCount];
        Arrays.fill(bestEnergy, -1);

        int energyStates = energy + 1;

        int initialState =
                ((start * maskCount) * energyStates) + energy;

        int[] queue = new int[1024];
        int head = 0;
        int tail = 0;

        queue[tail++] = initialState;
        bestEnergy[start * maskCount] = energy;

        int moves = 0;

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        while (head < tail) {
            int levelEnd = tail;

            while (head < levelEnd) {
                int state = queue[head++];

                int remainingEnergy = state % energyStates;
                int encoded = state / energyStates;

                int mask = encoded % maskCount;
                int pos = encoded / maskCount;

                if (mask == fullMask) {
                    return moves;
                }

                int r = pos / n;
                int c = pos - r * n;

                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d];
                    int nc = c + dc[d];

                    if (nr < 0 || nr >= m ||
                        nc < 0 || nc >= n) {
                        continue;
                    }

                    int nextPos = nr * n + nc;

                    if (blocked[nextPos]) {
                        continue;
                    }

                    int nextEnergy = remainingEnergy - 1;

                    if (nextEnergy < 0) {
                        continue;
                    }

                    if (reset[nextPos]) {
                        nextEnergy = energy;
                    }

                    int nextMask = mask | litterBit[nextPos];

                    if (nextMask == fullMask) {
                        return moves + 1;
                    }

                    int index = nextPos * maskCount + nextMask;

                    if (nextEnergy <= bestEnergy[index]) {
                        continue;
                    }

                    bestEnergy[index] = nextEnergy;

                    int nextState =
                            (index * energyStates) + nextEnergy;

                    if (tail == queue.length) {
                        queue = Arrays.copyOf(queue, queue.length * 2);
                    }

                    queue[tail++] = nextState;
                }
            }

            moves++;
        }

        return -1;
    }
}