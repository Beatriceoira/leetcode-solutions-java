class Solution {
    private int count;

    public int averageOfSubtree(TreeNode root) {
        count = 0;
        dfs(root);
        return count;
    }

    private long dfs(TreeNode node) {
        if (node == null) return 0L;

        long left = dfs(node.left);
        long right = dfs(node.right);

        int leftSize = (int) left;
        int rightSize = (int) right;

        int size = leftSize + rightSize + 1;
        int sum = (int) (left >>> 32) + (int) (right >>> 32) + node.val;

        if (sum / size == node.val) {
            count++;
        }

        return ((long) sum << 32) | (size & 0xFFFFFFFFL);
    }
}