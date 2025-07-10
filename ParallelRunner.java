import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

public class ParallelRunner {
    /**
     * Runs 'trials' simulations in parallel across 'threads' workers.
     * @return number of successful burns.
     */
    public static int runTrialsParallel(int gridSize,
                                        double density,
                                        int trials,
                                        int threads) throws InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(threads);
        LongAdder successCount = new LongAdder();

        // Distribute trials nearly evenly
        int base = trials / threads;
        int extra = trials % threads;

        CountDownLatch latch = new CountDownLatch(threads);
        for (int i = 0; i < threads; i++) {
            int thisBatch = base + (i < extra ? 1 : 0);
            exec.execute(() -> {
                ForestFireSimulator sim = new ForestFireSimulator(gridSize, density);
                int localSuccess = 0;
                for (int t = 0; t < thisBatch; t++) {
                    if (sim.runSingleTrial()) {
                        localSuccess++;
                    }
                }
                successCount.add(localSuccess);
                latch.countDown();
            });
        }

        latch.await();
        exec.shutdown();
        return successCount.intValue();
    }
}
