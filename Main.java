import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class Main {
    public static void main(String[] args) throws Exception {
        // === Configuration ===
        int gridSize = 50;      // N × N grid
        int trials   = 5000;    // Monte Carlo trials per density
        int threads  = 8;       // Number of parallel threads

        // How many logical processors the JVM sees
        int cores = Runtime.getRuntime().availableProcessors();

        // Bean to get process CPU time
        OperatingSystemMXBean osBean =
            ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

        System.out.println("Monte Carlo Forest Fire Simulation");
        System.out.printf("Grid: %dx%d | Trials: %d | Threads: %d | Cores: %d%n%n",
                          gridSize, gridSize, trials, threads, cores);

        System.out.println("Density  Prob(Seq)  Prob(Par)  Seq(s)  SeqCPU%   Par(s)  ParCPU%   Speed-up");
        System.out.println("-------  ---------  ---------  ------  -------   ------  -------   --------");

        // Sweep density from 0.30 to 0.70 in steps of 0.05
        for (double p = 0.30; p <= 0.70 + 1e-9; p += 0.05) {
            // —— Sequential run ——
            long wallSeqStart = System.nanoTime();
            long cpuSeqStart  = osBean.getProcessCpuTime();

            int successSeq = BaselineRunner.runTrialsSequential(gridSize, p, trials);

            long wallSeqEnd = System.nanoTime();
            long cpuSeqEnd  = osBean.getProcessCpuTime();

            double timeSeq    = (wallSeqEnd - wallSeqStart) / 1e9;
            double cpuTimeSeq = (cpuSeqEnd  - cpuSeqStart)  / 1e9;
            double utilSeq    = cpuTimeSeq / (timeSeq * cores) * 100.0;
            double probSeq    = (double) successSeq  / trials;

            // —— Parallel run ——
            long wallParStart = System.nanoTime();
            long cpuParStart  = osBean.getProcessCpuTime();

            int successPar = ParallelRunner.runTrialsParallel(gridSize, p, trials, threads);

            long wallParEnd = System.nanoTime();
            long cpuParEnd  = osBean.getProcessCpuTime();

            double timePar    = (wallParEnd - wallParStart) / 1e9;
            double cpuTimePar = (cpuParEnd  - cpuParStart)    / 1e9;
            double utilPar    = cpuTimePar / (timePar * cores) * 100.0;
            double probPar    = (double) successPar  / trials;

            // Speed-up
            double speedup = timeSeq / timePar;

            // Print formatted row
            System.out.printf(
                "%.2f     %7.3f     %7.3f   %6.3f   %5.1f%%   %6.3f   %5.1f%%   %7.2f%n",
                p,
                probSeq, probPar,
                timeSeq, utilSeq,
                timePar, utilPar,
                speedup
            );
        }
    }
}
