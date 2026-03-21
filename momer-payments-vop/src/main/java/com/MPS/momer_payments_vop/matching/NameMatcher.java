package com.MPS.momer_payments_vop.matching;

import com.MPS.momer_payments_vop.enums.MatchResult;
import org.springframework.stereotype.Component;

@Component
public class NameMatcher {

    public NameMatchResult match(String requestedName, String actualName) {
        requestedName = requestedName.toLowerCase();
        actualName = actualName.toLowerCase();

        int n = requestedName.length();
        int m = actualName.length();

        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                int cost = requestedName.charAt(i - 1) == actualName.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(dp[i-1][j] + 1,
                        Math.min(dp[i][j-1] + 1,
                                dp[i-1][j-1] + cost));
            }
        }

        double confidence = 1.0 - (double) dp[n][m] / Math.max(n, m);

        MatchResult matchResult = confidence == 1.0 ? MatchResult.EXACT_MATCH
                : confidence >= 0.6 ? MatchResult.CLOSE_MATCH
                : MatchResult.NO_MATCH;

        return new NameMatchResult(matchResult, confidence);
    }
}
