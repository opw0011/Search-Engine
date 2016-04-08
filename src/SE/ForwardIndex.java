package SE;

import jdbm.RecordManager;
import jdbm.helper.FastIterator;
import jdbm.htree.HTree;

import java.io.IOException;
import java.util.*;


/**
 * Created by opw on 4/6/16.
 */

// PageID -> (WordID1 ... wordIDN)
public class ForwardIndex {

    private RecordManager recman;
    private HTree hashtable;

    ForwardIndex(RecordManager recordmanager, String objectname) throws IOException
    {
        recman = recordmanager;
        long recid = recman.getNamedObject(objectname);

        if (recid != 0)
        {
            // if hashtable exist, load it
            hashtable = HTree.load(recman, recid);
        }
        else
        {
            hashtable = HTree.createInstance(recman);
            recman.setNamedObject( objectname, hashtable.getRecid() );
        }
    }


    public void finalize() throws IOException
    {
        recman.commit();
//        recman.close();
    }

    // allow duplicated wordIDs, e.g. 2 -> (2, 4, 3, 7)
    public void insert(int pageID, int wordID) throws IOException
    {
        // pageID -> wordPosList
        String key = Integer.toString(pageID);
        Vector<Integer> pages = null;

        if (hashtable.get(key) == null)
            pages = new Vector<Integer>();
        else
            pages = (Vector<Integer>) hashtable.get(key);

        pages.add(wordID);

        hashtable.put(key, pages);  // commit changes
//        recman.commit();
    }

    public void delete(int pageID) throws IOException
    {
        String key = Integer.toString(pageID);
        hashtable.remove(key);
//        recman.commit();
    }


    public Vector<Integer> getList(int pageID) throws IOException
    {
        String key = Integer.toString(pageID);
        Vector<Integer> list = new Vector<Integer>();
        if (hashtable.get(key) != null)
        {
            list = (Vector<Integer>) hashtable.get(key);
        }
        return list;
    }

    public int getTermFrequency(int pageID, int wordID) throws IOException
    {
        String key = Integer.toString(pageID);
        Vector<Integer> list = new Vector<Integer>();
        if (hashtable.get(key) != null)
        {
            list = (Vector<Integer>) hashtable.get(key);
        }
        return Collections.frequency(list, wordID);
    }

//    public List<Integer> getUniqueTerms(int pageID) throws IOException
//    {
//        String key = Integer.toString(pageID);
//        if(hashtable.get(key) == null)
//            return null;
//        Vector<Integer> list = (Vector<Integer>) hashtable.get(key);
//        List<Integer> uniqueList = new ArrayList<Integer>(list);
//        System.out.print(uniqueList);
//        return uniqueList;
//    }

//    public void printUniqueTermsFrequency(int pageID) throws IOException
//    {
//        String key = Integer.toString(pageID);
//        if(hashtable.get(key) == null)
//            return;
//
//        Vector<Integer> list = (Vector<Integer>) hashtable.get(key);
//
//        // wordID, frequency
//        Map<Integer, Integer> map = new HashMap<>();
//        for (int wordID : list) {
//            Integer freq = map.get(wordID);
//            freq = (freq == null) ? 1 : ++freq;
//            map.put(wordID, freq);
//        }
//
//        for (int k : map.keySet()) {
//            System.out.println(k + " " + map.get(k));
//        }
//
//    }

    // get Term Frequency map <wordID, Frequency>, return null is not exist
    public Map<Integer, Integer> getTermFrequencyMap(int pageID) throws IOException
    {
        String key = Integer.toString(pageID);
        if(hashtable.get(key) == null)
            return null;

        Vector<Integer> list = (Vector<Integer>) hashtable.get(key);

        // get wordID, frequency map
        Map<Integer, Integer> map = new HashMap<>();
        for (int wordID : list) {
            Integer freq = map.get(wordID);
            freq = (freq == null) ? 1 : ++freq;
            map.put(wordID, freq);
        }

        return map;
    }

    public void printAll() throws IOException
    {
        // Print all the data in the hashtable

        // iterate through all keys
        FastIterator iter = hashtable.keys();
        String key;
        while( (key = (String)iter.next())!=null)
        {
            System.out.printf("PAGEID= %s, WORDIDS= %s\n" , key, hashtable.get(key));
        }
    }

    public void printPageTermFrequency(int pageID) throws IOException
    {
//        if(forwardIndex.getTermFrequencyMap(pageID) == null)
//        {
//            System.out.println("ERROR: no term frequency map found");
//            return;
//        }
        Map<Integer, Integer> map = getTermFrequencyMap(pageID);
        MappingIndex wordIndex = new MappingIndex(recman, "wordMappingIndex");

        for (int k : map.keySet()) {
//            System.out.printf("%s:%s; ", wordIndex.getKey(k), map.get(k));
            System.out.printf("%s:%s; ", wordIndex.getKey(k), map.get(k));
        }
        System.out.println();
    }

    public Vector<Integer> getExistingPageIdList() throws IOException
    {
        Vector<Integer> v = new Vector<Integer>();
        FastIterator iter = hashtable.keys();
        String key;
        while( (key = (String)iter.next())!=null)
        {
            v.add(Integer.parseInt(key));
//            System.out.printf("PAGEID= %s, WORDIDS= %s\n" , key, hashtable.get(key));
        }
        return v;
    }


}
