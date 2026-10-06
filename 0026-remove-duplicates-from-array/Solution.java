class Solution {
    public int removeDuplicates(int[] nums) {
        int k = 1;
        int prev = nums[0];

        for (int i = 1; i < nums.length; i++) {
            int curr = nums[i];

            if (curr != prev) {
                nums[k++] = curr;
                prev = curr;
            }
        }

        return k;
    }
}