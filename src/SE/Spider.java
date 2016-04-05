package SE;

import org.htmlparser.util.ParserException;

import java.io.IOException;
import jdbm.*;
import java.util.*;

/**
 * Created by opw on 27/3/2016.
 */
public class Spider {
    private static RecordManager recman;
    private static final String DB_PATH = "data/database";

    public static void main(String[] args)
    {
        System.out.println("SPIDER START...");
        final long startTime = System.currentTimeMillis();

        Crawler crawler = new Crawler("http://www.cs.ust.hk/~dlee/4321/");
        Vector<String> words = null;
        Vector<String> links = null;
        try {
            links = crawler.extractLinks();
        } catch (ParserException e) {
            e.printStackTrace();
        }

        // Indexer
        try {
            recman = RecordManagerFactory.createRecordManager(DB_PATH);
            MappingIndex urlIndex = new MappingIndex(recman, "URLIndex");

            System.out.println("Words in "+crawler.getUrl()+":");

            // insert the words to word-id mapping index
            // insert the links to url-pageid mapping index
            for(int i = 0; i < links.size(); i++)
            {
                System.out.println(links.get(i)+" ");
                urlIndex.insert(links.get(i));
            }

            urlIndex.printAll();
            urlIndex.finalize();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // start fetching page from root index

        // extract all the links

        // extract keywords and do indexing

        // extract page info, e.g. title, last udpate date, size of page




        System.out.printf("PROGRAM RUN FOR %s s\n" , (System.currentTimeMillis() - startTime) / 1000d);
        System.out.println("SPIDER END");
    }

}
