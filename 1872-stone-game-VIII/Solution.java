class Solution {
    public int stoneGameVIII(int[] stones) {
        int sum = 0;
        for (int stone : stones) {
            sum += stone;
        }

        int maxDiff = sum;

        for (int i = stones.length - 1; i > 1; i--) {
            sum -= stones[i]; 
            maxDiff = Math.max(maxDiff, sum - maxDiff);
        }
        return maxDiff;
    }
}