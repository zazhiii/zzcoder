package com.zazhi;

/**
 *
 * @author lixh
 * @since 2025/8/25 21:20
 */
public class Test {

    private static int[][] t = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
    };

    public static void main(String[] args) {
        ;
    }

    public static int[] findDiagonalOrder(int[][] mat) {
        int i = 0, j = 0;
        boolean up = true;
        int n = mat.length, m = mat[0].length;
        int[] ans = new int[n * m];
        int p = 0;
        while (i != n - 1 || j != m - 1) {
            if (up) {
                ans[p++] = mat[i][j];
                if (i - 1 >= 0 && j + 1 < m) {
                    i--;
                    j++;
                } else if (i - 1 < 0 && j + 1 < m) {
                    j++;
                    up = false;
                } else {
                    i++;
                    up = false;
                }
            } else {
                ans[p++] = mat[i][j];
                if (i + 1 < n && j - 1 >= 0) {
                    i++;
                    j--;
                } else if (j - 1 < 0 && i + 1 < n) {
                    i++;
                    up = true;
                } else {
                    j++;
                    up = true;
                }
            }
        }
        ans[n * m - 1] = mat[n - 1][m - 1];
        return ans;
    }
}
