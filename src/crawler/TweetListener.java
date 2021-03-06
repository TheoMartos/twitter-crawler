/**
*   @author Theo Martos
*   @author Jules Perret
*
*   Classe qui implémente le crawler en lui même
*   Utilise l'API twitter4j en mode stream, implémente donc la classe StatusListener de l'API
**/

package crawler;

import java.util.*;
import twitter4j.*;
import common.*;

class TweetListener implements StatusListener
{
    private static final int CONF_CODE = 0;
    private TwitterStream twitter;
    private Garbage tweets;
    private boolean state;
    private ConfigurationCrawler conf;

    /**
    *   Constructeur de la classe, créé une instance de Garbage et récupère l'instace Twitter de twitter4j
    *   Le boolean state mit à false indique que le crawler est à l'arrêt.
    */
    public TweetListener(Garbage g)
    {
        this.tweets = g;
        this.twitter = new TwitterStreamFactory().getInstance();
        this.conf = (ConfigurationCrawler)ConfigFactory.getConf(CONF_CODE);
        state = false;
    }

    /**
    *   Démarre le crawler avec le filtre passé en paramètre
    */
    public void start()
    {
        // On ajouter à l'objet Twitter l'instance courante comme Listener de tweets
        twitter.addListener(this);
        // On démarre la capture
        twitter.filter(conf.FILTER);
        state = true;
    }

    /**
    *  Stop le crawler
    */
    public void stop()
    {
        twitter.shutdown();
        twitter.removeListener(this);
        state = false;
    }

    /**
    *   Retourne l'état du crawler, false pour arrêté et true pour un fonction
    *   @return l'état du crawler
    */
    public boolean getState()
    {
        return state;
    }

    /**
    *   Gestionnaire de récéption des tweets, appelée à chaque récéption
    *   @param status   le tweet reçu
    */
    @Override
    public void onStatus(Status status)
    {
        // On ajoute le tweet seulement si la limite de tweet a été fixée et atteinte
        if((conf.TWEET_LIMIT != -1 && tweets.size() < conf.TWEET_LIMIT) || conf.TWEET_LIMIT == -1)
            tweets.addStatusElement(status);
    }

    /**
    *   Méthodes héritées de StatusListener non utilisées ici
    */
    @Override
    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}

    @Override
    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}

    @Override
    public void onScrubGeo(long userId, long upToStatusId) {}

    @Override
    public void onStallWarning(StallWarning warning) {}

    @Override
    public void onException(Exception ex)
    {
        System.out.println(conf.ANSI_RED + "An error occured, please check the last log file." + conf.ANSI_RESET);
        ex.printStackTrace(conf.ERROR_STREAM());
    }
}
