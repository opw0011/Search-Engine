package SE;/* --
COMP336 Lab1 Exercise
Student Name:
Student ID:
Section:
Email:
*/

import jdbm.RecordManager;
import jdbm.htree.HTree;
import jdbm.helper.FastIterator;
import jdk.nashorn.internal.ir.IdentNode;

import java.util.*;
import java.util.ArrayList;
import java.io.IOException;
import java.io.Serializable;

class Posting implements Serializable
{
    //  wordID -> {pageID, [word position]}
    private int pageID;
    private Vector<Integer> wordPosList;
    private static final long serialVersionUID = 1L;    // define serialVersionUID

    public Posting(int pageID) {
        this.pageID = pageID;
        this.wordPosList = new Vector<Integer>();
    }

    @Override
    public String toString() {
        return "Posting{" +
                "pageID=" + pageID +
                ", wordPosList=" + wordPosList +
                '}';
    }

    public boolean insert(int wordPos) {
        if(wordPos >= 0){
            wordPosList.add(wordPos);
            return true;
        }
        return false;
    }

    public boolean contains(int wordPos) {
        if(wordPos >= 0){
            return wordPosList.contains(wordPos);
        }
        return false;
    }

    public boolean remove(int wordPos) {
        if(wordPos >= 0){
            wordPosList.remove((Integer) wordPos);
            return true;
        }
        return false;
    }

    public int getWordFrequency() {
        return wordPosList.size();
    }
}

public class InvertedIndex
{
    private RecordManager recman;
    private HTree hashtable;
//    private static final String DB_ROOT_FOLDER = "data/";

    InvertedIndex(RecordManager recordmanager, String objectname) throws IOException
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
        recman.close();
    }

    //  wordID -> {pageID, [word position]}
    public void insert(int wordID, int pageID, int wordPos) throws IOException
    {
        // pageID -> wordPosList
        String key = Integer.toString(wordID);

        if (hashtable.get(key) == null)
        {
            // initial new map and wordPosList
            Posting p = new Posting(pageID);
            p.insert(wordPos);

            HashMap<Integer, Posting> map = new HashMap<Integer, Posting>();
            map.put(pageID, p);
            hashtable.put(key, map);
        }
        else
        {
            // append the new word pos to existing word posting list
            HashMap<Integer, Posting> map = (HashMap<Integer, Posting>) hashtable.get(key);
            if(contains(wordID, pageID))
            {
                System.out.println("New Pos with existing pageid");
                // get the posting of specific pageID
                Posting posting = map.get(pageID);

                // ensure unique insert of wordPos
                if(! posting.contains(wordPos))
                {
                    posting.insert(wordPos);
//                    hashtable.remove(key);
                    hashtable.put(key, map);
                }

            }
            else
            {
                // have the wordID, but new pageID
                Posting posting = new Posting(pageID);
                posting.insert(wordPos);
                map.put(pageID, posting);
                hashtable.put(key, map);
            }
        }
    }

    public boolean contains(int wordID, int pageID) throws IOException
    {
        String key = Integer.toString(wordID);
        if (hashtable.get(key) != null)
        {
            HashMap<Integer, Posting> map = (HashMap<Integer, Posting>) hashtable.get(key);
            return map.containsKey(pageID);
        }
        return false;
    }

    public boolean delete(int wordID) throws IOException
    {
        // Delete the word and its list from the hashtable
        // remove the index word and its posting list
        String key = Integer.toString(wordID);
        if (hashtable.get(key) == null)
            return false;
        hashtable.remove(key);
        return true;
    }

    // Delete the posting(including all the wordpos) with specific pageID
    public boolean delete(int wordID, int pageID) throws IOException
    {
        String key = Integer.toString(wordID);
        if (hashtable.get(key) == null)
            return false;
        HashMap<Integer, Posting> map = (HashMap<Integer, Posting>) hashtable.get(key);
        map.remove(pageID);
        return true;
    }

    // Delete wordpost of specifc pageid
    public boolean delete(int wordID, int pageID, int wordPos) throws IOException
    {
        String key = Integer.toString(wordID);
        if (hashtable.get(key) == null)
            return false;
        HashMap<Integer, Posting> map = (HashMap<Integer, Posting>) hashtable.get(key);
        Posting posting = map.get(pageID);
        posting.remove(wordPos);
        return true;
    }

    // get the term frequency by counting # of wordPos in posting
    public int getTermFrequency(int wordID, int pageID) throws IOException
    {
        String key = Integer.toString(wordID);
        if (hashtable.get(key) == null)
            return -1;
        HashMap<Integer, Posting> map = (HashMap<Integer, Posting>) hashtable.get(key);
        Posting posting = map.get(pageID);
        return posting.getWordFrequency();
    }


    public void printAll() throws IOException
    {
        // Print all the data in the hashtable
        // ADD YOUR CODES HERE

        // iterate through all keys
        FastIterator iter = hashtable.keys();

        String key;
        System.out.println("Inverted Index:");

        while( (key = (String)iter.next())!=null)
        {
            // get and print the content of each key
//            System.out.println(key + " = " + hashtable.get(key));
//            Posting posting = (Posting) hashtable.get(key);
//            ArrayList<Posting> postingList = (ArrayList<Posting>) hashtable.get(key);
            HashMap<Integer, Posting> map = (HashMap<Integer, Posting>) hashtable.get(key);
            Set<Integer> keys = map.keySet();  //get all keys
            for(Integer i: keys)
            {
                System.out.println(key + " " + map.get(i));
            }
//            System.out.print(key + " = ");
//            for(Posting p : postingList){
//                System.out.print(p + " ");
//            }
//            System.out.println();
        }

    }

}

