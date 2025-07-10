import java.util.LinkedList;
import java.util.Queue;

public class FireSpreadChecker {
    /**
     * Returns true if fire starting on the left edge reaches the right edge.
     * @param forest boolean[n][n] grid; true = tree present.
     */
    public static boolean fireReachesRightEdge(boolean[][] forest) {
        int n = forest.length;
        boolean[][] visited = new boolean[n][n];
        Queue<int[]> queue = new LinkedList<>();

        // Ignite all trees on the left edge
        for (int i = 0; i < n; i++) {
            if (forest[i][0]) {
                queue.add(new int[]{i, 0});
                visited[i][0] = true;
            }
        }

        // 4-directional spread
        int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int x = cur[0], y = cur[1];
            if (y == n - 1) return true;  // reached right edge

            for (int[] d : dirs) {
                int nx = x + d[0], ny = y + d[1];
                if (nx >= 0 && nx < n && ny >= 0 && ny < n
                        && forest[nx][ny] && !visited[nx][ny]) {
                    visited[nx][ny] = true;
                    queue.add(new int[]{nx, ny});
                }
            }
        }
        return false;
    }
}
