package SE;/* --
COMP336 Lab1 Exercise
Student Name:
Student ID:
Section:
Email:
*/

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;
import jdbm.helper.FastIterator;
import java.util.*;
import java.util.ArrayList;
import java.util.Vector;
import java.io.IOException;
import java.io.Serializable;

class Posting implements Serializable
{
    private int docID;
    private int freq;

    Posting(int docID, int freq)
    {
        this.docID = docID;
        this.freq = freq;
    }

    @Override
    public String toString() {
        return "Posting{" +
                "doc='" + docID + '\'' +
                ", freq=" + freq +
                '}';
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public int getFreq() {
        return freq;
    }

    public int getDocID() {
        return docID;
    }
}

public class InvertedIndex
{
    private RecordManager recman;
    private HTree hashtable;
    private static final String DB_ROOT_FOLDER = "data/";

    InvertedIndex(String recordmanager, String objectname) throws IOException
    {
        recman = RecordManagerFactory.createRecordManager(DB_ROOT_FOLDER + recordmanager);
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

    public void addEntry(String word, int docId, int freq) throws IOException
    {
        ArrayList<Posting> postingList = null;
        Posting newPosting = new Posting(docId, freq);

        // if the index word does not exist
        if(hashtable.get(word)==null)
        {
            postingList = new ArrayList();
            postingList.add(newPosting);
            hashtable.put(word, postingList);
        }
        else
        {
            System.out.println("new word key insert");
            postingList = (ArrayList<Posting>) hashtable.get(word);

            // unsure unique(DOCID) insert : update the frequency if doc id found, otherwise just insert into list
            boolean duplicateFound = false;
            for(Posting p : postingList){
                if( p.getDocID() == docId)
                {
                    p.setFreq(freq);
                    duplicateFound = true;
                    break;
                }
            }
            if(!duplicateFound)
            {
                postingList.add(newPosting);
            }

            hashtable.put(word, postingList);
//            String cur_posting = hashtable.get(word).toString();
//
//            // ensure unique insert
//            if(! cur_posting.contains(posting))
//            {
//                // append the new entry to the last of posting list
//                hashtable.put(word, cur_posting + ' ' + new_posting);
//            }
        }

    }



//    public void addEntry(String word, int x, int y) throws IOException
//    {
//        // Add a "docX Y" entry for the key "word" into hashtable
//        // ADD YOUR CODES HERE
//
//        String new_posting = String.format("doc%d %d", x, y);
//
//        // if the index word does not exist
//        if(hashtable.get(word)==null)
//        {
//            hashtable.put(word, new_posting);
//        }
//        else
//        {
//            String cur_posting = hashtable.get(word).toString();
//
//            // ensure unique insert
//            if(! cur_posting.contains(new_posting))
//            {
//                // append the new entry to the last of posting list
//                hashtable.put(word, cur_posting + ' ' + new_posting);
//            }
//        }
//
//    }
    public void delEntry(String word) throws IOException
    {
        // Delete the word and its list from the hashtable
        // ADD YOUR CODES HERE
        // remove the index word and its posting list
        hashtable.remove(word);
    }
    public void printAll() throws IOException
    {
        // Print all the data in the hashtable
        // ADD YOUR CODES HERE

        // iterate through all keys
        FastIterator iter = hashtable.keys();

        String key;
        while( (key = (String)iter.next())!=null)
        {
            // get and print the content of each key
//            System.out.println(key + " = " + hashtable.get(key));
            System.out.println("printing");
//            Posting posting = (Posting) hashtable.get(key);
            ArrayList<Posting> postingList = (ArrayList<Posting>) hashtable.get(key);
            System.out.print(key + " = ");
            for(Posting p : postingList){
                System.out.print(p + " ");
            }
            System.out.println();
        }

    }

}

