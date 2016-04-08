package SE;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import java.io.IOException;
import java.util.Vector;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileWriter;


/**
 * Created by opw on 4/8/16.
 */
public class Phase1Test {
    public static void main(String[] args) throws IOException {
        System.out.println("test");
        try {
            // load the db
//            RecordManager recman = RecordManagerFactory.createRecordManager("data/database");
//            MappingIndex urlIndex = new MappingIndex(recman, "urlMappingIndex");

            RecordManager recman = RecordManagerFactory.createRecordManager("data/database");
            ForwardIndex fIndex = new ForwardIndex(recman, "forwardIndex");
            MappingIndex urlIndex = new MappingIndex(recman, "urlMappingIndex");
            PageProperty properyIndex = new PageProperty(recman, "pagePropertyIndex");
            ParentChildIndex parentChildIndex = new ParentChildIndex(recman, "parentChildIndex");

            Vector<Integer> urlList = fIndex.getExistingPageIdList();
            System.out.println(urlList);
            for (int pageID : urlList) {
                System.out.println(urlIndex.getKey(pageID));
                properyIndex.printWithPageID(pageID);
                fIndex.printPageTermFrequency(pageID);
                parentChildIndex.printWithPageID(pageID);
                System.out.println("------------------------------");
            }
            System.exit(-1);

            Indexer initIndex = new Indexer("data/database", "");
            initIndex.printForwardIndex();
//            initIndex.getUrlLinkList();
//            urlIndex.printAll();
//            Vector<String> pageList = urlIndex.getUrlList();
            Vector<String> pageList = initIndex.getUrlLinkList();
//
//
            for (String url : pageList) {

                Indexer index = new Indexer("data/database", url);
//                System.out.println(url);


//                PrintStream out = new PrintStream(new FileOutputStream("spider_result.txt", true));
//                System.setOut(out);
//                index.finalize();

            }
//            initIndex.finalize();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
