class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> triangle = new ArrayList<>(numRows);

        for (int row = 0; row < numRows; row++) {
            List<Integer> current = new ArrayList<>(row + 1);
            current.add(1);

            if (row > 0) {
                List<Integer> previous = triangle.get(row - 1);

                for (int i = 1; i < row; i++) {
                    current.add(previous.get(i - 1) + previous.get(i));
                }

                current.add(1);
            }

            triangle.add(current);
        }

        return triangle;
    }
}