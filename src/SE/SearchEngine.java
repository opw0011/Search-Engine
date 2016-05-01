package SE;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by opw on 1/5/2016.
 */
public class SearchEngine {
    // in descending order
    static Map sortByValue(Map map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        Collections.reverse(list); // sort in descending order

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


    public static void search() {
        System.out.println("Test Start");
        try {
            RecordManager recman = RecordManagerFactory.createRecordManager("data/database");


            ForwardIndex forwardIndex = new ForwardIndex(recman, "forwardIndex");
            MappingIndex wordIndex = new MappingIndex(recman, "wordMappingIndex");
            InvertedIndex bodyInvertedIndex = new InvertedIndex(recman, "bodyInvertedIndex");
            StopStem stopStem = new StopStem("stopwords.txt");

            Vector<String> input = new Vector<String>();
            input.add("paly");
            input.add("movie");

            Vector<String> query = new Vector<String>();
            for (String w : input) {
                // stop word removal
                if (stopStem.isStopWord(w)) {
                    System.out.println("Stop word: " + w);
                    continue;
                }

                // stemming
                String stem = stopStem.stem(w);
                query.add(stem);
            }
            System.out.println(query);


            final int TOTAL_NUM_PAGES = 30;

            // [PAGEID -> value]
            Map<Integer, Double> sumWeightMap = new HashMap<Integer, Double>();
            Map<Integer, Double> scoreMap = new HashMap<Integer, Double>();

            // normal search, union terms : e.g. Computer Games,
            // for each query term
            for (String q : query) {
                int wordID = wordIndex.getValue(q);

                // if query term is found in inverted index
                if (bodyInvertedIndex.get(wordID) != null) {
                    HashMap<Integer, Posting> map = bodyInvertedIndex.get(wordID);  // hashmap <wordID, Posting>

                    // loop the map entries
                    // for each pageID
                    for (Map.Entry<Integer, Posting> entry : map.entrySet()) {
                        int pageID = entry.getKey();
                        Posting posting = entry.getValue();

                        // calculate the weight
                        double tf = (double) posting.getTermFrequency();
                        double max_tf = (double) forwardIndex.getMaxTermFrequency(pageID);
                        double df = (double) bodyInvertedIndex.getDocumentFrequency(wordID);

                        double idf = Math.log(TOTAL_NUM_PAGES / df) / Math.log(2);
                        double weight = (tf / max_tf) * idf;

                        // sum of weight
                        Double sumWeight = sumWeightMap.get(pageID);
                        sumWeight = (sumWeight == null) ? 0 : sumWeight;    // if not exist, initialize with 0
                        sumWeight += weight;
                        sumWeightMap.put(pageID, sumWeight); // store the new value to map

                        System.out.printf("PAGE:%s tf:%s max_tf:%s df:%s idf:%s weight:%s sumWeight:%f\n", pageID, tf, max_tf, df, idf, weight, sumWeight);
                    }
                    System.out.println(map.toString());
                }

                // compute the score of each matched pages
                for (Map.Entry<Integer, Double> entry : sumWeightMap.entrySet()) {
                    int pageID = entry.getKey();
                    double sumWeight = entry.getValue();

                    int numWord = forwardIndex.getPageSize(pageID);
                    double documentLength = Math.sqrt(numWord);

                    double queryLength = Math.sqrt(query.size());
                    double dotProduct = sumWeight;  // for binary vector, sum of weight = dot product

//                    System.out.println("sumwe" + dotProduct + " " + numWord + " " + documentLength);

                    double score = dotProduct / (documentLength * queryLength);
                    System.out.printf("Page:%d socre:%f\n", pageID, score);
                    scoreMap.put(pageID, score);    // put inside a score map
                }


                System.out.println("----------sorted");
                Map<Integer, Double> sotedScoreMap = sortByValue(scoreMap);
                for (Map.Entry<Integer, Double> entry : sotedScoreMap.entrySet()) {
                    int pageID = entry.getKey();
                    double score = entry.getValue();
                    System.out.printf("pageID:%d score:%f\n", pageID, score);
                }


            }


            System.exit(-1);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
