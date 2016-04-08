package SE;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
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
    private static ForwardIndex forwardIndex;

    private static ParentChildIndex parentChildIndex;   // parentPageId -> {childPageIdList}
    private static ParentChildIndex childParentIndex;   // childPageId -> {parentPageIdList}

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
        parentChildIndex = new ParentChildIndex(recman, "parentChildIndex");
        childParentIndex = new ParentChildIndex(recman, "childParentIndex");
        forwardIndex = new ForwardIndex(recman, "forwardIndex");

        stopStem = new StopStem("stopwords.txt");

        this.url = url;
        urlIndex.insert(url);
//        urlIndex.finalize();
        this.pageID = urlIndex.getValue(url);
//        System.out.printf("Indexer: url:'%s' mapping to '%s' \n", url, urlIndex.getValue(url));
    }




    /*
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
//            System.out.printf("\"%s\" is a stop word" , word);
            return;
        }

        // retrieve the stemmed word
        String stem = stopStem.stem(word);
//        System.out.println("stem: \"" + stem +"\"");

        try {
            // insert into word mapping indexer
            wordIndex.insert(stem);
//            wordIndex.finalize();

            int stemWordID = wordIndex.getValue(stem);

            // insert into forward index file
            forwardIndex.insert(this.pageID, stemWordID);

            // insert into inverted file
            titleInvertedIndex.insert(stemWordID, 2, wordPos);
            titleInvertedIndex.printAll();
//            titleInvertedIndex.finalize();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    // Helper function: remove stop words + stemming, insert to mapping index and return word ID, wordID: -1 = stop word
    private int insertWordToMappingIndex(String word)
    {
        if(word == null || word.length() <= 0 || word.equals(""))
        {
            System.out.println("ERROR: Insert Title invalid word");
            return -2;
        }
        // skip stop words
        if (stopStem.isStopWord(word))
        {
            //System.out.printf("\"%s\" is a stop word \n" , word);
            return -1;
        }

        // retrieve the stemmed word
        String stem = stopStem.stem(word);
        //System.out.println("stem: \"" + stem +"\"");
        if(stem == null || stem.length() <= 0 || stem.equals(""))
        {
//            System.out.println("ERROR: null char");
            return -2;
        }

        try {
            // insert into word mapping indexer
            wordIndex.insert(stem);

            int stemWordID = wordIndex.getValue(stem);
            return stemWordID;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -3;  // ERROR
    }

    // Remove stop word, stemming, insert to forward indexer and inverted index
    // TODO: isert word to title/body db
    public void insertWords(Vector<String> words) throws IOException
    {
        if(words.isEmpty())
            return;

        forwardIndex.delete(this.pageID);   // clear the old content first

        // loop through the input Vector
        for(int wordPos = 0; wordPos < words.size(); wordPos ++)
        {
            int wordID = insertWordToMappingIndex(words.get(wordPos));    // put new word to mapping index

            // ignore stop word
            if(wordID > 0)
            {
                forwardIndex.insert(this.pageID, wordID);
                // TODO: add to title inverted index
                bodyInvertedIndex.insert(wordID, this.pageID, wordPos);
            }
        }
    }

    // insert properties into properyIndex
    public void insertPageProperty(String title, String url, Date modDate, int size) throws IOException
    {
        properyIndex.insert(this.pageID, title, url, modDate, size);
    }

    // return true if page last mod date is changed -> need to crawl the page again
    public boolean pageLastModDateIsUpdated(Date newDate) throws IOException
    {
        if(properyIndex.get(this.pageID) == null)
            return true;

        Properties p = properyIndex.get(this.pageID);
        System.out.println(newDate);
        System.out.println(p.getModDate());
        return (! newDate.equals(p.getModDate()));
    }

    // return true if page url exist in url mapping index
    public boolean pageIsContains() throws IOException
    {
        return (urlIndex.getValue(this.url) > 0);
    }

    // insert child pages to parentChildIndex, call by the parent page
    public void insertChildPage(String url) throws IOException
    {
        int childPageId = urlIndex.getValue(url);
        if(childPageId < 0) {
            urlIndex.insert(url);
            childPageId = urlIndex.getValue(url);
        }
        parentChildIndex.insert(this.pageID, childPageId);
    }

    // insert parent pages to childParent, call by the child page
    public void insertParentPage(String url) throws IOException
    {
        int parentPageId = urlIndex.getValue(url);
        if(parentPageId < 0) {
            urlIndex.insert(url);
            parentPageId = urlIndex.getValue(url);
        }
        childParentIndex.insert(this.pageID, parentPageId);
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

    public void printBodyInvertedIndex() throws IOException
    {
        bodyInvertedIndex.printAll();
    }

    public void printChildPages() throws IOException
    {
        System.out.println("----- Child Pages -----");
        if(parentChildIndex.getList(this.pageID) == null)
        {
            System.out.println("ERROR: no child page found");
            return;
        }
        Vector<Integer> list = parentChildIndex.getList(this.pageID);
        for (int pid : list) {
            System.out.println(urlIndex.getKey(pid));
        }
    }

    public void printParentPages() throws IOException
    {
        System.out.println("----- Parent Pages -----");
        if(childParentIndex.getList(this.pageID) == null)
        {
            System.out.println("ERROR: no parent page found");
            return;
        }
        Vector<Integer> list = childParentIndex.getList(this.pageID);
        for (int pid : list) {
            System.out.println(urlIndex.getKey(pid));
        }
    }

    public void printPageTermFrequency() throws IOException
    {
        if(forwardIndex.getTermFrequencyMap(this.pageID) == null)
        {
            System.out.println("ERROR: no term frequency map found");
            return;
        }
        Map<Integer, Integer> map = forwardIndex.getTermFrequencyMap(this.pageID);

        for (int k : map.keySet()) {
            System.out.printf("%s:%s; ", wordIndex.getKey(k), map.get(k));
        }
        System.out.println();
    }

    public void printPageProperty() throws IOException
    {
        if(properyIndex.get(this.pageID) == null)
        {
            System.out.println("ERROR: no page property found");
            return;
        }
        Properties ppt = (Properties) properyIndex.get(this.pageID);
        System.out.println(ppt.getUrl());
        System.out.println(ppt.getModDate() + " Size:" +ppt.getSize());

    }

    public Vector<String> getUrlLinkList() throws IOException
    {
        return urlIndex.getUrlList();
    }

    public void finalize() throws IOException
    {
        recman.commit();
        recman.close();
    }


}
