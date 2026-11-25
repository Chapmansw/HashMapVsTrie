import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
public class MemoryMeasurement {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            System.err.println("USAGE: MemoryMeasurement dataSet(wikipedia, dna_small, dna_medium, dna_large, or random) structure(trie or hashmap)");
            System.exit(-1);
        }
        String dataSet = args[0];
        String structure = args[1];

        MemoryMonitor memoryMonitor = new MemoryMonitor(100);
        Thread monitorThread = new Thread(memoryMonitor, "memory-monitor");
        monitorThread.setDaemon(true);
        monitorThread.start();

        // TODO: write code here to load the data set into the data structure
        try{
            WordCounts w;
            if(structure.equals("trie")){
                w=new WordCountsTrie();
            }else if(structure.equals("hashmap")){
                w=new WordCountsHashMap();
            }
            else{
                throw new Exception();
            }
            Parsing.loadData(dataSet, w);
        }catch(Exception e){
            System.out.println("Failure to load data");
        }

        Thread.sleep(1000);
        memoryMonitor.stop();
        monitorThread.join();

        long maxMemory = (memoryMonitor.getMaxMemoryUsed() / 1024 / 1024); // in MB
        System.out.printf("For data set %s, data structure %s:\n", dataSet, structure);
        System.out.println(maxMemory+"max memory in MB");


        String csvFile = "MemoryMeasurementOutput.csv";

       
        try (FileWriter fw = new FileWriter(csvFile,true); BufferedWriter bw = new BufferedWriter(fw);PrintWriter out = new PrintWriter(bw)) {
            out.println("For data set "+dataSet+", data structure "+structure);
            out.println(maxMemory+" max memory in MB");
            out.println("");
            
        }catch(IOException e){
            System.out.println("Writer Error");
        }
    }

    private static class MemoryMonitor implements Runnable {
        private final long intervalMillis;
        private volatile boolean running = true;
        private volatile long maxMemoryUsed = 0;

        MemoryMonitor(long intervalMillis) {
            this.intervalMillis = intervalMillis;
        }

        void stop() {
            running = false;
        }

        long getMaxMemoryUsed() {
            return maxMemoryUsed;
        }

        @Override
        public void run() {
            Runtime runtime = Runtime.getRuntime();
            while (running) {
                long usedMemory = runtime.totalMemory() - runtime.freeMemory();
                if (usedMemory > maxMemoryUsed) {
                    maxMemoryUsed = usedMemory;
                }
                //System.out.println("Current memory usage: " + (usedMemory / 1024 / 1024) + " MB");
                try {
                    Thread.sleep(intervalMillis);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}


