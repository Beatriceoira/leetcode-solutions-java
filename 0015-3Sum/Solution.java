class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        int n = nums.length;

        Arrays.sort(nums);

        for (int i = 0; i < n - 2; i++) {
            int a = nums[i];

            // Skip duplicate first values.
            if (i > 0 && a == nums[i - 1]) {
                continue;
            }

            // Since nums is sorted, all later values are >= a.
            if (a > 0) {
                break;
            }

            // Even the two largest values cannot reach 0.
            if (a + nums[n - 2] + nums[n - 1] < 0) {
                continue;
            }

            // Even the three smallest values exceed 0.
            if (a + nums[i + 1] + nums[i + 2] > 0) {
                break;
            }

            int left = i + 1;
            int right = n - 1;

            while (left < right) {
                int sum = a + nums[left] + nums[right];

                if (sum < 0) {
                    left++;
                } else if (sum > 0) {
                    right--;
                } else {
                    result.add(Arrays.asList(a, nums[left], nums[right]));

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

        return result;
    }
}