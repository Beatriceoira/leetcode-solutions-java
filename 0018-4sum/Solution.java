class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;

        if (n < 4) {
            return result;
        }

        Arrays.sort(nums);

        for (int i = 0; i < n - 3; i++) {
            // Skip duplicate first values.
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            // Minimum possible sum is already greater than target.
            if ((long) nums[i] + nums[i + 1] + nums[i + 2] + nums[i + 3] > target) {
                break;
            }

            // Maximum possible sum is still smaller than target.
            if ((long) nums[i] + nums[n - 3] + nums[n - 2] + nums[n - 1] < target) {
                continue;
            }

            for (int j = i + 1; j < n - 2; j++) {
                // Skip duplicate second values.
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }

                // Minimum possible sum is already greater than target.
                if ((long) nums[i] + nums[j] + nums[j + 1] + nums[j + 2] > target) {
                    break;
                }

                // Maximum possible sum is still smaller than target.
                if ((long) nums[i] + nums[j] + nums[n - 2] + nums[n - 1] < target) {
                    continue;
                }

                int left = j + 1;
                int right = n - 1;

                while (left < right) {
                    long sum = (long) nums[i]
                             + nums[j]
                             + nums[left]
                             + nums[right];

                    if (sum < target) {
                        left++;
                    } else if (sum > target) {
                        right--;
                    } else {
                        result.add(Arrays.asList(
                            nums[i],
                            nums[j],
                            nums[left],
                            nums[right]
                        ));

                        int leftValue = nums[left];
                        int rightValue = nums[right];

                        while (left < right && nums[left] == leftValue) {
                            left++;
                        }

                        while (left < right && nums[right] == rightValue) {
                            right--;
                        }
                    }
                }
            }
        }

        return result;
    }
}