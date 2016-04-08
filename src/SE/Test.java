package SE;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.util.Date;
import java.util.Vector;

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

            RecordManager recman = RecordManagerFactory.createRecordManager("data/database");
//
//            PageProperty p = new PageProperty(recman, "test");
//            MappingIndex urlIndex = new MappingIndex(recman, "testURLIndex");
//            MappingIndex wordIndex = new MappingIndex(recman, "testWordIndex");
//            InvertedIndex index = new InvertedIndex(recman, "testInvertedFile");
            ParentChildIndex PCIndex = new ParentChildIndex(recman, "testParentChildFile");
            ForwardIndex wordForwardIndex = new ForwardIndex(recman, "testForwardIndex");

            // ParentChildIndex Test
//            wordForwardIndex.insert(2, 8);
//            wordForwardIndex.insert(2, 82);
//            wordForwardIndex.insert(2, 82);
//            wordForwardIndex.finalize();
//            Vector v = wordForwardIndex.getList(2);
//            System.out.println(v);
//            wordForwardIndex.printAll();

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


            Indexer indexer = new Indexer("data/database", "https://www.google.com.hk");
//            indexer.insertTitle("HI");
//            indexer.insertTitle("Working");
//            indexer.insertTitle("What the fuck");
//            indexer.insertTitle("liking");
//            indexer.insertTitle("a");
//            indexer.insertTitle("sdfsdf");
//            indexer.insertTitle("banana123", 19);
//            indexer.insertTitle("cake");
            Vector<String> txt = new Vector<String>();
            txt.add("hi");
            txt.add("happy");
            txt.add("the");
            txt.add("apple");

            Vector<String> txt2 = new Vector<String>();
            txt2.add("Good");
            txt2.add("Morning!");
            txt2.add("how");
            txt2.add("are");
            txt2.add("you");
            txt2.add("Good");


//            indexer.insertToForwardIndex(txt2);
//
            Crawler crawler = new Crawler("http://www.cse.ust.hk");
            Vector<String> words = crawler.extractWords();
//            System.out.println(words);
//            System.out.println(words.get(6));

            indexer.insertWords(words);
            indexer.insertChildPage("www.google.com");
            indexer.insertChildPage("www.123.com");
            indexer.insertChildPage("www.yahoo.com");
            indexer.insertParentPage("www.parent.com");
            indexer.insertParentPage("www.parent.123com");

//            Boolean s = indexer.pageIsContains("www.bing.com.hk");
//            System.out.printf("BOOL: %s", s);
//            indexer.printUrlMappingIndex();
//            indexer.printWordMappingIndex();
//            indexer.printForwardIndex();
//            indexer.printTitleInvertedIndex();
//            indexer.printBodyInvertedIndex();
//            indexer.insertPageProperty("tes", "url", new Date(1), 100);
//            Boolean b = indexer.pageLastModDateIsUpdated(new Date(12));
//            System.out.print(b);
//            indexer.insertWords(words);
//            indexer.printWordMappingIndex();
//            indexer.printBodyInvertedIndex();
            indexer.printForwardIndex();

            ForwardIndex i = new ForwardIndex(recman, "forwardIndex");
            int s = i.getTermFrequency(1, 1);
            System.out.println("Fff");
            i.printAll();
            indexer.printPageTermFrequency();
            indexer.printChildPages();
            indexer.printParentPages();
            indexer.finalize(); // must call to write to db
//
            Indexer indexer2 = new Indexer("data/database", "https://www.google123123.com.hk");
            Crawler crawler2 = new Crawler("http://course.cse.ust.hk/comp4211/");
            Vector<String> words2 = crawler2.extractWords();
            indexer2.insertWords(words2);
            indexer2.printPageTermFrequency();






        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
