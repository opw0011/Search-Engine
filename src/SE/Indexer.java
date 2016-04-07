package SE;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import java.io.IOException;
import java.util.Vector;

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
    private static ForwardIndex forwardIndex;

    private static StopStem stopStem;

    private static RecordManager recman;
    private static String url;
    private static int pageID;

    // dbRootPath = "data/DATABASE_NAME"
    public Indexer(String dbRootPath, String url) throws IOException
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
        forwardIndex = new ForwardIndex(recman, "forwardIndex");

        stopStem = new StopStem("stopwords.txt");

        this.url = url;
        urlIndex.insert(url);
        urlIndex.finalize();
        this.pageID = urlIndex.getValue(url);
        System.out.printf("Indexer: url:'%s' mapping to '%s' \n", url, urlIndex.getValue(url));
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

            int stemWordID = wordIndex.getValue(stem);

            // insert into forward index file
            forwardIndex.insert(this.pageID, stemWordID);

            // insert into inverted file
            titleInvertedIndex.insert(stemWordID, 2, wordPos);
            titleInvertedIndex.printAll();
            titleInvertedIndex.finalize();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // remove stop words + stemming, insert to mapping index and return word ID, wordID: -1 = stop word
    private int insertWordToMappingIndex(String word)
    {
        // skip stop words
        if (stopStem.isStopWord(word))
        {
            System.out.printf("\"%s\" is a stop word \n" , word);
            return -1;
        }

        // retrieve the stemmed word
        String stem = stopStem.stem(word);
        System.out.println("stem: \"" + stem +"\"");

        try {
            // insert into word mapping indexer
            wordIndex.insert(stem);
            wordIndex.finalize();

            int stemWordID = wordIndex.getValue(stem);
            return stemWordID;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -2;  // ERROR
    }

    public void insertToForwardIndex(Vector<String> words) throws IOException
    {
        if(words.isEmpty())
            return;

        forwardIndex.delete(this.pageID);   // clear the old content first

        // loop through the input Vector
        for(String word : words)
        {
            int wordID = insertWordToMappingIndex(word);    // put new word to mapping index

            // ignore stop word
            if(wordID > 0)
                forwardIndex.insert(this.pageID, wordID);
        }
    }

    public void printWordMappingIndex() throws IOException
    {
        wordIndex.printAll();
    }

    public void printUrlMappingIndex() throws IOException
    {
        urlIndex.printAll();
    }

    public void printForwardIndex() throws IOException
    {
        forwardIndex.printAll();
    }

    public void printTitleInvertedIndex() throws IOException
    {
        titleInvertedIndex.printAll();
    }

    public void finalize() throws IOException
    {
        recman.commit();
        recman.close();
    }


}
