import java.util.Arrays;

class Solution {
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);

        int n = nums.length;
        int closest = nums[0] + nums[1] + nums[2];

        for (int i = 0; i < n - 2; i++) {

            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            int minSum = nums[i] + nums[i + 1] + nums[i + 2];

            if (minSum > target) {
                if (minSum - target < Math.abs(closest - target)) {
                    closest = minSum;
                }
                break;
            }

            int maxSum = nums[i] + nums[n - 2] + nums[n - 1];

            if (maxSum < target) {
                if (target - maxSum < Math.abs(closest - target)) {
                    closest = maxSum;
                }
                continue;
            }

            int left = i + 1;
            int right = n - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];

                int difference = sum - target;
                int closestDifference = closest - target;

                if (Math.abs(difference) < Math.abs(closestDifference)) {
                    closest = sum;
                }

                if (difference == 0) {
                    return sum;
                }

                if (difference < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }

        return closest;
    }
}