package SE;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import java.io.IOException;
import java.util.Vector;

/**
 * Created by opw on 4/8/16.
 */
public class Phase1Test {
    public static void main(String[] args)
    {
        System.out.println("test");
        try {
            // load the db
//            RecordManager recman = RecordManagerFactory.createRecordManager("data/database");
//            MappingIndex urlIndex = new MappingIndex(recman, "urlMappingIndex");
            Indexer initIndex = new Indexer("data/database", "");
            initIndex.getUrlLinkList();
//            urlIndex.printAll();
//            Vector<String> pageList = urlIndex.getUrlList();
            Vector<String> pageList = initIndex.getUrlLinkList();


            for(String url : pageList)
            {
                Indexer index = new Indexer("data/database", url);
                System.out.println(url);
                index.printPageTermFrequency();
                index.finalize();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
