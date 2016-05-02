package SE;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.util.*;

/**
 * Created by opw on 27/3/2016.
 */
public class Test {
    public int test() {
        return 123321;
    }

    public static void main(String[] args) {
        System.out.println("Test Start");
        try {
            Vector<String> input = new Vector<String>();
            input.add("play");
            input.add("movie");
            input.add("page");
            SearchEngine.search(input);

            System.exit(-1);

            RecordManager recman = RecordManagerFactory.createRecordManager("data/database");
            InvertedIndex titleInvertedIndex = new InvertedIndex(recman, "titleInvertedIndex");
            MappingIndex wordIndex = new MappingIndex(recman, "wordMappingIndex");
            titleInvertedIndex.printAll();
            wordIndex.printAll();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
