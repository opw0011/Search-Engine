package SE;

import jdbm.RecordManager;
import jdbm.helper.FastIterator;
import jdbm.htree.HTree;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by opw on 3/4/2016.
 */

class properties implements Serializable {
    private String title;
    private String url;
    private Date modDate;
    private int size;

    public properties(String title, String url, Date modDate, int size) {
        this.title = title;
        this.url = url;
        this.modDate = modDate;
        this.size = size;
    }

    @Override
    public String toString() {
        return "properties{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", modDate=" + modDate +
                ", size=" + size +
                '}';
    }
}

public class PageProperty {

    private int pageID;
    private RecordManager recman;
    private HTree hashtable;
    private long recid;

    public PageProperty(RecordManager recordmanager, String objectname) throws IOException
    {
        recman = recordmanager;
        recid = recman.getNamedObject(objectname);

        if (recid != 0)
        {
            // if hashtable exist, load it
            hashtable = HTree.load(recman, recid);
        }
        else
        {
            System.out.println("Initial new PageProperty Hashtable");
            // initial hashtable
            hashtable = HTree.createInstance(recman);
            recman.setNamedObject(objectname, hashtable.getRecid());
        }
    }

    public void insert(int pageID, String title, String url, Date modDate, int size) throws IOException {
        properties p = new properties(title, url, modDate, size);
        if(hashtable.get(pageID)==null)
        {
            hashtable.put(Integer.toString(pageID), p);
        }
    }

    public void delete(int pageID) throws IOException {
        hashtable.remove(Integer.toString(pageID));
    }

    public void finalize() throws IOException
    {
        recman.commit();
        recman.close();
    }

    public void printAll() throws IOException
    {
        FastIterator iter = hashtable.keys();

        String key;
        while( (key = (String)iter.next())!=null)
        {
            System.out.printf("KEY= %s, ID= %s\n" , key, hashtable.get(key));
        }
    }
}
