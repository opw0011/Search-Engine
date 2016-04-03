package SE;

import java.io.IOException;

/**
 * Created by opw on 27/3/2016.
 */
public class Spider {
    public static void main(String[] args)
    {
        System.out.println("SPIDER START...");
        final long startTime = System.currentTimeMillis();

        try {
            MappingIndex urlIndex = new MappingIndex("urlIndex", "urlIndex");
            urlIndex.insert("Word");

            urlIndex.printAll();
            int s = urlIndex.getValue("Peter");
            System.out.println(s);
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
