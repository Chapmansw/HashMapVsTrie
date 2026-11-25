import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class RunningTimeMeasurement {
    /**
     * Calls methods on "counts" numMethodCalls times, each time picking
     * between isKnownWord and numWordsStartingWith according to probabilityToCallIsKnownWord
     *
     * @param counts - object to call methods on
     * @param numMethodCalls - how many method calls to make
     * @param sampleStrings - arguments to use for the method calls;
     *                      cycle through these,
     *                      using sampleStrings[0] for the first method call,
     *                      sampleStrings[1] for the second method call,
     *                      sampleStrings[2] for the third method call,
     *                      ...
     *                      sampleStrings[0] for the (sampleStrings.length+1) method call MOD
     *                      ...
     * @param probabilityToCallIsKnownWord - in [0.0, 1.0]
     *                                     for each method call, there is a probabilityToCallIsKnownWord
     *                                     chance of calling isKnownWord;
     *                                     otherwise, numWordsStartingWith is called.
     * @return the elapsed time for all the method calls in microseconds
     */
    public static long runOneCase(WordCounts counts, int numMethodCalls, String[] sampleStrings, double probabilityToCallIsKnownWord) {
        long startTime = System.nanoTime();

        // TODO: complete this method
        Random rand=new Random();
        for(int x=0;x<numMethodCalls;x++){
            if(rand.nextDouble()<=probabilityToCallIsKnownWord){
                counts.isKnownWord(sampleStrings[x%sampleStrings.length]);// say that numMethod calls is 15, and sampleStrings has three strings in it, that means when x is 2 it should call sample strings[2] and when x is 4, it should call sample strings[1] 
            }else{
                counts.numWordsStartingWith(sampleStrings[x%sampleStrings.length]);
            }
        }
        long elapsedMicros = (System.nanoTime() - startTime) / 1_000;
        return elapsedMicros;
    }
    public static void main(String[] args) {
        // TODO: complete this method to run ALL the running-time cases
        
        String[] datasets={"dna_small","dna_medium","dna_large","wikipedia","random"};
        String[] counterTypes={"trie","hashmap"};
        double[] probs={0.0,0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0};
        String csvFile = "RunningTimeMeasurementOutput.csv";
        WordCounts w;       
        try (FileWriter fw = new FileWriter(csvFile); BufferedWriter bw = new BufferedWriter(fw);PrintWriter out = new PrintWriter(bw)) {
           for(String counterType:counterTypes){
                for(String dataset: datasets){
                    String[] sampleStrings;
                    if(dataset.equals("wikipedia")){
                        sampleStrings=sampleWikipediaStrings;
                    }
                    else if(dataset.equals("random")){
                        sampleStrings=sampleRandomStrings;
                    }
                    else{
                        sampleStrings=sampleDNAStrings;
                    }
                    if(counterType.equals("trie")){
                        w=new WordCountsTrie();
                    }
                    else{
                        w=new WordCountsHashMap();
                    }
                    Parsing.loadData(dataset,w);
                    for(double prob:probs){
                        long result=runOneCase(w, 10,sampleStrings, prob);
                        // System.out.println("For data set "+dataset+", data structure "+counterType+" probablityIsKnownWord: "+prob);
                        // System.out.println(result+ " Microseconds");
                        out.println("For data set "+dataset+", data structure "+counterType+" probablityIsKnownWord: "+prob);
                        out.println(result+ " Microseconds \n");
                    }
                }
           }
        }catch(IOException e){
            System.out.println("Writer Error");
        }
    }

    private static String[] sampleDNAStrings = {"bda", "adbacb", "cccccc"};
    private static String[] sampleWikipediaStrings = {"the", "south", "zsflsda"};
    private static String[] sampleRandomStrings = {"s", "pa", "harps"};
}
