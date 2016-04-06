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
//
//            PageProperty p = new PageProperty(recman, "test");
//            MappingIndex urlIndex = new MappingIndex(recman, "testURLIndex");
//            MappingIndex wordIndex = new MappingIndex(recman, "testWordIndex");
//            InvertedIndex index = new InvertedIndex(recman, "testInvertedFile");
//            ParentChildIndex PCIndex = new ParentChildIndex(recman, "testParentChildFile");

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
            /*
//            index.insert(1, 9, 12345);
//            urlIndex.insert("www.google.com.hk");
//            wordIndex.insert("google");
//            wordIndex.insert("yahoo");
//            urlIndex.printAll();
//            wordIndex.printAll();
//            index.insert(wordIndex.getValue("yahoo"), urlIndex.getValue("www.google.com.hk"), 2);
//            index.insert(19, 6,  9);
//            index.insert(19, 6,  10);
//            index.insert(19, 8,  10);
//            index.delete(19, 6);
//            index.insert(10, 2,  50);
//            index.insert(10, 1,  2000);
//            index.insert(10, 2,  9000);
//            index.insert(10, 1,  3000);
//            index.delete(10,2,50);
//            index.delete(10,2);
//            index.delete(10);
//            index.insert(10, 1,  4000);
//            int f = index.getTermFrequency(10, 1);
//            System.out.println(f);
            index.printAll();

            index.finalize();
            */

            // Test Parent Child index
            /*
            PCIndex.insert(1, 3);
            PCIndex.delete(1,3);
            PCIndex.delete(1);
            PCIndex.insert(2,3);
            PCIndex.insert(2,4);
            PCIndex.insert(2,9);
            PCIndex.insert(6,9);
            PCIndex.insert(6,2);

            PCIndex.printAll();
            PCIndex.finalize();
            */

            Indexer indexer = new Indexer("data/database");
//            indexer.insertTitle("HI");
//            indexer.insertTitle("Working");
//            indexer.insertTitle("What the fuck");
//            indexer.insertTitle("liking");
//            indexer.insertTitle("a");
//            indexer.insertTitle("sdfsdf");
            indexer.insertTitle("banana123", 19);
//            indexer.insertTitle("cake");

            indexer.printWordMappingIndex();
            indexer.finalize();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
