import java.util.HashMap;
import java.util.Map;

public class WordCountsHashMap implements WordCounts {
    
    HashMap<String,Integer> map;
    
    public WordCountsHashMap(){
        map=new HashMap<>();
    }

    /** Adds the word s if it is not already present, and increments its count. */
    public void addWord(String s){
        if(map.get(s)==null){
            map.put(s,1);
        }
        else{
            map.put(s,map.get(s)+1);
        }
    }

    /** Returns true if the word s has a non-zero count. */
    public boolean isKnownWord(String s){
        return map.get(s)!=null;
    }

    /** Returns the count of the word s (0 if it has never been added). */
    public int getWordCount(String s){
        if(isKnownWord(s)){
            return map.get(s);
        }
        return 0; 
    }

    /** Returns the number of distinct words with non-zero counts that start with s. */
    public int numWordsStartingWith(String s){//assuming all keys have non zero counts
        int countAppearances=0;
        for (String key : map.keySet()) {
            if(key.startsWith(s)){
                countAppearances++;
            }
        }
        return countAppearances;
    }

    /**
     * Returns the word with the highest count and its count. Ties go to the word that comes
     * first alphabetically. Returns null if there are no words.
     */
    public WordAndCount mostCommonWord(){
        if(map.isEmpty()) return null;
        int max=0;
        String maxString="";
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if(entry.getValue()>max){ 
                max=entry.getValue();
                maxString=entry.getKey();
            }
        }
        return new WordAndCount(maxString,max);
    }
}
