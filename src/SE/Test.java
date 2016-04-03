package SE;

import java.io.IOException;

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
            InvertedIndex index = new InvertedIndex("test","ht_test");
//            Posting doc1 = new Posting("1", 1);
//            System.out.println(doc1);
//            index.addEntry("ggg", "2" , 10);


//            doc1.setFreq(5);
//            System.out.println(doc1);

            index.printAll();
            index.finalize();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
