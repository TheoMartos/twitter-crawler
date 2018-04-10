/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui encapsule les Hastables
**/

package analyser;

import common.*;
import java.util.*;
import java.io.*;

class DataContainer implements Serializable
{
    private static final long serialVersionUID = 50000l;
    private Map<String, String> retweets; // entrée ID tweet sortie String nombre de RT.
    private Map<String, List<Strings>> urls; // entrée ID tweet sortie String url.
    private Map<String, List<String>> hashtags; // entré hastag sortie arraylist des tweets.
    private Map<String, String> idToNames;  // Entrée Id user sortie pseudo.
    private Map<String, List<String>> idToTweets; // entrée ID user en sortie arraylist de ses tweets.

    public DataContainer()
    {
        this.retweets = new HashMap<String, String>();
        this.urls = new HashMap<String, List<String>>();
        this.hashtags = new HashMap<String, List<String>>();
        this.idToName = new HashMap<String, String>();
        this.idToTweets = new HashMap<String, List<String>>();
    }

    public synchronized void add(Tweet t)
    {
        // Ajout Retweet
        retweets.put(t.id_str, "" + t.retweet_count);

        // urlExt
        urls.put(t.id_str, new ArrayList<Url>(Array.asList(t.entities.urls)));

        // hashtag
        hashtags.put(t.id_str, new ArrayList<String>(Array.asList(t.entities.hashtags)));

        // idToName
        if(!idToNames.containsKey(t.user.id_str))
            idToNames.put(t.user.id_str, t.user.screen_name);

        // idToTweets
        if(idToTweets.containsKey(t.user.id_str))
        {
            idToTweets.get(t.user.id_str).add(id_str);
        }
        else
        {
            List<String> l = new ArrayList<String>();
            l.add(t.id_str);
            idToTweets.put(t.user.id_str, l);
        }

        if(t.retweeted_status != null)
        {
            Retweet rt = t.retweeted_status;
            retweets.put(rt.id_str, "" + rt.retweet_count);

            // String rtCountString = retweets.get(rt.id_str);
            // if(rtCountString != null && (Intger.parseInt(rtCountString) < Intger.parseInt(rt.retweet_count)))
            // {
            //     retweets.put(rt.id_str, "" + rt.retweet_count);
            // }

            urls.put(rt.id_str, new ArrayList<Url>(Array.asList(rt.entities.urls)));

            hashtags.put(rt.id_str, new ArrayList<String>(Array.asList(rt.entities.hashtags)));

            if(!idToNames.containsKey(rt.user.id_str))
                idToNames.put(rt.user.id_str, rt.user.screen_name);

            List<String> listTweet = idToTweets.get(rt.user.id_str);
            if(listTweet != null && listTweet.indexOf(rt.id_str) == -1)
            {
                listTweet.add(rt.id_str);
            }
        }

        if(t.quoted_status != null)
        {
            QuotedTweet qt = t.quoted_status;
            retweets.put(qt.id_str, "" + qt.retweet_count);

            urls.put(qt.id_str, new ArrayList<Url>(Array.asList(qt.entities.urls)));

            hashtags.put(qt.id_str, new ArrayList<String>(Array.asList(qt.entities.hashtags)));

            if(!idToNames.containsKey(qt.user.id_str))
                idToNames.put(qt.user.id_str, qt.user.screen_name);

            List<String> listTweet = idToTweets.get(qt.user.id_str);
            if(listTweet != null && listTweet.indexOf(qt.id_str) == -1)
            {
                listTweet.add(qt.id_str);
            }
        }
    }

    @Override
    public String toString()
    {
        return retweets.toString() + "\n\n" + urls.toString() + "\n\n" + hashtags.toString() + "\n\n" + idToNames.toString() + "\n\n" + idToTweets.toString();
    }

}
