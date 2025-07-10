public class BaselineRunner {
    /**
     * Runs 'trials' simulations sequentially.
     * @return number of successful burns.
     */
    public static int runTrialsSequential(int gridSize, double density, int trials) {
        ForestFireSimulator sim = new ForestFireSimulator(gridSize, density);
        int success = 0;
        for (int t = 0; t < trials; t++) {
            if (sim.runSingleTrial()) {
                success++;
            }
        }
        return success;
    }
}
