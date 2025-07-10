import java.util.Random;

public class ForestFireSimulator {
    private final int size;
    private final double density;
    private final Random rnd;

    public ForestFireSimulator(int size, double density) {
        this.size = size;
        this.density = density;
        this.rnd = new Random();
    }

    /** Run one Monte Carlo trial: build forest and test percolation. */
    public boolean runSingleTrial() {
        boolean[][] forest = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                forest[i][j] = rnd.nextDouble() < density;
            }
        }
        return FireSpreadChecker.fireReachesRightEdge(forest);
    }
}
