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
    private static int MAXPAGE = 15;

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
        if(DoneList.size()<MAXPAGE-1)
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
            Indexer indexer = new Indexer(DB_PATH,url);

            for(int i=0; i<links.size(); i++)
            {
                task.add(links.elementAt(i));
            }
            if(!indexer.pageIsContains())
            {
                indexer.insertWords(word);
                indexer.insertPageProperty(title.toString(),url,lastupdate,pagesize);
                DoneList.add(url);
                task.remove();
            }
            else
            {
                if(indexer.pageLastModDateIsUpdated(lastupdate))
                {
                    indexer.insertWords(word);
                    indexer.insertPageProperty(title.toString(),url,lastupdate,pagesize);
                    DoneList.add(url);
                    task.remove();
                }
            }
            indexer.finalize();
          try{
              fetch(task.peek());
          }catch (Exception e){
              task.remove();
              fetch(task.peek());
          }
        }

    }


}
