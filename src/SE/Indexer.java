package SE;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import java.io.IOException;

/**
 * Created by opw on 4/6/16.
 */
public class Indexer {
    // Open Global Mapping Index (pageid & docid mapping)
    // Remove stop words and do stemming
    // manipulate invertedIndex (body & title)

    private static MappingIndex urlIndex;
    private static MappingIndex wordIndex;
    private static InvertedIndex titleInvertedIndex;
    private static InvertedIndex bodyInvertedIndex;
    private static PageProperty properyIndex;
    private static ParentChildIndex PCIndex;

    private static StopStem stopStem;

    private static RecordManager recman;

    // dbRootPath = "data/DATABASE_NAME"
    public Indexer(String dbRootPath) throws IOException
    {
        // initial main database
        recman = RecordManagerFactory.createRecordManager(dbRootPath);

        // initial supporting index db
        urlIndex = new MappingIndex(recman, "urlMappingIndex");
        wordIndex = new MappingIndex(recman, "wordMappingIndex");
        titleInvertedIndex = new InvertedIndex(recman, "titleInvertedIndex");
        bodyInvertedIndex = new InvertedIndex(recman, "bodyInvertedIndex");
        properyIndex = new PageProperty(recman, "pagePropertyIndex");
        PCIndex = new ParentChildIndex(recman, "parentChildIndex");

        stopStem = new StopStem("stopwords.txt");
    }

    // input the title string one by one, insert the word into word mapping index and title inverted index
    public void insertTitle(String word, int wordPos)
    {
        if(word.length() <= 0 || wordPos < 0)
        {
            System.out.println("ERROR: Insert Title invalid word/wordPos");
            return;
        }

        if (stopStem.isStopWord(word))
        {
            System.out.printf("\"%s\" is a stop word" , word);
            return;
        }

        // retrieve the stemmed word
        String stem = stopStem.stem(word);
        System.out.println("stem: \"" + stem +"\"");

        try {
            // insert into word mapping indexer
            wordIndex.insert(stem);
            wordIndex.finalize();

            // insert into inverted file
            titleInvertedIndex.insert(wordIndex.getValue(stem), 2, wordPos);
            titleInvertedIndex.printAll();
            titleInvertedIndex.finalize();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void printWordMappingIndex() throws IOException
    {
        wordIndex.printAll();
    }

    public void finalize() throws IOException
    {
        recman.commit();
        recman.close();
    }


}
