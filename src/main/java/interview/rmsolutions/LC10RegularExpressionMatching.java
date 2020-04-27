package interview.rmsolutions;

public class LC10RegularExpressionMatching {

    /**
     * <pre>
     * Copied from https://leetcode.com/problems/regular-expression-matching/discuss/191830/Java-DP-solution-beats-100-with-explanation
     *
     *
     * Consider following example
     *
     * s='aab', p='c*a*b'
     *
     *
     *       c * a * b
     *     0 1 2 3 4 5
     *   0 y
     * a 1
     * a 2
     * b 3
     *
     * dp[i][j] denotes if s.substring(0,i) is valid for pattern p.substring(0,j). For example dp[0][0] == true (denoted by y in the matrix) because when s and p are both empty
     * they match. So if we somehow base dp[i+1][j+1] on previos dp[i][j]'s then the result will be dp[s.length()][p.length()]
     *
     * So what about the first column? for and empty pattern p="" only thing that is valid is an empty string s="" and that is already our dp[0][0] which is true. T
     * hat means rest of `dp[i][0]' is false.
     *
     * s='aab', p='c*a*b'
     *
     *       c * a * b
     *     0 1 2 3 4 5
     *   0 y
     * a 1 n
     * a 2 n
     * b 3 n
     *
     * What about the first row? In other words which pattern p matches empty string s=""?
     * The answer is either an empty pattern p="" or a pattern that can represent an empty string such as p="a*", p="z*"
     * or more interestingly a combination of them as in p="a*b*c*". Below for loop is used to populate dp[0][j].
     * Note how it uses previous states by checking dp[0][j-2]
     *
     *         for (int j=2; j<=p.length(); j++) {
     *             dp[0][j] = p.charAt(j-1) == '*' && dp[0][j-2];
     *         }
     * At this stage our matrix has become as follows: Notice dp[0][2] and dp[0][4] are both true because p="c*" and p="c*a*" can both match an empty string.
     *
     * s='aab', p='c*a*b'
     *
     *       c * a * b
     *     0 1 2 3 4 5
     *   0 y n y n y n
     * a 1 n
     * a 2 n
     * b 3 n
     * So now we can start our main iteration.
     * It is basically the same, we will iterate all possible s lengths (i) for all possible p lengths (j) and we will try to find a relation based on previous results.
     * Turns out there are two cases.
     *
     * (p.charAt(j-1) == s.charAt(i-1) || p.charAt(j-1) == '.') if the current characters match or pattern has . then the result is determined
     * by the previous state dp[i][j] = dp[i-1][j-1].
     * Don't be confused by the charAt(j-1) charAt(i-1) indexes using a -1 offset that is because our dp array is actually one index bigger
     * than our string and pattern lengths to hold the initial state dp[0][0]
     *
     * if p.charAt(j-1) == '*' then either it acts as an empty set and the result is dp[i][j] = dp[i][j-2] or (s.charAt(i-1) == p.charAt(j-2) || p.charAt(j-2) == '.')
     * current char of string equals the char preceding * in pattern so the result is dp[i-1][j]
     *
     * So here is the final state of matrix after we evaluate all elements:
     *
     * s='aab', p='c*a*b'
     *
     *       c * a * b
     *     0 1 2 3 4 5
     *   0 y n y n y n
     * a 1 n n n y y n
     * a 2 n n n n y n
     * b 3 n n n n n y
     * </pre>
     * And here is the code:
     */
    public boolean isMatch(String s, String p) {
        if (p == null || p.length() == 0) {
            return (s == null || s.length() == 0);
        }

        boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
        dp[0][0] = true;
        for (int j = 2; j <= p.length(); j++) {
            dp[0][j] = p.charAt(j - 1) == '*' && dp[0][j - 2];
        }

        for (int j = 1; j <= p.length(); j++) {
            for (int i = 1; i <= s.length(); i++) {
                if (p.charAt(j - 1) == s.charAt(i - 1) || p.charAt(j - 1) == '.') {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (p.charAt(j - 1) == '*') {
                    dp[i][j] = dp[i][j - 2] || ((s.charAt(i - 1) == p.charAt(j - 2) || p.charAt(j - 2) == '.') && dp[i - 1][j]);
                }
            }
        }
        return dp[s.length()][p.length()];
    }
}
