import java.util.Arrays;

public class LongestSubsequence {

    public static void main(String[] args) {
        int[] nums = {8, 5, 4, 2, 1, 4, 3, 4, 3, 1, 15};
        int k = 3;
        System.out.println(longestSubsequence(nums, k));
    }
    public static int longestSubsequence(int[] nums, int k) {
        int n = nums.length;
        int[] dp = new int[n];


        for (int i = 0; i < n; i++) {
            int maxLen = 0;
            for (int j = 0; j < i; j++) {
                if (nums[i] < nums[j] && nums[j] - nums[i] <= k) {
                    maxLen = Math.max(maxLen, dp[j]);
                }
            }
            dp[i] = maxLen + 1;
        }

        int maxLength = 0;
        for (int len : dp) {
            maxLength = Math.max(maxLength, len);
        }
        System.out.println(Arrays.toString(dp));
        return maxLength;
    }

}
