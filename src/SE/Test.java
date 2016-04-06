package SE;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import java.io.IOException;
import java.util.Date;

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

            RecordManager recman = RecordManagerFactory.createRecordManager("data/test");

            PageProperty p = new PageProperty(recman, "test");
            MappingIndex urlIndex = new MappingIndex(recman, "testURLIndex");
            MappingIndex wordIndex = new MappingIndex(recman, "testWordIndex");

            // Page property Insert Test
            /*
            System.out.println(p.isContains(2));
            p.insert(1, "test title", "url", new Date(), 1);
            p.insert(2, "test title 2", "www.google.com", new Date(), 1024);
            Properties ppt = p.get(2);
            System.out.printf("TEST OUTPUT: %s %s %s %s\n", ppt.getUrl(), ppt.getTitle(), ppt.getModDate(), ppt.getSize());
            p.delete(2);
            p.printAll();
            p.finalize();
            */

            // Mapping Index Test
            /*
            int id = urlIndex.getLastID();
            System.out.println("LastID: " + id);
//            urlIndex.insert("website.com");
//            urlIndex.insert("google.com");
            int r = urlIndex.getValue("website.com");
            String s = urlIndex.getKey(1);
            String t = urlIndex.getKey(2);
            System.out.println(r);
            System.out.println(s);
            System.out.println(t);
            urlIndex.printAll();
            urlIndex.finalize();    // to save all the changes into db
            */

            // Inverted File Test
            InvertedIndex index = new InvertedIndex(recman, "testInvertedFile");
//            index.insert(1, 9, 12345);
            urlIndex.insert("www.google.com.hk");
            wordIndex.insert("google");
            wordIndex.insert("yahoo");
            urlIndex.printAll();
            wordIndex.printAll();
            index.insert(wordIndex.getValue("yahoo"), urlIndex.getValue("www.google.com.hk"), 2);
//            index.insert(19, 6,  9);
//            index.insert(19, 6,  10);
//            index.insert(19, 8,  10);
            index.delete(19, 6);


            index.printAll();
            index.finalize();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
