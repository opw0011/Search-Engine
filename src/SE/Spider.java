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
    private static int numOfPage = 0;
    private static Vector<String> DoneList = new Vector<String>();
    private static Vector<String> TaskList = new Vector<String>();
    private static Queue<String> task = new LinkedList();

    public static void main(String[] args) {
        System.out.println("SPIDER START...");
        final long startTime = System.currentTimeMillis();

        try {
            fetch("http://www.cs.ust.hk/~dlee/4321/");
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.printf("PROGRAM RUN FOR %s s\n", (System.currentTimeMillis() - startTime) / 1000d);
        System.out.println("SPIDER END");
    }

    public static void fetch(String url) throws ParserException, IOException{
        System.out.println(url);
        if(DoneList.size()<30)
        {
            Crawler crawler = new Crawler(url);
            Vector<String> links = crawler.extractLinks();
            System.out.println(links);
            int pagesize = crawler.getPageSize();
            System.out.println(pagesize);
            Date lastupdate = crawler.lastUpdate();
            System.out.println(lastupdate);
            Vector<String> title = crawler.extractTitle();
            System.out.println(title);
            Vector<String> word = crawler.extractTitle();
            System.out.println(word);

            for(int i=0; i<links.size(); i++)
            {
                task.add(links.elementAt(i));
            }
            DoneList.add(url);
            task.remove();
            fetch(task.peek());
        }

    }


}
