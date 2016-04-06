package SE;/* --
COMP4321 Lab2 Exercise
Student Name:
Student ID:
Section:
Email:
*/

import java.io.IOException;
import java.util.Vector;
import org.htmlparser.beans.StringBean;
import java.util.*;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import java.net.HttpURLConnection;
import org.htmlparser.util.ParserException;
import java.util.StringTokenizer;
import org.htmlparser.beans.LinkBean;
import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Crawler
{
    private String url;
    Crawler(String _url)
    {
        url = _url;
    }

    public String getUrl() {
        return url;
    }
    //extract title, url]
    //parents and child
    //date, size

    public int getPageSize() throws IOException
    {
        URL inputLink = new URL(url);
        URLConnection linkConnect = inputLink.openConnection();
        BufferedReader newIn = new BufferedReader(new InputStreamReader(linkConnect.getInputStream()));
        String inputln, temp = "";

        while ((inputln = newIn.readLine()) != null){
            temp += inputln;
        }
        newIn.close();
        return temp.length();
    }

    public Vector<String> extractWords() throws ParserException
    {
        // extract words in url and return them
        // use StringTokenizer to tokenize the result from StringBean
        // ADD YOUR CODES HERE
        StringBean sb = new StringBean();

        // StringBean settings
        sb.setLinks (false);
        sb.setReplaceNonBreakingSpaces (true);
        sb.setCollapse (true);
        sb.setURL (url);

        String s = sb.getStrings ();

        Vector<String> v_word = new Vector<String>();
        StringTokenizer st = new StringTokenizer(s);
        while (st.hasMoreTokens()) {
            v_word.add(st.nextToken());
        }
        return v_word;
    }

    public Vector<String> extractLinks() throws ParserException
    {
        // extract links in url and return them
        // ADD YOUR CODES HERE
        LinkBean lb = new LinkBean();
        lb.setURL(this.url);
        URL[] URL_array = lb.getLinks();

        Vector<String> v_link = new Vector<String>();
        for(int i=0; i<URL_array.length; i++){
            v_link.add(URL_array[i].toString());
        }
        return v_link;
    }

    public static void main(String[] args)
    {
        try
        {
            Crawler crawler = new Crawler("http://www.cs.ust.hk/~dlee/4321/");

            int  NumPages = crawler.getPageSize();
            System.out.println("It has number of pages: "+ NumPages);

            Vector<String> words = crawler.extractWords();
            System.out.println("Words in "+crawler.getUrl()+":");
            for(int i = 0; i < words.size(); i++)
                System.out.print(words.get(i)+" ");
            System.out.println("\n\n");

            Vector<String> links = crawler.extractLinks();
            System.out.println("Links in "+crawler.getUrl()+":");
            for(int i = 0; i < links.size(); i++)
                System.out.println(links.get(i));
            System.out.println("");


        }
        catch (ParserException e)
        {
            e.printStackTrace ();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }



}

