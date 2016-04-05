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
            // Page property Insert Test
            /*
            PageProperty p = new PageProperty(recman, "test");
            System.out.println(p.isContains(2));
            p.insert(1, "test title", "url", new Date(), 1);
            p.insert(2, "test title 2", "www.google.com", new Date(), 1024);
            Properties ppt = p.get(2);
            System.out.printf("TEST OUTPUT: %s %s %s %s\n", ppt.getUrl(), ppt.getTitle(), ppt.getModDate(), ppt.getSize());
            p.delete(2);
            p.printAll();
            p.finalize();
            */

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
