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
//            p.insert(1, "test title", "url", new Date(), 1);
//            p.delete(1);

            p.printAll();
            p.finalize();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
