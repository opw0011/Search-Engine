package SE;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.util.*;

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
            SearchEngine.search();
            System.exit(-1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
