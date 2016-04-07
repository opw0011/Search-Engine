package SE;

import jdbm.RecordManager;
import jdbm.helper.FastIterator;
import jdbm.htree.HTree;

import java.io.IOException;
import java.util.Vector;


/**
 * Created by opw on 4/6/16.
 */

// ParentPageID -> (ChildPageID1 ... ChildPageIDN)
// ChildPageID -> (ParentPageID1 ... ParentPageIDN)
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
    }

    public void delete(int pageID) throws IOException
    {
        String key = Integer.toString(pageID);
        hashtable.remove(key);
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
}
