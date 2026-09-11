class Solution {
    public int maxArea(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;
        while (left < right) {
            int hLeft = height[left];
            int hRight = height[right];
            int area = (right - left) * Math.min(hLeft, hRight);
            if (area > maxArea) {
                maxArea = area;
            }
            if (hLeft < hRight) {
                while (left < right && height[left] <= hLeft) {
                    left++;
                }
            } else {
                while (left < right && height[right] <= hRight) {
                    right--;
                }
            }
        }
        return maxArea;
    }
}
