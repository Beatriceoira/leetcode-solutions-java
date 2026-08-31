class Solution {
    public int[] nodesBetweenCriticalPoints(ListNode head) {
         if (head == null || head.next == null || head.next.next == null) {
            return new int[]{-1, -1};
        }

        int firstIdx = -1;
        int prevIdx = -1;
        int minDistance = Integer.MAX_VALUE;
        
        int index = 1;
        int prevVal = head.val;
        ListNode curr = head.next;

        while (curr.next != null) {
            int currVal = curr.val;
            int nextVal = curr.next.val;

            if ((currVal > prevVal && currVal > nextVal) || 
                (currVal < prevVal && currVal < nextVal)) {
                
                if (firstIdx == -1) {
                    firstIdx = index;
                } else {
                    int dist = index - prevIdx;
                    if (dist < minDistance) {
                        minDistance = dist;
                    }
                }
                prevIdx = index;
            }

            prevVal = currVal;
            curr = curr.next;
            index++;
        }

        if (prevIdx == firstIdx) {
            return new int[]{-1, -1};
        }

        return new int[]{minDistance, prevIdx - firstIdx};
    }
}